package com.communityfix.controller;

import com.communityfix.dto.*;
import com.communityfix.service.MeetingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/meetings")
@CrossOrigin(origins = "http://localhost:5173")
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @GetMapping
    public ResponseEntity<List<MeetingResponse>> getAllMeetings() {
        return ResponseEntity.ok(meetingService.getAllMeetings());
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<MeetingResponse>> getUpcomingMeetings() {
        return ResponseEntity.ok(meetingService.getUpcomingMeetings());
    }

    @GetMapping("/live")
    public ResponseEntity<List<MeetingResponse>> getLiveMeetings() {
        return ResponseEntity.ok(meetingService.getLiveMeetings());
    }

    @GetMapping("/my")
    public ResponseEntity<List<MeetingResponse>> getMyMeetings() {
        return ResponseEntity.ok(meetingService.getMyMeetings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeetingResponse> getMeetingById(@PathVariable Long id) {
        return ResponseEntity.ok(meetingService.getMeetingById(id));
    }

    @GetMapping("/problem/{problemId}")
    public ResponseEntity<List<MeetingResponse>> getMeetingsByProblem(@PathVariable Long problemId) {
        return ResponseEntity.ok(meetingService.getMeetingsByProblem(problemId));
    }

    @PostMapping
    public ResponseEntity<MeetingResponse> createMeeting(@Valid @RequestBody MeetingCreateRequest request) {
        return ResponseEntity.ok(meetingService.createMeeting(request));
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<Map<String, String>> joinMeeting(@PathVariable Long id) {
        meetingService.joinMeeting(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Joined meeting");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/leave")
    public ResponseEntity<Map<String, String>> leaveMeeting(@PathVariable Long id) {
        meetingService.leaveMeeting(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Left meeting");
        return ResponseEntity.ok(response);
    }
}
