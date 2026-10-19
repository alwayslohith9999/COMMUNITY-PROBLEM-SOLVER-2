package com.communityfix.controller;

import com.communityfix.dto.UserSummary;
import com.communityfix.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserSummary>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllApprovedResidents());
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<UserSummary>> getLeaderboard() {
        return ResponseEntity.ok(userService.getLeaderboard());
    }

    @GetMapping("/block/{block}")
    public ResponseEntity<List<UserSummary>> getUsersByBlock(@PathVariable String block) {
        return ResponseEntity.ok(userService.getResidentsByBlock(block));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserSummary> getUserProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserProfile(id));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<UserSummary>> getPendingApprovals() {
        return ResponseEntity.ok(userService.getPendingApprovals());
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Map<String, String>> approveUser(@PathVariable Long id) {
        userService.approveUser(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User approved");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Map<String, String>> rejectUser(@PathVariable Long id) {
        userService.rejectUser(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User rejected");
        return ResponseEntity.ok(response);
    }
}
