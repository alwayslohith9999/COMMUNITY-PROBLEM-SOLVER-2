package com.communityfix.service;

import com.communityfix.dto.UserSummary;
import com.communityfix.model.User;
import com.communityfix.model.enums.UserRole;
import com.communityfix.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<UserSummary> getAllApprovedResidents() {
        return userRepository.findAllApprovedOrderByBlockAndFlat().stream()
                .map(this::toUserSummary)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserSummary> getResidentsByBlock(String block) {
        return userRepository.findByBlockAndApprovedTrueOrderByFlatNumber(block).stream()
                .map(this::toUserSummary)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserSummary> getLeaderboard() {
        return userRepository.findApprovedResidentsByReputationDesc().stream()
                .limit(10)
                .map(this::toUserSummary)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserSummary getUserProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return toUserSummary(user);
    }

    @Transactional(readOnly = true)
    public List<UserSummary> getPendingApprovals() {
        return userRepository.findPendingApprovals().stream()
                .map(this::toUserSummary)
                .collect(Collectors.toList());
    }

    @Transactional
    public void approveUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setApproved(true);
        userRepository.save(user);
    }

    @Transactional
    public void rejectUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional(readOnly = true)
    public long countTotalResidents() {
        return userRepository.countByApprovedTrue();
    }

    @Transactional(readOnly = true)
    public long countPendingApprovals() {
        return userRepository.countByApprovedFalse();
    }

    public UserSummary toUserSummary(User user) {
        return UserSummary.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .flatNumber(user.getFlatNumber())
                .block(user.getBlock())
                .phoneNumber(user.getPhoneNumber())
                .reputation(user.getReputation())
                .reputationRank(user.getReputationRank())
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
