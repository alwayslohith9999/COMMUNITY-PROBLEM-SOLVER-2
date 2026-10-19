package com.communityfix.repository;

import com.communityfix.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    @Query("SELECT a FROM Announcement a ORDER BY a.pinned DESC, a.createdAt DESC")
    List<Announcement> findAllOrderByPinnedAndDate();

    List<Announcement> findByPinnedTrueOrderByCreatedAtDesc();
}
