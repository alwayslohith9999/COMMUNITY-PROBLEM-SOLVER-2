package com.communityfix.repository;

import com.communityfix.model.Problem;
import com.communityfix.model.enums.Category;
import com.communityfix.model.enums.ProblemStatus;
import com.communityfix.model.enums.UrgencyLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

    List<Problem> findByPostedByIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT p FROM Problem p WHERE " +
           "(:category IS NULL OR p.category = :category) AND " +
           "(:urgency IS NULL OR p.urgency = :urgency) AND " +
           "(:status IS NULL OR p.status = :status) AND " +
           "(:block IS NULL OR p.block = :block) AND " +
           "(:search IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "ORDER BY " +
           "CASE WHEN :sort = 'mostVoted' THEN (SELECT COUNT(v) FROM Vote v WHERE v.problem = p AND v.isUpvote = true) END DESC, " +
           "CASE WHEN :sort = 'oldest' THEN p.createdAt END ASC, " +
           "CASE WHEN :sort IS NULL OR :sort = 'latest' THEN p.createdAt END DESC")
    List<Problem> findWithFilters(
            @Param("category") Category category,
            @Param("urgency") UrgencyLevel urgency,
            @Param("status") ProblemStatus status,
            @Param("block") String block,
            @Param("search") String search,
            @Param("sort") String sort);

    @Query("SELECT p FROM Problem p ORDER BY p.createdAt DESC")
    List<Problem> findRecentProblems(Pageable pageable);

    long countByStatus(ProblemStatus status);
    long countByUrgencyAndStatus(UrgencyLevel urgency, ProblemStatus status);
    long countByResolvedAtAfter(LocalDateTime date);

    @Query("SELECT AVG(DATEDIFF(HOUR, p.createdAt, p.resolvedAt)) / 24.0 FROM Problem p WHERE p.status = 'RESOLVED' AND p.resolvedAt IS NOT NULL")
    Double calculateAverageResolutionTime();

    @Query("SELECT p.category as category, COUNT(p) as count FROM Problem p GROUP BY p.category")
    List<Object[]> countByCategory();

    @Query("SELECT p.block as block, COUNT(p) as count FROM Problem p GROUP BY p.block")
    List<Object[]> countByBlock();

    List<Problem> findByCategoryAndIdNot(Category category, Long id);

    @Query("SELECT COUNT(p) FROM Problem p WHERE p.createdAt >= :since")
    long countCreatedSince(@Param("since") LocalDateTime since);

    @Query("SELECT COUNT(p) FROM Problem p WHERE p.resolvedAt >= :since")
    long countResolvedSince(@Param("since") LocalDateTime since);

    @Query("SELECT COUNT(p) FROM Problem p WHERE p.assignedTo.id = :userId AND p.status = 'IN_PROGRESS'")
    long countByAssignedToIdAndStatusInProgress(@Param("userId") Long userId);
}
