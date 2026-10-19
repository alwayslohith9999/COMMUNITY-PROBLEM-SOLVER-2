package com.communityfix.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStatsResponse {
    private long totalResidents;
    private long pendingApprovals;
    private long openProblems;
    private long inProgressProblems;
    private long resolvedProblems;
    private long escalatedProblems;
    private long criticalOpen;
    private long resolvedThisMonth;
    private double averageResolutionTime;
    private long activeMeetingsToday;
    private long totalComments;
    private long totalMeetings;
}
