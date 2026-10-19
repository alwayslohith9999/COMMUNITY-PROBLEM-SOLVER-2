package com.communityfix.repository;

import com.communityfix.model.User;
import com.communityfix.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    long countByApprovedTrue();
    long countByApprovedFalse();

    @Query("SELECT u FROM User u WHERE u.approved = true AND u.role = 'RESIDENT' ORDER BY u.reputation DESC")
    List<User> findApprovedResidentsByReputationDesc();

    @Query("SELECT u FROM User u WHERE u.approved = false ORDER BY u.createdAt DESC")
    List<User> findPendingApprovals();

    List<User> findByApprovedTrueAndRoleNotOrderByReputationDesc(UserRole role);

    @Query("SELECT u FROM User u WHERE u.block = ?1 AND u.approved = true ORDER BY u.flatNumber")
    List<User> findByBlockAndApprovedTrueOrderByFlatNumber(String block);

    @Query("SELECT u FROM User u WHERE u.approved = true ORDER BY u.block, u.flatNumber")
    List<User> findAllApprovedOrderByBlockAndFlat();

    long countByRole(UserRole role);
}
