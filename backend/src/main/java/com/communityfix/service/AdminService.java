package com.communityfix.service;

import com.communityfix.dto.DashboardStatsResponse;
import com.communityfix.model.enums.ProblemStatus;
import com.communityfix.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final CommentRepository commentRepository;
    private final MeetingRepository meetingRepository;

    public AdminService(UserRepository userRepository, ProblemRepository problemRepository,
                        CommentRepository commentRepository, MeetingRepository meetingRepository) {
        this.userRepository = userRepository;
        this.problemRepository = problemRepository;
        this.commentRepository = commentRepository;
        this.meetingRepository = meetingRepository;
    }

    @Transactional(readOnly = true)
    public DashboardStatsResponse getDashboardStats() {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);

        long totalResidents = userRepository.countByApprovedTrue();
        long pendingApprovals = userRepository.countByApprovedFalse();
        long openProblems = problemRepository.countByStatus(ProblemStatus.OPEN);
        long inProgressProblems = problemRepository.countByStatus(ProblemStatus.IN_PROGRESS);
        long resolvedProblems = problemRepository.countByStatus(ProblemStatus.RESOLVED);
        long escalatedProblems = problemRepository.countByStatus(ProblemStatus.ESCALATED);
        long criticalOpen = problemRepository.countByUrgencyAndStatus(
                com.communityfix.model.enums.UrgencyLevel.CRITICAL, ProblemStatus.OPEN);
        long resolvedThisMonth = problemRepository.countResolvedSince(startOfMonth);
        Double avgResolutionTime = problemRepository.calculateAverageResolutionTime();
        long activeMeetingsToday = meetingRepository.countTodayMeetings();
        long totalComments = commentRepository.countAllComments();
        long totalMeetings = meetingRepository.count();

        return DashboardStatsResponse.builder()
                .totalResidents(totalResidents)
                .pendingApprovals(pendingApprovals)
                .openProblems(openProblems)
                .inProgressProblems(inProgressProblems)
                .resolvedProblems(resolvedProblems)
                .escalatedProblems(escalatedProblems)
                .criticalOpen(criticalOpen)
                .resolvedThisMonth(resolvedThisMonth)
                .averageResolutionTime(avgResolutionTime != null ? avgResolutionTime : 0.0)
                .activeMeetingsToday(activeMeetingsToday)
                .totalComments(totalComments)
                .totalMeetings(totalMeetings)
                .build();
    }
}
