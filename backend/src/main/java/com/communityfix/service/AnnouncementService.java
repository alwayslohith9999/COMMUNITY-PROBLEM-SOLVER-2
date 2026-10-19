package com.communityfix.service;

import com.communityfix.dto.AnnouncementResponse;
import com.communityfix.dto.UserSummary;
import com.communityfix.model.Announcement;
import com.communityfix.model.User;
import com.communityfix.repository.AnnouncementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final AuthService authService;

    public AnnouncementService(AnnouncementRepository announcementRepository, AuthService authService) {
        this.announcementRepository = announcementRepository;
        this.authService = authService;
    }

    @Transactional(readOnly = true)
    public List<AnnouncementResponse> getAllAnnouncements() {
        return announcementRepository.findAllOrderByPinnedAndDate().stream()
                .map(this::toAnnouncementResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AnnouncementResponse> getPinnedAnnouncements() {
        return announcementRepository.findByPinnedTrueOrderByCreatedAtDesc().stream()
                .map(this::toAnnouncementResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AnnouncementResponse getAnnouncementById(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found"));
        return toAnnouncementResponse(announcement);
    }

    @Transactional
    public AnnouncementResponse createAnnouncement(String title, String content, Boolean pinned) {
        User currentUser = authService.getCurrentUser();

        Announcement announcement = Announcement.builder()
                .title(title)
                .content(content)
                .pinned(pinned != null ? pinned : false)
                .postedBy(currentUser)
                .build();

        return toAnnouncementResponse(announcementRepository.save(announcement));
    }

    private AnnouncementResponse toAnnouncementResponse(Announcement announcement) {
        return AnnouncementResponse.builder()
                .id(announcement.getId())
                .title(announcement.getTitle())
                .content(announcement.getContent())
                .pinned(announcement.getPinned())
                .thumbsUpCount(announcement.getThumbsUpCount())
                .questionCount(announcement.getQuestionCount())
                .checkCount(announcement.getCheckCount())
                .createdAt(announcement.getCreatedAt())
                .postedBy(toUserSummary(announcement.getPostedBy()))
                .build();
    }

    private UserSummary toUserSummary(User user) {
        return UserSummary.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .flatNumber(user.getFlatNumber())
                .block(user.getBlock())
                .reputation(user.getReputation())
                .avatarInitials(getInitials(user.getFullName()))
                .build();
    }

    private String getInitials(String name) {
        if (name == null || name.isEmpty()) return "?";
        String[] parts = name.split(" ");
        if (parts.length >= 2) {
            return (parts[0].charAt(0) + "" + parts[parts.length - 1].charAt(0)).toUpperCase();
        }
        return name.substring(0, Math.min(2, name.length())).toUpperCase();
    }
}
