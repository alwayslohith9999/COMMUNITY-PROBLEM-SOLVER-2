package com.communityfix.service;

import com.communityfix.dto.CommentCreateRequest;
import com.communityfix.dto.CommentResponse;
import com.communityfix.dto.UserSummary;
import com.communityfix.model.Comment;
import com.communityfix.model.Problem;
import com.communityfix.model.User;
import com.communityfix.repository.CommentRepository;
import com.communityfix.repository.ProblemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ProblemRepository problemRepository;
    private final AuthService authService;
    private final NotificationService notificationService;

    public CommentService(CommentRepository commentRepository, ProblemRepository problemRepository,
                          AuthService authService, NotificationService notificationService) {
        this.commentRepository = commentRepository;
        this.problemRepository = problemRepository;
        this.authService = authService;
        this.notificationService = notificationService;
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByProblem(Long problemId) {
        List<Comment> comments = commentRepository.findByProblemIdOrderByCreatedAtAsc(problemId);
        return comments.stream()
                .filter(c -> c.getParent() == null)
                .map(this::toCommentResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse addComment(Long problemId, CommentCreateRequest request) {
        User currentUser = authService.getCurrentUser();
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("Problem not found"));

        Comment.CommentBuilder commentBuilder = Comment.builder()
                .content(request.getContent())
                .isVolunteerOffer(request.getIsVolunteerOffer())
                .problem(problem)
                .user(currentUser);

        if (request.getParentId() != null) {
            Comment parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));
            commentBuilder.parent(parent);
        }

        Comment saved = commentRepository.save(commentBuilder.build());

        if (!currentUser.getId().equals(problem.getPostedBy().getId())) {
            notificationService.createCommentNotification(problem, currentUser);
        }

        return toCommentResponse(saved);
    }

    @Transactional
    public CommentResponse markAsSolution(Long commentId) {
        User currentUser = authService.getCurrentUser();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        Problem problem = comment.getProblem();

        comment.setIsSolution(true);
        comment.setUpvoteCount(comment.getUpvoteCount() + 5);

        Comment saved = commentRepository.save(comment);

        User commentUser = comment.getUser();
        commentUser.setReputation(commentUser.getReputation() + 10);

        notificationService.createSolutionAcceptedNotification(problem, commentUser);

        return toCommentResponse(saved);
    }

    @Transactional
    public void toggleUpvote(Long commentId) {
        // Simplified - in production would use Vote entity
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setUpvoteCount(comment.getUpvoteCount() + 1);
        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getMyComments() {
        User currentUser = authService.getCurrentUser();
        return commentRepository.findByUserIdOrderByCreatedAtDesc(currentUser.getId()).stream()
                .map(this::toCommentResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countTotalComments() {
        return commentRepository.countAllComments();
    }

    private CommentResponse toCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .isVolunteerOffer(comment.getIsVolunteerOffer())
                .isSolution(comment.getIsSolution())
                .upvoteCount(comment.getUpvoteCount())
                .createdAt(comment.getCreatedAt())
                .user(toUserSummary(comment.getUser()))
                .replies(comment.getReplies() != null ? comment.getReplies().stream()
                        .map(this::toCommentResponse)
                        .collect(Collectors.toList()) : List.of())
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
