package com.bumbatech.bumbalearning.web.rest.student;

import com.bumbatech.bumbalearning.domain.Achievement;
import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.domain.UserAchievement;
import com.bumbatech.bumbalearning.domain.UserProfile;
import com.bumbatech.bumbalearning.repository.AchievementRepository;
import com.bumbatech.bumbalearning.repository.StudentLessonProgressRepository;
import com.bumbatech.bumbalearning.repository.UserAchievementRepository;
import com.bumbatech.bumbalearning.repository.UserProfileRepository;
import com.bumbatech.bumbalearning.repository.UserRepository;
import com.bumbatech.bumbalearning.security.SecurityUtils;
import com.bumbatech.bumbalearning.service.custom.StreakService;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student")
public class StudentAchievementResource {

    private static final Logger LOG = LoggerFactory.getLogger(StudentAchievementResource.class);

    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final UserProfileRepository userProfileRepository;
    private final StudentLessonProgressRepository studentLessonProgressRepository;
    private final StreakService streakService;

    public StudentAchievementResource(
        UserRepository userRepository,
        AchievementRepository achievementRepository,
        UserAchievementRepository userAchievementRepository,
        UserProfileRepository userProfileRepository,
        StudentLessonProgressRepository studentLessonProgressRepository,
        StreakService streakService
    ) {
        this.userRepository = userRepository;
        this.achievementRepository = achievementRepository;
        this.userAchievementRepository = userAchievementRepository;
        this.userProfileRepository = userProfileRepository;
        this.studentLessonProgressRepository = studentLessonProgressRepository;
        this.streakService = streakService;
    }

    @GetMapping("/achievements")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<List<StudentAchievementDTO>> getStudentAchievements() {
        LOG.debug("REST request to get student achievements");

        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new RuntimeException("User not authenticated"));

        User user = userRepository.findOneByLogin(userLogin).orElseThrow(() -> new RuntimeException("User not found: " + userLogin));

        List<Achievement> allAchievements = achievementRepository.findAll();

        List<UserAchievement> userAchievements = userAchievementRepository.findByUserId(user.getId());

        UserProfile profile = userProfileRepository
            .findByUserId(user.getId())
            .orElseThrow(() -> new RuntimeException("UserProfile not found for user: " + user.getId()));

        Long completedLessons = studentLessonProgressRepository.countCompletedLessonsByUserId(user.getId());
        Integer totalXp = profile.getTotalXp() != null ? profile.getTotalXp() : 0;
        Integer currentStreak = streakService.getCurrentStreak(user);

        List<StudentAchievementDTO> result = new ArrayList<>();

        for (Achievement achievement : allAchievements) {
            StudentAchievementDTO dto = new StudentAchievementDTO();
            dto.setId(achievement.getId());
            dto.setCode(achievement.getCode());
            dto.setName(achievement.getName());
            dto.setDescription(achievement.getDescription());
            dto.setIconUrl(achievement.getIconUrl());

            UserAchievement userAchievement = userAchievements
                .stream()
                .filter(ua -> ua.getAchievement().getId().equals(achievement.getId()))
                .findFirst()
                .orElse(null);

            if (userAchievement != null) {
                dto.setUnlocked(true);
                dto.setUnlockedAt(userAchievement.getUnlockedAt());
            } else {
                dto.setUnlocked(false);
            }

            ProgressInfo progress = calculateProgress(achievement.getCode(), completedLessons, totalXp, currentStreak);
            dto.setCurrent(progress.current);
            dto.setTarget(progress.target);

            result.add(dto);
        }

        LOG.debug("Retrieved {} achievements for user: {}", result.size(), userLogin);
        return ResponseEntity.ok(result);
    }

    private ProgressInfo calculateProgress(String code, Long completedLessons, Integer totalXp, Integer currentStreak) {
        ProgressInfo info = new ProgressInfo();

        switch (code) {
            case "FIRST_LESSON":
                info.current = completedLessons.intValue();
                info.target = 1;
                break;
            case "LESSON_MASTER_10":
                info.current = completedLessons.intValue();
                info.target = 10;
                break;
            case "LESSON_MASTER_50":
                info.current = completedLessons.intValue();
                info.target = 50;
                break;
            case "LESSON_MASTER_100":
                info.current = completedLessons.intValue();
                info.target = 100;
                break;
            case "XP_COLLECTOR_100":
                info.current = totalXp;
                info.target = 100;
                break;
            case "XP_COLLECTOR_1000":
                info.current = totalXp;
                info.target = 1000;
                break;
            case "XP_COLLECTOR_5000":
                info.current = totalXp;
                info.target = 5000;
                break;
            case "XP_COLLECTOR_10000":
                info.current = totalXp;
                info.target = 10000;
                break;
            case "STREAK_WARRIOR_7":
                info.current = currentStreak;
                info.target = 7;
                break;
            case "STREAK_WARRIOR_30":
                info.current = currentStreak;
                info.target = 30;
                break;
            case "STREAK_WARRIOR_100":
                info.current = currentStreak;
                info.target = 100;
                break;
            default:
                info.current = 0;
                info.target = 1;
        }

        return info;
    }

    private static class ProgressInfo {

        int current;
        int target;
    }

    public static class StudentAchievementDTO {

        private Long id;
        private String code;
        private String name;
        private String description;
        private String iconUrl;
        private Boolean unlocked;
        private java.time.Instant unlockedAt;
        private Integer current;
        private Integer target;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public Boolean getUnlocked() {
            return unlocked;
        }

        public void setUnlocked(Boolean unlocked) {
            this.unlocked = unlocked;
        }

        public java.time.Instant getUnlockedAt() {
            return unlockedAt;
        }

        public void setUnlockedAt(java.time.Instant unlockedAt) {
            this.unlockedAt = unlockedAt;
        }

        public Integer getCurrent() {
            return current;
        }

        public void setCurrent(Integer current) {
            this.current = current;
        }

        public Integer getTarget() {
            return target;
        }

        public void setTarget(Integer target) {
            this.target = target;
        }
    }
}
