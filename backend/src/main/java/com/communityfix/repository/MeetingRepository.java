package com.communityfix.repository;

import com.communityfix.model.Meeting;
import com.communityfix.model.enums.MeetingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findByStatusOrderByScheduledAtAsc(MeetingStatus status);

    @Query("SELECT m FROM Meeting m WHERE m.scheduledAt > :now ORDER BY m.scheduledAt ASC")
    List<Meeting> findUpcomingMeetings(LocalDateTime now);

    @Query("SELECT m FROM Meeting m WHERE m.scheduledAt <= :now AND m.status = 'SCHEDULED' ORDER BY m.scheduledAt DESC")
    List<Meeting> findOngoingMeetings(LocalDateTime now);

    @Query("SELECT m FROM Meeting m WHERE m.host.id = :userId ORDER BY m.scheduledAt DESC")
    List<Meeting> findByHostId(Long userId);

    @Query("SELECT COUNT(m) FROM Meeting m WHERE m.status = 'SCHEDULED' AND DATE(m.scheduledAt) = CURRENT_DATE")
    long countTodayMeetings();

    @Query("SELECT m FROM Meeting m JOIN m.attendees a WHERE a.id = :userId ORDER BY m.scheduledAt DESC")
    List<Meeting> findMeetingsByAttendeeId(Long userId);

    List<Meeting> findByProblemId(Long problemId);
}
