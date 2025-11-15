package com.bumbatech.bumbalearning.web.rest.student;

import com.bumbatech.bumbalearning.domain.User;
import com.bumbatech.bumbalearning.domain.UserProfile;
import com.bumbatech.bumbalearning.repository.UserProfileRepository;
import com.bumbatech.bumbalearning.repository.UserRepository;
import com.bumbatech.bumbalearning.security.SecurityUtils;
import com.bumbatech.bumbalearning.service.dto.RankingDTO;
import com.bumbatech.bumbalearning.service.dto.RankingEntryDTO;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student")
public class StudentRankingResource {

    private static final Logger LOG = LoggerFactory.getLogger(StudentRankingResource.class);
    private static final int DEFAULT_RANKING_SIZE = 50;

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    public StudentRankingResource(UserRepository userRepository, UserProfileRepository userProfileRepository) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @GetMapping("/ranking")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<RankingDTO> getRanking(
        @RequestParam(required = false) String municipality,
        @RequestParam(defaultValue = "all") String period
    ) {
        LOG.debug("REST request to get student ranking - municipality: {}, period: {}", municipality, period);

        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new RuntimeException("User not authenticated"));

        User currentUser = userRepository.findOneByLogin(userLogin).orElseThrow(() -> new RuntimeException("User not found: " + userLogin));

        UserProfile currentUserProfile = userProfileRepository
            .findByUserId(currentUser.getId())
            .orElseThrow(() -> new RuntimeException("UserProfile not found for user: " + currentUser.getId()));

        String municipalityCode = municipality != null ? municipality : currentUserProfile.getMunicipalityCode();

        Instant periodStart = calculatePeriodStart(period);

        List<UserProfile> profiles;
        if (periodStart != null) {
            profiles = userProfileRepository.findTopUsersByMunicipalityAndPeriod(
                municipalityCode,
                periodStart,
                PageRequest.of(0, DEFAULT_RANKING_SIZE)
            );
        } else {
            profiles = userProfileRepository.findTopUsersByMunicipality(municipalityCode, PageRequest.of(0, DEFAULT_RANKING_SIZE));
        }

        RankingDTO rankingDTO = new RankingDTO();
        rankingDTO.setPeriod(period);
        rankingDTO.setMunicipalityCode(municipalityCode);

        List<RankingEntryDTO> rankings = new ArrayList<>();
        int rank = 1;
        Integer currentUserRank = null;

        for (UserProfile profile : profiles) {
            User user = profile.getUser();
            boolean isCurrentUser = user.getId().equals(currentUser.getId());

            RankingEntryDTO entry = new RankingEntryDTO(
                user.getId(),
                user.getFirstName() != null ? user.getFirstName() : user.getLogin(),
                profile.getTotalXp(),
                profile.getCurrentLevel(),
                profile.getCurrentStreak(),
                profile.getMunicipalityCode(),
                rank,
                isCurrentUser
            );

            rankings.add(entry);

            if (isCurrentUser) {
                currentUserRank = rank;
            }

            rank++;
        }

        rankingDTO.setRankings(rankings);
        rankingDTO.setCurrentUserRank(currentUserRank);

        Long totalUsers;
        if (periodStart != null) {
            totalUsers = userProfileRepository.countByMunicipalityAndPeriod(municipalityCode, periodStart);
        } else {
            totalUsers = userProfileRepository.countByMunicipality(municipalityCode);
        }
        rankingDTO.setTotalUsers(totalUsers.intValue());

        LOG.debug("Ranking retrieved: {} users, current user rank: {}", rankings.size(), currentUserRank);
        return ResponseEntity.ok(rankingDTO);
    }

    private Instant calculatePeriodStart(String period) {
        Instant now = Instant.now();
        return switch (period.toLowerCase()) {
            case "week" -> now.minus(7, ChronoUnit.DAYS);
            case "month" -> now.minus(30, ChronoUnit.DAYS);
            case "all" -> null;
            default -> null;
        };
    }
}
