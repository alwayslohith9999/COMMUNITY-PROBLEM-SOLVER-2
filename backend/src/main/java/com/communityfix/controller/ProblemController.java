package com.communityfix.controller;

import com.communityfix.dto.*;
import com.communityfix.model.enums.Category;
import com.communityfix.model.enums.ProblemStatus;
import com.communityfix.model.enums.UrgencyLevel;
import com.communityfix.service.ProblemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/problems")
@CrossOrigin(origins = "http://localhost:5173")
public class ProblemController {

    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @GetMapping
    public ResponseEntity<List<ProblemResponse>> getAllProblems(
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) UrgencyLevel urgency,
            @RequestParam(required = false) ProblemStatus status,
            @RequestParam(required = false) String block,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "latest") String sort) {
        return ResponseEntity.ok(problemService.getAllProblems(category, urgency, status, block, search, sort));
    }

    @GetMapping("/recent")
    public ResponseEntity<List<ProblemResponse>> getRecentProblems(
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(problemService.getRecentProblems(limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProblemResponse> getProblemById(@PathVariable Long id) {
        return ResponseEntity.ok(problemService.getProblemById(id));
    }

    @PostMapping
    public ResponseEntity<ProblemResponse> createProblem(@Valid @RequestBody ProblemCreateRequest request) {
        return ResponseEntity.ok(problemService.createProblem(request));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ProblemResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(problemService.updateProblemStatus(id, request));
    }

    @PostMapping("/{id}/assign/{volunteerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Map<String, String>> assignProblem(
            @PathVariable Long id,
            @PathVariable Long volunteerId) {
        problemService.assignProblem(id, volunteerId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Problem assigned successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/upvote")
    public ResponseEntity<Map<String, Object>> toggleUpvote(@PathVariable Long id) {
        problemService.toggleUpvote(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Upvote toggled");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/volunteer")
    public ResponseEntity<Map<String, Object>> toggleVolunteer(@PathVariable Long id) {
        problemService.toggleVolunteer(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Volunteer status toggled");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Map<String, String>> deleteProblem(@PathVariable Long id) {
        problemService.deleteProblem(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Problem deleted");
        return ResponseEntity.ok(response);
    }
}
