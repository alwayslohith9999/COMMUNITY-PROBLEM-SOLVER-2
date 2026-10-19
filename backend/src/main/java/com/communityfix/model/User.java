package com.communityfix.model;

import com.communityfix.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private UserRole role = UserRole.RESIDENT;

    @Column(nullable = false)
    private String flatNumber;

    @Column(nullable = false)
    private String block;

    private String phoneNumber;

    private LocalDate moveInDate;

    @Builder.Default
    private Integer reputation = 0;

    @Builder.Default
    private Boolean approved = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "postedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Problem> problems = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Vote> votes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Notification> notifications = new ArrayList<>();

    @ManyToMany(mappedBy = "attendees")
    @Builder.Default
    private List<Meeting> meetings = new ArrayList<>();

    public String getReputationRank() {
        if (reputation >= 500) return "Community Legend";
        if (reputation >= 300) return "Problem Solver";
        if (reputation >= 200) return "Active Helper";
        if (reputation >= 150) return "Rising Star";
        if (reputation >= 100) return "Contributor";
        return "New Member";
    }
}
