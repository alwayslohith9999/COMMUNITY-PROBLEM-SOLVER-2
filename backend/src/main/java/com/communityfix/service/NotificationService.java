package com.communityfix.service;

import com.communityfix.dto.NotificationResponse;
import com.communityfix.model.Meeting;
import com.communityfix.model.Notification;
import com.communityfix.model.Problem;
import com.communityfix.model.User;
import com.communityfix.model.enums.ProblemStatus;
import com.communityfix.repository.NotificationRepository;
import com.communityfix.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(NotificationRepository notificationRepository,
                               UserRepository userRepository,
                               SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getMyNotifications() {
        User currentUser = getCurrentUser();
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(currentUser.getId()).stream()
                .map(this::toNotificationResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getUnreadNotifications() {
        User currentUser = getCurrentUser();
        return notificationRepository.findUnreadByUserId(currentUser.getId()).stream()
                .map(this::toNotificationResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countUnread() {
        try {
            User currentUser = getCurrentUser();
            return notificationRepository.countByUserIdAndIsReadFalse(currentUser.getId());
        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional
    public void markAllAsRead() {
        User currentUser = getCurrentUser();
        notificationRepository.markAllAsRead(currentUser.getId());
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    @Transactional
    public void createCommentNotification(Problem problem, User commenter) {
        Notification notification = Notification.builder()
                .message(commenter.getFullName() + " commented on your problem: " + problem.getTitle())
                .type("COMMENT")
                .link("/problems/" + problem.getId())
                .user(problem.getPostedBy())
                .problem(problem)
                .isRead(false)
                .build();
        Notification saved = notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/notifications/" + problem.getPostedBy().getId(),
                toNotificationResponse(saved));
    }

    @Transactional
    public void createUpvoteNotification(Problem problem, User voter) {
        if (voter.getId().equals(problem.getPostedBy().getId())) return;
        Notification notification = Notification.builder()
                .message(voter.getFullName() + " upvoted your problem: " + problem.getTitle())
                .type("UPVOTE")
                .link("/problems/" + problem.getId())
                .user(problem.getPostedBy())
                .problem(problem)
                .isRead(false)
                .build();
        Notification saved = notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/notifications/" + problem.getPostedBy().getId(),
                toNotificationResponse(saved));
    }

    @Transactional
    public void createVolunteerNotification(Problem problem, User volunteer) {
        Notification notification = Notification.builder()
                .message(volunteer.getFullName() + " volunteered to help with: " + problem.getTitle())
                .type("VOLUNTEER")
                .link("/problems/" + problem.getId())
                .user(problem.getPostedBy())
                .problem(problem)
                .isRead(false)
                .build();
        Notification saved = notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/notifications/" + problem.getPostedBy().getId(),
                toNotificationResponse(saved));
    }

    @Transactional
    public void createStatusNotification(Problem problem, ProblemStatus newStatus) {
        Notification notification = Notification.builder()
                .message("Your problem \"" + problem.getTitle() + "\" has been marked as " + newStatus.name().replace("_", " "))
                .type("STATUS")
                .link("/problems/" + problem.getId())
                .user(problem.getPostedBy())
                .problem(problem)
                .isRead(false)
                .build();
        Notification saved = notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/notifications/" + problem.getPostedBy().getId(),
                toNotificationResponse(saved));
    }

    @Transactional
    public void createMeetingNotification(Meeting meeting) {
        List<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            if (user.getRole().name().equals("RESIDENT") && Boolean.TRUE.equals(user.getApproved())) {
                Notification notification = Notification.builder()
                        .message("New meeting scheduled: " + meeting.getTitle())
                        .type("MEETING")
                        .link("/meetings/" + meeting.getId())
                        .user(user)
                        .meeting(meeting)
                        .isRead(false)
                        .build();
                notificationRepository.save(notification);
            }
        }
    }

    @Transactional
    public void createSolutionAcceptedNotification(Problem problem, User solver) {
        Notification notification = Notification.builder()
                .message("Your suggestion for \"" + problem.getTitle() + "\" was accepted as the solution!")
                .type("SOLUTION")
                .link("/problems/" + problem.getId())
                .user(solver)
                .problem(problem)
                .isRead(false)
                .build();
        notificationRepository.save(notification);
    }

    @Transactional
    public Notification createNotification(User user, String message, String type, String link) {
        Notification notification = Notification.builder()
                .message(message)
                .type(type)
                .link(link)
                .user(user)
                .isRead(false)
                .build();
        return notificationRepository.save(notification);
    }

    private NotificationResponse toNotificationResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .type(notification.getType())
                .link(notification.getLink())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .problemId(notification.getProblem() != null ? notification.getProblem().getId() : null)
                .meetingId(notification.getMeeting() != null ? notification.getMeeting().getId() : null)
                .build();
    }

    private User getCurrentUser() {
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new RuntimeException("Not authenticated");
        }
        var userDetails = (com.communityfix.security.UserDetailsImpl) auth.getPrincipal();
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
