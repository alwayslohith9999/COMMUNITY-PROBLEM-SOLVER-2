package com.communityfix.repository;

import com.communityfix.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByProblemIdOrderByCreatedAtAsc(Long problemId);
    List<Comment> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT COUNT(c) FROM Comment c")
    long countAllComments();
}
