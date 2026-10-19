package com.communityfix.service;

import com.communityfix.model.*;
import com.communityfix.model.enums.*;
import com.communityfix.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final CommentRepository commentRepository;
    private final MeetingRepository meetingRepository;
    private final NotificationRepository notificationRepository;
    private final AnnouncementRepository announcementRepository;
    private final VoteRepository voteRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, ProblemRepository problemRepository,
                      CommentRepository commentRepository, MeetingRepository meetingRepository,
                      NotificationRepository notificationRepository, AnnouncementRepository announcementRepository,
                      VoteRepository voteRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.problemRepository = problemRepository;
        this.commentRepository = commentRepository;
        this.meetingRepository = meetingRepository;
        this.notificationRepository = notificationRepository;
        this.announcementRepository = announcementRepository;
        this.voteRepository = voteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        List<User> users = seedUsers();
        List<Problem> problems = seedProblems(users);
        seedComments(users, problems);
        seedMeetings(users, problems);
        seedAnnouncements(users);
        seedNotifications(users, problems);
        seedVotes(users, problems);
    }

    private List<User> seedUsers() {
        String hashedPassword = passwordEncoder.encode("password123");
        List<User> users = new ArrayList<>();

        // Super Admin
        users.add(User.builder().fullName("Ramesh Sharma").email("admin@communityfix.com")
                .password(hashedPassword).role(UserRole.SUPER_ADMIN).flatNumber("OFFICE")
                .block("Admin").phoneNumber("9876543200").reputation(500).approved(true)
                .moveInDate(LocalDate.of(2020, 1, 1)).createdAt(LocalDateTime.now().minusMonths(6)).build());

        // Admins
        users.add(User.builder().fullName("Priya Nair").email("priya@communityfix.com")
                .password(hashedPassword).role(UserRole.ADMIN).flatNumber("A-001").block("A")
                .phoneNumber("9876543201").reputation(340).approved(true)
                .moveInDate(LocalDate.of(2020, 6, 1)).createdAt(LocalDateTime.now().minusMonths(5)).build());
        users.add(User.builder().fullName("Suresh Menon").email("suresh@communityfix.com")
                .password(hashedPassword).role(UserRole.ADMIN).flatNumber("B-001").block("B")
                .phoneNumber("9876543202").reputation(280).approved(true)
                .moveInDate(LocalDate.of(2021, 1, 1)).createdAt(LocalDateTime.now().minusMonths(5)).build());

        // Residents
        users.add(User.builder().fullName("Arjun Reddy").email("arjun@gmail.com")
                .password(hashedPassword).role(UserRole.RESIDENT).flatNumber("A-101").block("A")
                .phoneNumber("9876543210").reputation(210).approved(true)
                .moveInDate(LocalDate.of(2023, 1, 15)).createdAt(LocalDateTime.now().minusMonths(4)).build());
        users.add(User.builder().fullName("Kavitha Iyer").email("kavitha@gmail.com")
                .password(hashedPassword).role(UserRole.RESIDENT).flatNumber("A-203").block("A")
                .phoneNumber("9876543211").reputation(185).approved(true)
                .moveInDate(LocalDate.of(2022, 8, 20)).createdAt(LocalDateTime.now().minusMonths(3)).build());
        users.add(User.builder().fullName("Mohammed Farhan").email("farhan@gmail.com")
                .password(hashedPassword).role(UserRole.RESIDENT).flatNumber("B-102").block("B")
                .phoneNumber("9876543212").reputation(320).approved(true)
                .moveInDate(LocalDate.of(2021, 11, 5)).createdAt(LocalDateTime.now().minusMonths(4)).build());
        users.add(User.builder().fullName("Sneha Pillai").email("sneha@gmail.com")
                .password(hashedPassword).role(UserRole.RESIDENT).flatNumber("B-305").block("B")
                .phoneNumber("9876543213").reputation(95).approved(true)
                .moveInDate(LocalDate.of(2023, 6, 10)).createdAt(LocalDateTime.now().minusMonths(2)).build());
        users.add(User.builder().fullName("Vikram Joshi").email("vikram@gmail.com")
                .password(hashedPassword).role(UserRole.RESIDENT).flatNumber("C-201").block("C")
                .phoneNumber("9876543214").reputation(450).approved(true)
                .moveInDate(LocalDate.of(2020, 3, 22)).createdAt(LocalDateTime.now().minusMonths(5)).build());
        users.add(User.builder().fullName("Ananya Krishnan").email("ananya@gmail.com")
                .password(hashedPassword).role(UserRole.RESIDENT).flatNumber("C-404").block("C")
                .phoneNumber("9876543215").reputation(130).approved(true)
                .moveInDate(LocalDate.of(2022, 12, 1)).createdAt(LocalDateTime.now().minusMonths(3)).build());
        users.add(User.builder().fullName("Deepak Verma").email("deepak@gmail.com")
                .password(hashedPassword).role(UserRole.RESIDENT).flatNumber("D-103").block("D")
                .phoneNumber("9876543216").reputation(275).approved(true)
                .moveInDate(LocalDate.of(2021, 7, 18)).createdAt(LocalDateTime.now().minusMonths(4)).build());
        users.add(User.builder().fullName("Lakshmi Rao").email("lakshmi@gmail.com")
                .password(hashedPassword).role(UserRole.RESIDENT).flatNumber("D-202").block("D")
                .phoneNumber("9876543217").reputation(160).approved(true)
                .moveInDate(LocalDate.of(2023, 2, 28)).createdAt(LocalDateTime.now().minusMonths(2)).build());
        users.add(User.builder().fullName("Rahul Gupta").email("rahul@gmail.com")
                .password(hashedPassword).role(UserRole.RESIDENT).flatNumber("E-301").block("E")
                .phoneNumber("9876543218").reputation(390).approved(true)
                .moveInDate(LocalDate.of(2020, 9, 14)).createdAt(LocalDateTime.now().minusMonths(5)).build());
        users.add(User.builder().fullName("Meera Balachandran").email("meera@gmail.com")
                .password(hashedPassword).role(UserRole.RESIDENT).flatNumber("E-102").block("E")
                .phoneNumber("9876543219").reputation(220).approved(true)
                .moveInDate(LocalDate.of(2022, 5, 30)).createdAt(LocalDateTime.now().minusMonths(3)).build());
        users.add(User.builder().fullName("Sanjay Patil").email("sanjay@gmail.com")
                .password(hashedPassword).role(UserRole.RESIDENT).flatNumber("A-302").block("A")
                .phoneNumber("9876543220").reputation(75).approved(true)
                .moveInDate(LocalDate.of(2024, 1, 10)).createdAt(LocalDateTime.now().minusMonths(1)).build());
        users.add(User.builder().fullName("Divya Menon").email("divya@gmail.com")
                .password(hashedPassword).role(UserRole.RESIDENT).flatNumber("B-204").block("B")
                .phoneNumber("9876543221").reputation(110).approved(true)
                .moveInDate(LocalDate.of(2023, 9, 5)).createdAt(LocalDateTime.now().minusMonths(2)).build());
        users.add(User.builder().fullName("Kiran Kumar").email("kiran@gmail.com")
                .password(hashedPassword).role(UserRole.RESIDENT).flatNumber("C-103").block("C")
                .phoneNumber("9876543222").reputation(290).approved(true)
                .moveInDate(LocalDate.of(2021, 4, 12)).createdAt(LocalDateTime.now().minusMonths(4)).build());
        users.add(User.builder().fullName("Pooja Sharma").email("pooja@gmail.com")
                .password(hashedPassword).role(UserRole.RESIDENT).flatNumber("D-401").block("D")
                .phoneNumber("9876543223").reputation(55).approved(true)
                .moveInDate(LocalDate.of(2024, 3, 20)).createdAt(LocalDateTime.now().minusWeeks(2)).build());
        users.add(User.builder().fullName("Aditya Nambiar").email("aditya@gmail.com")
                .password(hashedPassword).role(UserRole.RESIDENT).flatNumber("E-203").block("E")
                .phoneNumber("9876543224").reputation(340).approved(true)
                .moveInDate(LocalDate.of(2021, 12, 8)).createdAt(LocalDateTime.now().minusMonths(4)).build());

        // Pending approvals
        users.add(User.builder().fullName("Ritu Agarwal").email("ritu@gmail.com")
                .password(hashedPassword).role(UserRole.RESIDENT).flatNumber("A-405").block("A")
                .phoneNumber("9876543225").reputation(0).approved(false)
                .moveInDate(LocalDate.of(2024, 5, 1)).createdAt(LocalDateTime.now().minusDays(2)).build());
        users.add(User.builder().fullName("Manoj Tiwari").email("manoj@gmail.com")
                .password(hashedPassword).role(UserRole.RESIDENT).flatNumber("C-302").block("C")
                .phoneNumber("9876543226").reputation(0).approved(false)
                .moveInDate(LocalDate.of(2024, 5, 10)).createdAt(LocalDateTime.now().minusDays(1)).build());
        users.add(User.builder().fullName("Fatima Sheikh").email("fatima@gmail.com")
                .password(hashedPassword).role(UserRole.RESIDENT).flatNumber("B-401").block("B")
                .phoneNumber("9876543227").reputation(0).approved(false)
                .moveInDate(LocalDate.of(2024, 5, 15)).createdAt(LocalDateTime.now().minusHours(5)).build());

        return userRepository.saveAll(users);
    }

    private List<Problem> seedProblems(List<User> users) {
        Map<String, User> u = new HashMap<>();
        users.forEach(user -> u.put(user.getEmail(), user));

        List<Problem> problems = new ArrayList<>();

        // Problem 1 - Critical Water Pump
        problems.add(Problem.builder().title("Main water pump not working — no water in Block A since morning")
                .description("The main water pump for Block A has completely stopped working since 6 AM today. All flats from A-101 to A-405 have zero water supply. We have contacted the pump room but no technician has arrived yet. This is severely affecting daily routines especially for families with children and elderly residents. Please treat this as highest priority.")
                .category(Category.PLUMBING).urgency(UrgencyLevel.CRITICAL).status(ProblemStatus.IN_PROGRESS)
                .block("A").flatNumber("A-101").postedBy(u.get("arjun@gmail.com")).assignedTo(u.get("suresh@communityfix.com"))
                .createdAt(LocalDateTime.now().minusHours(3)).updatedAt(LocalDateTime.now().minusHours(3)).build());

        // Problem 2 - Elevator
        problems.add(Problem.builder().title("Elevator in Block B stuck between floors — elderly resident trapped briefly")
                .description("The elevator in Block B got stuck between 2nd and 3rd floor this morning around 9 AM. Mrs. Sulochana from B-301 (aged 72) was inside for about 20 minutes before the doors were manually opened. The elevator is currently out of service. Multiple residents on upper floors including senior citizens are severely impacted. AMC company has been called but hasn't arrived. This elevator has had issues for the past 2 weeks.")
                .category(Category.LIFT).urgency(UrgencyLevel.CRITICAL).status(ProblemStatus.IN_PROGRESS)
                .block("B").flatNumber("B-102").postedBy(u.get("farhan@gmail.com")).assignedTo(u.get("suresh@communityfix.com"))
                .createdAt(LocalDateTime.now().minusHours(5)).updatedAt(LocalDateTime.now().minusHours(5)).build());

        // Problem 3 - Sewage
        problems.add(Problem.builder().title("Sewage overflow near C-Block entrance — foul smell and health hazard")
                .description("There is a major sewage overflow happening right at the entrance of C Block near the main gate. The water is stagnant and the smell is unbearable. Children play in this area every evening. This is a serious health hazard and could lead to disease. I have attached a photo. This needs immediate municipal corporation involvement.")
                .category(Category.STRUCTURAL).urgency(UrgencyLevel.CRITICAL).status(ProblemStatus.ESCALATED)
                .block("C").flatNumber("C-201").postedBy(u.get("vikram@gmail.com"))
                .createdAt(LocalDateTime.now().minusDays(1)).updatedAt(LocalDateTime.now().minusDays(1)).build());

        // Problem 4 - Street lights
        problems.add(Problem.builder().title("Street lights on the jogging track not working for 2 weeks")
                .description("All 6 street lights along the inner jogging track have been non-functional for the past 2 weeks. Evening walkers and joggers are walking in complete darkness after 7 PM. This is a safety concern especially for women and elderly residents who exercise in the evenings. I have reported this twice to the security desk but no action has been taken.")
                .category(Category.ELECTRICAL).urgency(UrgencyLevel.HIGH).status(ProblemStatus.OPEN)
                .block("E").flatNumber("E-301").postedBy(u.get("rahul@gmail.com"))
                .createdAt(LocalDateTime.now().minusDays(2)).updatedAt(LocalDateTime.now().minusDays(2)).build());

        // Problem 5 - Parking
        problems.add(Problem.builder().title("Parking slot B-47 being occupied by unauthorized vehicle daily")
                .description("My registered parking slot B-47 is being occupied every night by a white Swift Dzire (TN-09-AB-1234) which does not belong to any resident. I have complained to the security guard multiple times but the vehicle is back the next day. I have to park on the road putting my car at risk. I have photos of the vehicle as evidence.")
                .category(Category.PARKING).urgency(UrgencyLevel.HIGH).status(ProblemStatus.OPEN)
                .block("B").flatNumber("B-305").postedBy(u.get("sneha@gmail.com"))
                .createdAt(LocalDateTime.now().minusDays(4)).updatedAt(LocalDateTime.now().minusDays(4)).build());

        // Problem 6 - Noise
        problems.add(Problem.builder().title("Loud construction noise from flat D-204 every day 6 AM to 10 PM")
                .description("The resident in D-204 has been doing heavy renovation work with drilling and hammering every single day starting at 6 AM. This violates the community bylaw which allows construction only between 10 AM and 6 PM. My infant cannot sleep and my work-from-home calls are constantly disrupted. Multiple neighbors have also complained. Please issue a formal warning.")
                .category(Category.NOISE).urgency(UrgencyLevel.HIGH).status(ProblemStatus.OPEN)
                .block("D").flatNumber("D-202").postedBy(u.get("lakshmi@gmail.com"))
                .createdAt(LocalDateTime.now().minusDays(6)).updatedAt(LocalDateTime.now().minusDays(6)).build());

        // Problem 7 - CCTV
        problems.add(Problem.builder().title("CCTV cameras near Block A entrance are non-functional")
                .description("Both CCTV cameras at the Block A entrance gate have been non-functional since last week. The display at the security cabin shows a black screen. This is a serious security gap — there was a bike theft reported nearby last month. We should not be left with blind spots at the main entrance.")
                .category(Category.SECURITY).urgency(UrgencyLevel.HIGH).status(ProblemStatus.IN_PROGRESS)
                .block("A").flatNumber("A-203").postedBy(u.get("kavitha@gmail.com")).assignedTo(u.get("deepak@gmail.com"))
                .createdAt(LocalDateTime.now().minusDays(7)).updatedAt(LocalDateTime.now().minusDays(7)).build());

        // Problem 8 - Swimming pool
        problems.add(Problem.builder().title("Swimming pool tiles cracked and sharp edges are a safety risk")
                .description("Several tiles inside the swimming pool have cracked and are exposing sharp edges, particularly near the steps on the shallow end. A child slightly cut their foot last Sunday. The pool maintenance team seems to only do surface cleaning and is not addressing structural issues. I request the management to close the pool temporarily until the tiles are repaired.")
                .category(Category.COMMON_AREA).urgency(UrgencyLevel.MEDIUM).status(ProblemStatus.OPEN)
                .block("E").flatNumber("E-102").postedBy(u.get("meera@gmail.com"))
                .createdAt(LocalDateTime.now().minusDays(5)).updatedAt(LocalDateTime.now().minusDays(5)).build());

        // Problem 9 - Gym
        problems.add(Problem.builder().title("Gym equipment — treadmill 2 and cross trainer broken since 3 weeks")
                .description("Treadmill number 2 (the one near the window) makes a loud grinding noise and suddenly stops after 5 minutes. The cross trainer's resistance knob is completely broken and stuck at maximum resistance. Both machines have 'Out of Order' sticky notes but no repair has happened in 3 weeks. The gym has only 4 machines so this is a significant loss.")
                .category(Category.COMMON_AREA).urgency(UrgencyLevel.MEDIUM).status(ProblemStatus.IN_PROGRESS)
                .block("E").flatNumber("E-203").postedBy(u.get("aditya@gmail.com"))
                .createdAt(LocalDateTime.now().minusWeeks(3)).updatedAt(LocalDateTime.now().minusWeeks(3)).build());

        // Problem 10 - Water leakage
        problems.add(Problem.builder().title("Water leakage from C-301 bathroom affecting my ceiling in C-201")
                .description("There is continuous water seepage from the flat above me (C-301) which is staining my bedroom ceiling and causing paint to peel. I have spoken to the neighbor but they say their bathroom is fine. The leakage is most severe after morning bath time (7-9 AM). I request management to get a plumber to inspect both flats together.")
                .category(Category.PLUMBING).urgency(UrgencyLevel.MEDIUM).status(ProblemStatus.OPEN)
                .block("C").flatNumber("C-201").postedBy(u.get("vikram@gmail.com"))
                .createdAt(LocalDateTime.now().minusDays(10)).updatedAt(LocalDateTime.now().minusDays(10)).build());

        // Problem 11 - Visitor parking
        problems.add(Problem.builder().title("Visitor parking area used by residents for permanent parking")
                .description("At least 8 vehicles with resident stickers are permanently occupying the 12 visitor parking slots near the main gate. When my family visits, they have no place to park and are asked to park outside the gate on the main road. This defeats the purpose of visitor parking. A strict rotation or timed parking system should be implemented.")
                .category(Category.PARKING).urgency(UrgencyLevel.MEDIUM).status(ProblemStatus.OPEN)
                .block("C").flatNumber("C-103").postedBy(u.get("kiran@gmail.com"))
                .createdAt(LocalDateTime.now().minusWeeks(2)).updatedAt(LocalDateTime.now().minusWeeks(2)).build());

        // Problem 12 - Mosquito (RESOLVED)
        problems.add(Problem.builder().title("Mosquito breeding in stagnant water at terrace garden — Block D")
                .description("The flower pots on the Block D terrace garden have been collecting stagnant rainwater for weeks and are clearly breeding mosquitoes. I found larvae in at least 4 large pots. This is especially concerning with dengue season approaching. Fogging was done last month but the root cause of standing water is not fixed.")
                .category(Category.COMMON_AREA).urgency(UrgencyLevel.MEDIUM).status(ProblemStatus.RESOLVED)
                .block("D").flatNumber("D-103").postedBy(u.get("deepak@gmail.com")).resolvedBy(u.get("priya@communityfix.com"))
                .createdAt(LocalDateTime.now().minusWeeks(3)).updatedAt(LocalDateTime.now().minusDays(3))
                .resolvedAt(LocalDateTime.now().minusDays(3)).build());

        // Problem 13 - Speed breaker
        problems.add(Problem.builder().title("Request for speed breaker near children's play area")
                .description("Vehicles drive too fast on the internal road right next to the children's play area. A speed breaker or at minimum a 'Children Playing' sign is needed urgently. I have seen near misses on two occasions. Other gated communities in the area have speed breakers — why don't we?")
                .category(Category.STRUCTURAL).urgency(UrgencyLevel.LOW).status(ProblemStatus.OPEN)
                .block("A").flatNumber("A-302").postedBy(u.get("sanjay@gmail.com"))
                .createdAt(LocalDateTime.now().minusWeeks(2)).updatedAt(LocalDateTime.now().minusWeeks(2)).build());

        // Problem 14 - Benches
        problems.add(Problem.builder().title("Request to add benches near Block E garden")
                .description("The garden area near Block E has beautiful landscaping but absolutely no seating. Elderly residents who come for morning walks have nowhere to rest. Even 4-5 simple benches would make a huge difference. Other blocks have benches. Please consider adding seating before the monsoon garden work begins.")
                .category(Category.COMMON_AREA).urgency(UrgencyLevel.LOW).status(ProblemStatus.OPEN)
                .block("E").flatNumber("D-401").postedBy(u.get("pooja@gmail.com"))
                .createdAt(LocalDateTime.now().minusWeeks(3)).updatedAt(LocalDateTime.now().minusWeeks(3)).build());

        // Problem 15 - Flickering lights (RESOLVED)
        problems.add(Problem.builder().title("Corridor lights in Block A staircase are flickering badly")
                .description("The tube lights in the staircase corridor between floors 1 and 2 of Block A have been flickering on and off for a week. It is slightly unsettling at night. Probably a loose connection or a tube that needs replacement. Small fix but affects daily convenience.")
                .category(Category.ELECTRICAL).urgency(UrgencyLevel.LOW).status(ProblemStatus.RESOLVED)
                .block("A").flatNumber("A-101").postedBy(u.get("arjun@gmail.com")).resolvedBy(u.get("suresh@communityfix.com"))
                .createdAt(LocalDateTime.now().minusWeeks(2)).updatedAt(LocalDateTime.now().minusDays(5))
                .resolvedAt(LocalDateTime.now().minusDays(5)).build());

        // Problem 16 - Storm drain
        problems.add(Problem.builder().title("Blocked storm drain near Block B causing flooding during rain")
                .description("The storm water drain near Block B entrance is completely blocked with mud and debris. Every time it rains, the entire area floods making it impossible to walk. This has been the case for the past month.")
                .category(Category.PLUMBING).urgency(UrgencyLevel.HIGH).status(ProblemStatus.OPEN)
                .block("B").flatNumber("B-102").postedBy(u.get("farhan@gmail.com"))
                .createdAt(LocalDateTime.now().minusDays(3)).updatedAt(LocalDateTime.now().minusDays(3)).build());

        // Problem 17 - WiFi
        problems.add(Problem.builder().title("Society Wi-Fi in common areas not working")
                .description("The community Wi-Fi that is supposed to be available in the clubhouse, gym, and garden areas has not been working for a week. The router lights are blinking red. Several residents rely on this for work calls when working from common areas.")
                .category(Category.ELECTRICAL).urgency(UrgencyLevel.MEDIUM).status(ProblemStatus.IN_PROGRESS)
                .block("C").flatNumber("C-201").postedBy(u.get("vikram@gmail.com"))
                .createdAt(LocalDateTime.now().minusWeeks(1)).updatedAt(LocalDateTime.now().minusWeeks(1)).build());

        // Problem 18 - Repaint staircase
        problems.add(Problem.builder().title("Request to repaint Block C staircase walls — badly chipped")
                .description("The walls of Block C staircases are in terrible condition with paint peeling off and visible damp patches. It looks very shabby and gives a poor impression to visitors. A fresh coat of paint is urgently needed on all floors.")
                .category(Category.STRUCTURAL).urgency(UrgencyLevel.LOW).status(ProblemStatus.OPEN)
                .block("C").flatNumber("C-404").postedBy(u.get("ananya@gmail.com"))
                .createdAt(LocalDateTime.now().minusMonths(1)).updatedAt(LocalDateTime.now().minusMonths(1)).build());

        // Problem 19 - Dog waste
        problems.add(Problem.builder().title("Dog waste not being cleaned up in garden area")
                .description("Pet owners are not cleaning up after their dogs in the community garden. There are at least 3-4 spots every morning. This is unhygienic especially for children who play there. Please install pet waste stations and put up reminder signs.")
                .category(Category.COMMON_AREA).urgency(UrgencyLevel.MEDIUM).status(ProblemStatus.OPEN)
                .block("E").flatNumber("E-102").postedBy(u.get("meera@gmail.com"))
                .createdAt(LocalDateTime.now().minusDays(4)).updatedAt(LocalDateTime.now().minusDays(4)).build());

        // Problem 20 - Gate 2 (RESOLVED)
        problems.add(Problem.builder().title("Gate 2 remote access not working for Block D residents")
                .description("The RFID remote access for Gate 2 has stopped working for Block D residents. Our remotes don't open the gate and we have to wait for security to manually open it. This causes delays during morning rush hour.")
                .category(Category.SECURITY).urgency(UrgencyLevel.HIGH).status(ProblemStatus.RESOLVED)
                .block("D").flatNumber("D-103").postedBy(u.get("deepak@gmail.com")).resolvedBy(u.get("priya@communityfix.com"))
                .createdAt(LocalDateTime.now().minusWeeks(2)).updatedAt(LocalDateTime.now().minusDays(2))
                .resolvedAt(LocalDateTime.now().minusDays(2)).build());

        // Problem 21 - Fused bulb
        problems.add(Problem.builder().title("Fused bulb in B-Block parking basement for 2 months")
                .description("One of the main lights in B-Block basement parking has been fused for nearly 2 months. It creates a dark spot near pillar P-12 which is a safety concern. Several residents have reported this but no replacement has been done.")
                .category(Category.ELECTRICAL).urgency(UrgencyLevel.LOW).status(ProblemStatus.OPEN)
                .block("B").flatNumber("B-305").postedBy(u.get("sneha@gmail.com"))
                .createdAt(LocalDateTime.now().minusMonths(2)).updatedAt(LocalDateTime.now().minusMonths(2)).build());

        // Problem 22 - Dustbins
        problems.add(Problem.builder().title("Request for dustbins at each floor landing")
                .description("Currently there are no dustbins at floor landings in any block. Residents have to walk down to the ground floor to dispose of even small items. Installing covered dustbins at each floor would greatly improve convenience.")
                .category(Category.COMMON_AREA).urgency(UrgencyLevel.LOW).status(ProblemStatus.OPEN)
                .block("C").flatNumber("C-103").postedBy(u.get("kiran@gmail.com"))
                .createdAt(LocalDateTime.now().minusWeeks(3)).updatedAt(LocalDateTime.now().minusWeeks(3)).build());

        // Problem 23 - Lift vandalism
        problems.add(Problem.builder().title("Kids vandalizing lift buttons in Block E")
                .description("Children from nearby buildings are coming into Block E and deliberately pressing all floor buttons and sometimes spitting on the lift panels. The buttons for floors 3 and 5 are already damaged. Request security to not allow unaccompanied children from other blocks.")
                .category(Category.SECURITY).urgency(UrgencyLevel.MEDIUM).status(ProblemStatus.OPEN)
                .block("E").flatNumber("E-203").postedBy(u.get("aditya@gmail.com"))
                .createdAt(LocalDateTime.now().minusWeeks(1)).updatedAt(LocalDateTime.now().minusWeeks(1)).build());

        // Problem 24 - Leaking tank
        problems.add(Problem.builder().title("Leaking overhead tank — water wastage continuously")
                .description("The overhead water tank on Block C terrace has a visible leak with water continuously dripping from the overflow pipe. This has been going on for a week resulting in hundreds of liters of water wasted daily. Please fix the float valve immediately.")
                .category(Category.PLUMBING).urgency(UrgencyLevel.HIGH).status(ProblemStatus.IN_PROGRESS)
                .block("C").flatNumber("E-301").postedBy(u.get("rahul@gmail.com"))
                .createdAt(LocalDateTime.now().minusDays(6)).updatedAt(LocalDateTime.now().minusDays(6)).build());

        // Problem 25 - Broken swing (RESOLVED)
        problems.add(Problem.builder().title("Broken swing in children's play area")
                .description("The main swing in the children's play area has a broken chain on one side. It is dangerous and could cause injury. Please remove it immediately or repair it before any child gets hurt.")
                .category(Category.COMMON_AREA).urgency(UrgencyLevel.HIGH).status(ProblemStatus.RESOLVED)
                .block("A").flatNumber("A-203").postedBy(u.get("kavitha@gmail.com")).resolvedBy(u.get("suresh@communityfix.com"))
                .createdAt(LocalDateTime.now().minusWeeks(3)).updatedAt(LocalDateTime.now().minusDays(2))
                .resolvedAt(LocalDateTime.now().minusDays(2)).build());

        return problemRepository.saveAll(problems);
    }

    private void seedComments(List<User> users, List<Problem> problems) {
        Map<String, User> u = new HashMap<>();
        users.forEach(user -> u.put(user.getEmail(), user));

        List<Comment> comments = new ArrayList<>();

        // Comments for Problem 1 (Water pump)
        comments.add(Comment.builder().content("We are facing the same issue in A-203. No water since 6 AM. Please fix ASAP.").upvoteCount(8).problem(problems.get(0)).user(u.get("kavitha@gmail.com")).createdAt(LocalDateTime.now().minusHours(3)).build());
        comments.add(Comment.builder().content("I know a reliable plumber — Ramakrishna Pumps on Outer Ring Road. They fixed our pump last year in 2 hours. Shall I share the contact?").upvoteCount(12).isVolunteerOffer(true).problem(problems.get(0)).user(u.get("vikram@gmail.com")).createdAt(LocalDateTime.now().minusHours(2).minusMinutes(30)).build());
        comments.add(Comment.builder().content("We have contacted the AMC team. Technician is expected by 11 AM. Apologies for the inconvenience.").upvoteCount(15).problem(problems.get(0)).user(u.get("priya@communityfix.com")).createdAt(LocalDateTime.now().minusHours(2)).build());
        comments.add(Comment.builder().content("It is 12:30 PM and no technician yet. What is happening?").upvoteCount(5).problem(problems.get(0)).user(u.get("sanjay@gmail.com")).createdAt(LocalDateTime.now().minusMinutes(30)).build());
        comments.add(Comment.builder().content("Still no water. The AMC person just called and said they are 30 minutes away.").upvoteCount(3).problem(problems.get(0)).user(u.get("arjun@gmail.com")).createdAt(LocalDateTime.now().minusMinutes(15)).build());
        Comment solution1 = Comment.builder().content("Status updated to In Progress. Technician is on site now.").upvoteCount(9).isSolution(true).problem(problems.get(0)).user(u.get("admin@communityfix.com")).createdAt(LocalDateTime.now().minusMinutes(10)).build();
        comments.add(solution1);

        // Comments for Problem 2 (Elevator)
        comments.add(Comment.builder().content("My mother-in-law lives on 4th floor and cannot take stairs. This is a major emergency.").upvoteCount(7).problem(problems.get(1)).user(u.get("lakshmi@gmail.com")).createdAt(LocalDateTime.now().minusHours(4)).build());
        comments.add(Comment.builder().content("I am volunteering to coordinate with the elevator company. I have their direct engineer contact.").upvoteCount(11).isVolunteerOffer(true).problem(problems.get(1)).user(u.get("deepak@gmail.com")).createdAt(LocalDateTime.now().minusHours(4)).build());
        Comment solution2 = Comment.builder().content("I checked — the elevator door sensor is the issue. The AMC company has been informed.").upvoteCount(8).isSolution(true).problem(problems.get(1)).user(u.get("farhan@gmail.com")).createdAt(LocalDateTime.now().minusHours(3)).build();
        comments.add(solution2);
        comments.add(Comment.builder().content("Is there any timeline for repair? My grandmother is stuck on 5th floor.").upvoteCount(4).problem(problems.get(1)).user(u.get("sneha@gmail.com")).createdAt(LocalDateTime.now().minusHours(2)).build());
        comments.add(Comment.builder().content("Repair estimated to be completed by 5 PM today. We will update.").upvoteCount(10).problem(problems.get(1)).user(u.get("suresh@communityfix.com")).createdAt(LocalDateTime.now().minusHours(1)).build());

        // Comments for Problem 3 (Sewage)
        comments.add(Comment.builder().content("The smell is reaching all the way to C-301. This is disgusting.").upvoteCount(9).problem(problems.get(2)).user(u.get("meera@gmail.com")).createdAt(LocalDateTime.now().minusDays(1).minusHours(2)).build());
        comments.add(Comment.builder().content("I have photos from this morning. The overflow is getting worse after the rain.").upvoteCount(6).problem(problems.get(2)).user(u.get("kiran@gmail.com")).createdAt(LocalDateTime.now().minusDays(1).minusHours(1)).build());
        Comment solution3 = Comment.builder().content("We need to escalate this to BBMP/GHMC immediately. This is a public health issue.").upvoteCount(14).isSolution(true).problem(problems.get(2)).user(u.get("vikram@gmail.com")).createdAt(LocalDateTime.now().minusHours(20)).build();
        comments.add(solution3);
        comments.add(Comment.builder().content("Escalated to municipal corporation. Reference number: GHMC-2024-8834.").upvoteCount(12).problem(problems.get(2)).user(u.get("priya@communityfix.com")).createdAt(LocalDateTime.now().minusHours(18)).build());

        // Comments for Problem 6 (Noise)
        comments.add(Comment.builder().content("I can hear this drilling from Block C. The sound travels through the walls.").upvoteCount(5).problem(problems.get(5)).user(u.get("ananya@gmail.com")).createdAt(LocalDateTime.now().minusDays(5)).build());
        Comment solution6 = Comment.builder().content("Community bylaws clearly state 10 AM to 6 PM only. Issue a formal notice.").upvoteCount(8).isSolution(true).problem(problems.get(5)).user(u.get("rahul@gmail.com")).createdAt(LocalDateTime.now().minusDays(5)).build();
        comments.add(solution6);
        comments.add(Comment.builder().content("Formal notice issued to flat D-204. They have been warned of fine on next violation.").upvoteCount(11).problem(problems.get(5)).user(u.get("priya@communityfix.com")).createdAt(LocalDateTime.now().minusDays(4)).build());

        // Additional comments for other problems
        comments.add(Comment.builder().content("I noticed the same issue. The lights go off one by one every evening.").upvoteCount(3).problem(problems.get(3)).user(u.get("meera@gmail.com")).createdAt(LocalDateTime.now().minusDays(1)).build());
        comments.add(Comment.builder().content("The wiring probably needs replacement. This is a safety hazard too.").upvoteCount(5).problem(problems.get(3)).user(u.get("aditya@gmail.com")).createdAt(LocalDateTime.now().minusDays(1)).build());
        comments.add(Comment.builder().content("I volunteer to check the electrical panel if someone can assist.").upvoteCount(4).isVolunteerOffer(true).problem(problems.get(3)).user(u.get("kiran@gmail.com")).createdAt(LocalDateTime.now().minusDays(2)).build());

        comments.add(Comment.builder().content("This happened to me last week too. Different vehicle though.").upvoteCount(3).problem(problems.get(4)).user(u.get("divya@gmail.com")).createdAt(LocalDateTime.now().minusDays(3)).build());
        comments.add(Comment.builder().content("Security should note down the vehicle number and issue a warning.").upvoteCount(6).problem(problems.get(4)).user(u.get("farhan@gmail.com")).createdAt(LocalDateTime.now().minusDays(3)).build());

        commentRepository.saveAll(comments);
    }

    private void seedMeetings(List<User> users, List<Problem> problems) {
        Map<String, User> u = new HashMap<>();
        users.forEach(user -> u.put(user.getEmail(), user));

        List<Meeting> meetings = new ArrayList<>();

        meetings.add(Meeting.builder().title("Emergency Water Crisis Discussion — Block A")
                .description("Urgent meeting to discuss the water pump failure and coordinate with AMC team.")
                .scheduledAt(LocalDateTime.now().withHour(19).withMinute(0))
                .jitsiRoom("communityfix-water-crisis-blocka")
                .status(MeetingStatus.SCHEDULED).host(u.get("admin@communityfix.com"))
                .problem(problems.get(0)).notifyAll(true)
                .attendees(Arrays.asList(u.get("arjun@gmail.com"), u.get("kavitha@gmail.com"), u.get("sanjay@gmail.com"), u.get("priya@communityfix.com")))
                .build());

        meetings.add(Meeting.builder().title("Elevator Maintenance Review Meeting")
                .description("Review the elevator repair status and discuss AMC contract terms.")
                .scheduledAt(LocalDateTime.now().plusDays(1).withHour(18).withMinute(30))
                .jitsiRoom("communityfix-elevator-review")
                .status(MeetingStatus.SCHEDULED).host(u.get("suresh@communityfix.com"))
                .problem(problems.get(1)).notifyAll(false)
                .attendees(Arrays.asList(u.get("farhan@gmail.com"), u.get("sneha@gmail.com"), u.get("lakshmi@gmail.com"), u.get("deepak@gmail.com")))
                .build());

        meetings.add(Meeting.builder().title("Security & CCTV Infrastructure Upgrade Discussion")
                .description("Plan for CCTV camera replacement and security infrastructure improvements.")
                .scheduledAt(LocalDateTime.now().plusDays(3).withHour(20).withMinute(0))
                .jitsiRoom("communityfix-security-upgrade")
                .status(MeetingStatus.SCHEDULED).host(u.get("priya@communityfix.com"))
                .problem(problems.get(6)).notifyAll(false)
                .attendees(Arrays.asList(u.get("kavitha@gmail.com"), u.get("rahul@gmail.com"), u.get("deepak@gmail.com"), u.get("vikram@gmail.com"), u.get("aditya@gmail.com")))
                .build());

        meetings.add(Meeting.builder().title("Monthly Community General Body Meeting — May 2024")
                .description("Monthly GBM to discuss community matters, budget, and upcoming events.")
                .scheduledAt(LocalDateTime.now().plusDays(7).withHour(18).withMinute(0))
                .jitsiRoom("communityfix-gbm-may2024")
                .status(MeetingStatus.SCHEDULED).host(u.get("admin@communityfix.com"))
                .notifyAll(true)
                .attendees(users.stream().filter(user -> user.getRole() == UserRole.RESIDENT && Boolean.TRUE.equals(user.getApproved())).limit(15).toList())
                .build());

        meetings.add(Meeting.builder().title("Parking Policy Review — Visitor Slots Issue")
                .description("Discuss and finalize new parking policy for visitor slots.")
                .scheduledAt(LocalDateTime.now().minusDays(5).withHour(19).withMinute(0))
                .jitsiRoom("communityfix-parking-policy")
                .status(MeetingStatus.COMPLETED).host(u.get("priya@communityfix.com"))
                .problem(problems.get(10)).notifyAll(false)
                .attendees(Arrays.asList(u.get("kiran@gmail.com"), u.get("sanjay@gmail.com"), u.get("deepak@gmail.com"), u.get("sneha@gmail.com")))
                .build());

        meetings.add(Meeting.builder().title("Swimming Pool Safety & Renovation Planning")
                .description("Discuss pool tile repair and safety measures.")
                .scheduledAt(LocalDateTime.now().minusDays(2).withHour(20).withMinute(0))
                .jitsiRoom("communityfix-pool-safety")
                .status(MeetingStatus.COMPLETED).host(u.get("suresh@communityfix.com"))
                .problem(problems.get(7)).notifyAll(false)
                .attendees(Arrays.asList(u.get("meera@gmail.com"), u.get("aditya@gmail.com"), u.get("ananya@gmail.com"), u.get("vikram@gmail.com")))
                .build());

        meetingRepository.saveAll(meetings);
    }

    private void seedAnnouncements(List<User> users) {
        Map<String, User> u = new HashMap<>();
        users.forEach(user -> u.put(user.getEmail(), user));

        List<Announcement> announcements = new ArrayList<>();

        announcements.add(Announcement.builder().title("Maintenance charge payment due by 31st May 2024")
                .content("Dear residents, the quarterly maintenance charges of Rs. 3,500 per flat are due by 31st May 2024. Please pay via NEFT to the society account (details in circular) or hand cash to the admin office (Block A Ground Floor) between 10 AM and 12 PM on weekdays. Late payment attracts a penalty of Rs. 200 per month. Please cooperate.")
                .pinned(true).thumbsUpCount(12).postedBy(u.get("admin@communityfix.com")).createdAt(LocalDateTime.now().minusDays(5)).build());

        announcements.add(Announcement.builder().title("Water supply shutdown — Saturday 25th May, 9 AM to 2 PM")
                .content("BWSSB/HMWSSB will be carrying out pipeline maintenance work on Saturday 25th May. There will be no water supply to the entire complex between 9 AM and 2 PM. Please store sufficient water in advance. The water tanker will be available near the main gate from 10 AM to 12 PM free of charge for emergency needs.")
                .pinned(true).thumbsUpCount(28).questionCount(5).postedBy(u.get("priya@communityfix.com")).createdAt(LocalDateTime.now().minusDays(2)).build());

        announcements.add(Announcement.builder().title("New security guard shift timings from June 1st")
                .content("From June 1st 2024, all entry gates will operate with 3 security guards per shift instead of 2. Night shift timing is changed to 10 PM to 6 AM. All visitors must now show ID proof and sign the visitor register. Delivery persons must wait at Gate 1 only.")
                .pinned(false).thumbsUpCount(19).postedBy(u.get("suresh@communityfix.com")).createdAt(LocalDateTime.now().minusWeeks(1)).build());

        announcements.add(Announcement.builder().title("Summer fest — Community Gathering on June 2nd")
                .content("We are organizing a summer fest for all residents on Sunday June 2nd at 5 PM in the common lawn area. There will be games for kids, a potluck dinner, and a cultural program. All residents are requested to participate and bring one dish to share. Register with the admin office by May 30th.")
                .pinned(false).thumbsUpCount(35).postedBy(u.get("admin@communityfix.com")).createdAt(LocalDateTime.now().minusDays(10)).build());

        announcements.add(Announcement.builder().title("Pest control scheduled for all common areas — 28th May")
                .content("Professional pest control treatment will be carried out on 28th May (Tuesday) starting 8 AM. All common areas including gym, clubhouse, garden, basement parking, and staircases will be treated. Please keep your flat doors closed during treatment. Pets should be kept indoors. Treatment will be complete by 1 PM.")
                .pinned(false).thumbsUpCount(22).checkCount(3).postedBy(u.get("priya@communityfix.com")).createdAt(LocalDateTime.now().minusDays(3)).build());

        announcements.add(Announcement.builder().title("New community app launched — CommunityFix!")
                .content("We are excited to announce the official launch of CommunityFix — our community problem-solving platform. Going forward, all maintenance complaints, suggestions, and meeting schedules will be managed here. Please register using your flat number and block. The old WhatsApp group will continue for casual conversations but official issues must be raised here only.")
                .pinned(false).thumbsUpCount(41).postedBy(u.get("admin@communityfix.com")).createdAt(LocalDateTime.now().minusMonths(1)).build());

        announcements.add(Announcement.builder().title("Rules reminder — No vehicles in pedestrian walkway")
                .content("It has been noticed that several two-wheelers are being ridden and parked in the pedestrian walkway between Block C and the garden. This is strictly prohibited and poses a risk to children and elderly residents. Violating vehicles will be clamped from next week onwards.")
                .pinned(false).thumbsUpCount(17).postedBy(u.get("suresh@communityfix.com")).createdAt(LocalDateTime.now().minusWeeks(2)).build());

        announcements.add(Announcement.builder().title("Congratulations — Our community won 'Best Maintained Complex' award!")
                .content("We are thrilled to announce that Greenview Enclave has won the 'Best Maintained Residential Complex' award by the Apartment Owners Association of the city for 2023-24. This is a testament to all our residents' cooperation and pride in maintaining our community. A formal felicitation will be done at the June 2nd Summer Fest.")
                .pinned(false).thumbsUpCount(52).postedBy(u.get("admin@communityfix.com")).createdAt(LocalDateTime.now().minusWeeks(3)).build());

        announcementRepository.saveAll(announcements);
    }

    private void seedNotifications(List<User> users, List<Problem> problems) {
        Map<String, User> u = new HashMap<>();
        users.forEach(user -> u.put(user.getEmail(), user));

        List<Notification> notifications = new ArrayList<>();

        // Arjun's notifications
        notifications.add(Notification.builder().message("Vikram Joshi volunteered to help with your water pump problem").type("VOLUNTEER").link("/problems/" + problems.get(0).getId()).user(u.get("arjun@gmail.com")).problem(problems.get(0)).isRead(false).createdAt(LocalDateTime.now().minusHours(2)).build());
        notifications.add(Notification.builder().message("Priya Nair commented on your problem: Water Pump Issue").type("COMMENT").link("/problems/" + problems.get(0).getId()).user(u.get("arjun@gmail.com")).problem(problems.get(0)).isRead(false).createdAt(LocalDateTime.now().minusHours(2)).build());
        notifications.add(Notification.builder().message("Your problem has been marked as In Progress by admin").type("STATUS").link("/problems/" + problems.get(0).getId()).user(u.get("arjun@gmail.com")).problem(problems.get(0)).isRead(false).createdAt(LocalDateTime.now().minusHours(1)).build());
        notifications.add(Notification.builder().message("Meeting scheduled: Emergency Water Crisis Discussion — Today at 7 PM").type("MEETING").link("/meetings").user(u.get("arjun@gmail.com")).isRead(false).createdAt(LocalDateTime.now().minusMinutes(45)).build());
        notifications.add(Notification.builder().message("New announcement: Water supply shutdown on Saturday").type("ANNOUNCEMENT").link("/announcements").user(u.get("arjun@gmail.com")).isRead(false).createdAt(LocalDateTime.now().minusDays(2)).build());
        notifications.add(Notification.builder().message("Kavitha Iyer upvoted your problem").type("UPVOTE").link("/problems/" + problems.get(0).getId()).user(u.get("arjun@gmail.com")).problem(problems.get(0)).isRead(true).createdAt(LocalDateTime.now().minusHours(3)).build());
        notifications.add(Notification.builder().message("New announcement: Maintenance charges due by 31st May").type("ANNOUNCEMENT").link("/announcements").user(u.get("arjun@gmail.com")).isRead(true).createdAt(LocalDateTime.now().minusDays(5)).build());

        notificationRepository.saveAll(notifications);
    }

    private void seedVotes(List<User> users, List<Problem> problems) {
        List<Vote> votes = new ArrayList<>();
        List<User> residents = users.stream().filter(u -> u.getRole() == UserRole.RESIDENT).toList();

        for (Problem problem : problems) {
            int upvoteCount = problem.getUpvoteCount();
            int count = 0;
            for (User user : residents) {
                if (count >= upvoteCount + 5) break;
                if (!user.getId().equals(problem.getPostedBy().getId())) {
                    votes.add(Vote.builder().user(user).problem(problem).isUpvote(true).createdAt(problem.getCreatedAt().plusHours(1)).build());
                    count++;
                }
            }
        }

        voteRepository.saveAll(votes);
    }
}
