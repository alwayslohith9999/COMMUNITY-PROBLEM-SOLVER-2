package com.communityfix.controller;

import com.communityfix.dto.CommentCreateRequest;
import com.communityfix.dto.CommentResponse;
import com.communityfix.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/problems/{problemId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long problemId) {
        return ResponseEntity.ok(commentService.getCommentsByProblem(problemId));
    }

    @PostMapping("/problems/{problemId}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long problemId,
            @Valid @RequestBody CommentCreateRequest request) {
        return ResponseEntity.ok(commentService.addComment(problemId, request));
    }

    @PostMapping("/comments/{commentId}/solution")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<CommentResponse> markAsSolution(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.markAsSolution(commentId));
    }

    @PostMapping("/comments/{commentId}/upvote")
    public ResponseEntity<Map<String, String>> upvoteComment(@PathVariable Long commentId) {
        commentService.toggleUpvote(commentId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Comment upvoted");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/me/comments")
    public ResponseEntity<List<CommentResponse>> getMyComments() {
        return ResponseEntity.ok(commentService.getMyComments());
    }
}
