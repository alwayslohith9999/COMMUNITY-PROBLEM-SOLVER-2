package com.communityfix.service;

import com.communityfix.dto.*;
import com.communityfix.model.Meeting;
import com.communityfix.model.Problem;
import com.communityfix.model.User;
import com.communityfix.model.enums.MeetingStatus;
import com.communityfix.repository.MeetingRepository;
import com.communityfix.repository.ProblemRepository;
import com.communityfix.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final NotificationService notificationService;

    public MeetingService(MeetingRepository meetingRepository, ProblemRepository problemRepository,
                          UserRepository userRepository, AuthService authService,
                          NotificationService notificationService) {
        this.meetingRepository = meetingRepository;
        this.problemRepository = problemRepository;
        this.userRepository = userRepository;
        this.authService = authService;
        this.notificationService = notificationService;
    }

    @Transactional(readOnly = true)
    public List<MeetingResponse> getAllMeetings() {
        return meetingRepository.findAll().stream()
                .map(this::toMeetingResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MeetingResponse> getUpcomingMeetings() {
        return meetingRepository.findUpcomingMeetings(LocalDateTime.now()).stream()
                .map(this::toMeetingResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MeetingResponse> getLiveMeetings() {
        return meetingRepository.findOngoingMeetings(LocalDateTime.now()).stream()
                .map(this::toMeetingResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MeetingResponse> getMeetingsByProblem(Long problemId) {
        return meetingRepository.findByProblemId(problemId).stream()
                .map(this::toMeetingResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MeetingResponse> getMyMeetings() {
        User currentUser = authService.getCurrentUser();
        return meetingRepository.findMeetingsByAttendeeId(currentUser.getId()).stream()
                .map(this::toMeetingResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MeetingResponse getMeetingById(Long id) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));
        return toMeetingResponse(meeting);
    }

    @Transactional
    public MeetingResponse createMeeting(MeetingCreateRequest request) {
        User currentUser = authService.getCurrentUser();

        Meeting.MeetingBuilder meetingBuilder = Meeting.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .scheduledAt(request.getScheduledAt())
                .jitsiRoom("communityfix-" + UUID.randomUUID().toString().substring(0, 8))
                .status(MeetingStatus.SCHEDULED)
                .host(currentUser)
                .notifyAll(request.getNotifyAll());

        if (request.getProblemId() != null) {
            Problem problem = problemRepository.findById(request.getProblemId())
                    .orElseThrow(() -> new RuntimeException("Problem not found"));
            meetingBuilder.problem(problem);
        }

        Meeting saved = meetingRepository.save(meetingBuilder.build());

        if (request.getNotifyAll()) {
            notificationService.createMeetingNotification(saved);
        }

        return toMeetingResponse(saved);
    }

    @Transactional
    public void joinMeeting(Long meetingId) {
        User currentUser = authService.getCurrentUser();
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        if (!meeting.getAttendees().stream().anyMatch(u -> u.getId().equals(currentUser.getId()))) {
            meeting.getAttendees().add(currentUser);
            meetingRepository.save(meeting);
        }
    }

    @Transactional
    public void leaveMeeting(Long meetingId) {
        User currentUser = authService.getCurrentUser();
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        meeting.setAttendees(meeting.getAttendees().stream()
                .filter(u -> !u.getId().equals(currentUser.getId()))
                .collect(Collectors.toList()));
        meetingRepository.save(meeting);
    }

    @Transactional(readOnly = true)
    public long countTodayMeetings() {
        return meetingRepository.countTodayMeetings();
    }

    private MeetingResponse toMeetingResponse(Meeting meeting) {
        User currentUser = getCurrentUserOrNull();
        boolean isUserAttending = currentUser != null &&
                meeting.getAttendees().stream().anyMatch(u -> u.getId().equals(currentUser.getId()));

        boolean isLive = meeting.getStatus() == MeetingStatus.SCHEDULED &&
                meeting.getScheduledAt() != null &&
                meeting.getScheduledAt().isBefore(LocalDateTime.now().plusMinutes(5)) &&
                meeting.getScheduledAt().isAfter(LocalDateTime.now().minusHours(2));

        return MeetingResponse.builder()
                .id(meeting.getId())
                .title(meeting.getTitle())
                .description(meeting.getDescription())
                .scheduledAt(meeting.getScheduledAt())
                .jitsiRoom(meeting.getJitsiRoom())
                .status(meeting.getStatus())
                .host(toUserSummary(meeting.getHost()))
                .problem(meeting.getProblem() != null ? ProblemSummary.builder()
                        .id(meeting.getProblem().getId())
                        .title(meeting.getProblem().getTitle())
                        .category(meeting.getProblem().getCategory().name())
                        .build() : null)
                .attendees(meeting.getAttendees().stream()
                        .map(this::toUserSummary)
                        .collect(Collectors.toList()))
                .attendeeCount(meeting.getAttendees().size())
                .isUserAttending(isUserAttending)
                .isLive(isLive)
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
                .avatarInitials(getInitials(user.getFullName()))
                .build();
    }

    private User getCurrentUserOrNull() {
        try {
            return authService.getCurrentUser();
        } catch (Exception e) {
            return null;
        }
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
