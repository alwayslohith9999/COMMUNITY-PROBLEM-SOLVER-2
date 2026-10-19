package com.communityfix.repository;

import com.communityfix.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByUserIdAndProblemId(Long userId, Long problemId);
    Optional<Vote> findByUserIdAndCommentId(Long userId, Long commentId);
    boolean existsByUserIdAndProblemId(Long userId, Long problemId);
    boolean existsByUserIdAndCommentId(Long userId, Long commentId);
    void deleteByUserIdAndProblemId(Long userId, Long problemId);
    void deleteByUserIdAndCommentId(Long userId, Long commentId);
    long countByProblemIdAndIsUpvoteTrue(Long problemId);
    long countByCommentIdAndIsUpvoteTrue(Long commentId);
}
