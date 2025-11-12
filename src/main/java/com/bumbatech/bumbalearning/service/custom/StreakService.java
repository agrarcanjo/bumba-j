package com.bumbatech.bumbalearning.service.custom;

import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.domain.UserProfile;
import com.bumbatech.bumbalearning.repository.UserProfileRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StreakService {

    private static final Logger log = LoggerFactory.getLogger(StreakService.class);

    private final UserProfileRepository userProfileRepository;

    public StreakService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public UserProfile updateStreak(User user) {
        UserProfile profile = userProfileRepository
            .findByUserId(user.getId())
            .orElseThrow(() -> new RuntimeException("UserProfile not found for user: " + user.getId()));

        LocalDate today = LocalDate.now();
        LocalDate lastActivityDate = profile.getLastActivityDate();

        if (lastActivityDate == null) {
            profile.setCurrentStreak(1);
            profile.setLastActivityDate(today);
            log.info("Started new streak for user {}: 1 day", user.getLogin());
        } else if (lastActivityDate.equals(today)) {
            log.debug("User {} already has activity today, streak unchanged: {} days", user.getLogin(), profile.getCurrentStreak());
        } else {
            long daysBetween = ChronoUnit.DAYS.between(lastActivityDate, today);

            if (daysBetween == 1) {
                int newStreak = (profile.getCurrentStreak() != null ? profile.getCurrentStreak() : 0) + 1;
                profile.setCurrentStreak(newStreak);
                profile.setLastActivityDate(today);
                log.info("User {} extended streak to {} days", user.getLogin(), newStreak);
            } else if (daysBetween > 1) {
                log.info(
                    "User {} broke streak of {} days (last activity: {})",
                    user.getLogin(),
                    profile.getCurrentStreak(),
                    lastActivityDate
                );
                profile.setCurrentStreak(1);
                profile.setLastActivityDate(today);
            }
        }

        return userProfileRepository.save(profile);
    }

    public boolean checkStreakBreak(User user) {
        UserProfile profile = userProfileRepository
            .findByUserId(user.getId())
            .orElseThrow(() -> new RuntimeException("UserProfile not found for user: " + user.getId()));

        LocalDate today = LocalDate.now();
        LocalDate lastActivityDate = profile.getLastActivityDate();

        if (lastActivityDate == null) {
            return false;
        }

        long daysBetween = ChronoUnit.DAYS.between(lastActivityDate, today);

        if (daysBetween > 1) {
            log.warn("Streak broken for user {}: {} days since last activity", user.getLogin(), daysBetween);
            profile.setCurrentStreak(0);
            userProfileRepository.save(profile);
            return true;
        }

        return false;
    }

    public int getCurrentStreak(User user) {
        UserProfile profile = userProfileRepository
            .findByUserId(user.getId())
            .orElseThrow(() -> new RuntimeException("UserProfile not found for user: " + user.getId()));

        Integer streak = profile.getCurrentStreak();
        return streak != null ? streak : 0;
    }

    public boolean isStreakActive(User user) {
        UserProfile profile = userProfileRepository
            .findByUserId(user.getId())
            .orElseThrow(() -> new RuntimeException("UserProfile not found for user: " + user.getId()));

        LocalDate today = LocalDate.now();
        LocalDate lastActivityDate = profile.getLastActivityDate();

        if (lastActivityDate == null) {
            return false;
        }

        long daysBetween = ChronoUnit.DAYS.between(lastActivityDate, today);
        return daysBetween <= 1;
    }
}
