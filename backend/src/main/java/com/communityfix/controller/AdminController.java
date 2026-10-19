package com.communityfix.controller;

import com.communityfix.dto.DashboardStatsResponse;
import com.communityfix.dto.ProblemResponse;
import com.communityfix.dto.UserSummary;
import com.communityfix.service.AdminService;
import com.communityfix.service.ProblemService;
import com.communityfix.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;
    private final ProblemService problemService;

    public AdminController(AdminService adminService, UserService userService, ProblemService problemService) {
        this.adminService = adminService;
        this.userService = userService;
        this.problemService = problemService;
    }

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsResponse> getStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    @GetMapping("/pending-approvals")
    public ResponseEntity<List<UserSummary>> getPendingApprovals() {
        return ResponseEntity.ok(userService.getPendingApprovals());
    }

    @GetMapping("/problems")
    public ResponseEntity<List<ProblemResponse>> getAllProblems() {
        return ResponseEntity.ok(problemService.getAllProblems(null, null, null, null, null, "latest"));
    }

    @PostMapping("/users/{id}/approve")
    public ResponseEntity<Map<String, String>> approveUser(@PathVariable Long id) {
        userService.approveUser(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User approved");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users/{id}/reject")
    public ResponseEntity<Map<String, String>> rejectUser(@PathVariable Long id) {
        userService.rejectUser(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User rejected");
        return ResponseEntity.ok(response);
    }
}
