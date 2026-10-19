package com.communityfix.controller;

import com.communityfix.dto.AnnouncementResponse;
import com.communityfix.service.AnnouncementService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/announcements")
@CrossOrigin(origins = "http://localhost:5173")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping
    public ResponseEntity<List<AnnouncementResponse>> getAllAnnouncements() {
        return ResponseEntity.ok(announcementService.getAllAnnouncements());
    }

    @GetMapping("/pinned")
    public ResponseEntity<List<AnnouncementResponse>> getPinnedAnnouncements() {
        return ResponseEntity.ok(announcementService.getPinnedAnnouncements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnnouncementResponse> getAnnouncementById(@PathVariable Long id) {
        return ResponseEntity.ok(announcementService.getAnnouncementById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<AnnouncementResponse> createAnnouncement(@RequestBody Map<String, Object> request) {
        String title = (String) request.get("title");
        String content = (String) request.get("content");
        Boolean pinned = (Boolean) request.get("pinned");
        return ResponseEntity.ok(announcementService.createAnnouncement(title, content, pinned));
    }
}
