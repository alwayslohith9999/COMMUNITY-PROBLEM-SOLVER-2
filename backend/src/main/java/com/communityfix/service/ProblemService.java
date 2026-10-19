package com.communityfix.service;

import com.communityfix.dto.*;
import com.communityfix.model.Problem;
import com.communityfix.model.User;
import com.communityfix.model.Vote;
import com.communityfix.model.enums.*;
import com.communityfix.repository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final CommentRepository commentRepository;
    private final AuthService authService;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    public ProblemService(ProblemRepository problemRepository, UserRepository userRepository,
                          VoteRepository voteRepository, CommentRepository commentRepository,
                          AuthService authService, NotificationService notificationService,
                          SimpMessagingTemplate messagingTemplate) {
        this.problemRepository = problemRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
        this.commentRepository = commentRepository;
        this.authService = authService;
        this.notificationService = notificationService;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional(readOnly = true)
    public List<ProblemResponse> getAllProblems(Category category, UrgencyLevel urgency,
                                                 ProblemStatus status, String block, String search, String sort) {
        List<Problem> problems = problemRepository.findWithFilters(category, urgency, status, block, search, sort);
        User currentUser = getCurrentUserOrNull();
        return problems.stream()
                .map(p -> toProblemResponse(p, currentUser))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProblemResponse> getRecentProblems(int limit) {
        List<Problem> problems = problemRepository.findRecentProblems(PageRequest.of(0, limit));
        User currentUser = getCurrentUserOrNull();
        return problems.stream()
                .map(p -> toProblemResponse(p, currentUser))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProblemResponse getProblemById(Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem not found"));
        User currentUser = getCurrentUserOrNull();
        return toProblemResponse(problem, currentUser);
    }

    @Transactional
    public ProblemResponse createProblem(ProblemCreateRequest request) {
        User currentUser = authService.getCurrentUser();

        Problem problem = Problem.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .urgency(request.getUrgency())
                .status(ProblemStatus.OPEN)
                .block(request.getBlock())
                .flatNumber(request.getFlatNumber())
                .photoUrl(request.getPhotoUrl())
                .notifyNeighbors(request.getNotifyNeighbors())
                .postedBy(currentUser)
                .build();

        Problem saved = problemRepository.save(problem);

        if (saved.getNotifyNeighbors()) {
            messagingTemplate.convertAndSend("/topic/feed", "New problem posted in Block " + saved.getBlock());
        }

        return toProblemResponse(saved, currentUser);
    }

    @Transactional
    public ProblemResponse updateProblemStatus(Long id, StatusUpdateRequest request) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem not found"));

        ProblemStatus oldStatus = problem.getStatus();
        problem.setStatus(request.getStatus());

        if (request.getStatus() == ProblemStatus.RESOLVED) {
            problem.setResolvedAt(LocalDateTime.now());
            problem.setResolvedBy(authService.getCurrentUser());
        }

        Problem updated = problemRepository.save(problem);

        if (oldStatus != request.getStatus()) {
            notificationService.createStatusNotification(updated, request.getStatus());
            messagingTemplate.convertAndSend("/topic/feed", "Problem #" + id + " status changed to " + request.getStatus());
        }

        return toProblemResponse(updated, authService.getCurrentUser());
    }

    @Transactional
    public void assignProblem(Long id, Long volunteerId) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Problem not found"));
        User volunteer = userRepository.findById(volunteerId)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));

        problem.setAssignedTo(volunteer);
        problem.setStatus(ProblemStatus.IN_PROGRESS);
        problemRepository.save(problem);
    }

    @Transactional
    public void toggleUpvote(Long problemId) {
        User currentUser = authService.getCurrentUser();
        boolean hasVoted = voteRepository.existsByUserIdAndProblemId(currentUser.getId(), problemId);

        if (hasVoted) {
            voteRepository.deleteByUserIdAndProblemId(currentUser.getId(), problemId);
        } else {
            Problem problem = problemRepository.findById(problemId)
                    .orElseThrow(() -> new RuntimeException("Problem not found"));
            Vote vote = Vote.builder()
                    .user(currentUser)
                    .problem(problem)
                    .isUpvote(true)
                    .build();
            voteRepository.save(vote);
            notificationService.createUpvoteNotification(problem, currentUser);
        }
    }

    @Transactional
    public void toggleVolunteer(Long problemId) {
        User currentUser = authService.getCurrentUser();
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("Problem not found"));

        if (problem.getVolunteers().contains(currentUser)) {
            problem.getVolunteers().remove(currentUser);
        } else {
            problem.getVolunteers().add(currentUser);
            notificationService.createVolunteerNotification(problem, currentUser);
        }
        problemRepository.save(problem);
    }

    @Transactional
    public void deleteProblem(Long id) {
        problemRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long countByStatus(ProblemStatus status) {
        return problemRepository.countByStatus(status);
    }

    @Transactional(readOnly = true)
    public long countCriticalOpen() {
        return problemRepository.countByUrgencyAndStatus(UrgencyLevel.CRITICAL, ProblemStatus.OPEN);
    }

    private User getCurrentUserOrNull() {
        try {
            return authService.getCurrentUser();
        } catch (Exception e) {
            return null;
        }
    }

    public ProblemResponse toProblemResponse(Problem problem, User currentUser) {
        boolean userHasUpvoted = currentUser != null &&
                voteRepository.existsByUserIdAndProblemId(currentUser.getId(), problem.getId());
        boolean userIsVolunteer = currentUser != null &&
                problem.getVolunteers().stream().anyMatch(v -> v.getId().equals(currentUser.getId()));

        return ProblemResponse.builder()
                .id(problem.getId())
                .title(problem.getTitle())
                .description(problem.getDescription())
                .category(problem.getCategory())
                .urgency(problem.getUrgency())
                .status(problem.getStatus())
                .block(problem.getBlock())
                .flatNumber(problem.getFlatNumber())
                .photoUrl(problem.getPhotoUrl())
                .upvoteCount(problem.getUpvoteCount())
                .commentCount(problem.getCommentCount())
                .volunteerCount(problem.getVolunteerCount())
                .createdAt(problem.getCreatedAt())
                .updatedAt(problem.getUpdatedAt())
                .resolvedAt(problem.getResolvedAt())
                .postedBy(toUserSummary(problem.getPostedBy()))
                .assignedTo(problem.getAssignedTo() != null ? toUserSummary(problem.getAssignedTo()) : null)
                .resolvedBy(problem.getResolvedBy() != null ? toUserSummary(problem.getResolvedBy()) : null)
                .userHasUpvoted(userHasUpvoted)
                .userIsVolunteer(userIsVolunteer)
                .build();
    }

    private UserSummary toUserSummary(User user) {
        return UserSummary.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .flatNumber(user.getFlatNumber())
                .block(user.getBlock())
                .phoneNumber(user.getPhoneNumber())
                .reputation(user.getReputation())
                .reputationRank(user.getReputationRank())
                .avatarInitials(getInitials(user.getFullName()))
                .build();
    }

    private String getInitials(String name) {
        if (name == null || name.isEmpty()) return "?";
        String[] parts = name.split(" ");
        if (parts.length >= 2) {
            return (parts[0].charAt(0) + "" + parts[parts.length - 1].charAt(0)).toUpperCase();
        }
        return name.substring(0, Math.min(2, name.length())).toUpperCase();
    }
}
