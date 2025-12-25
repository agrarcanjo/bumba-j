package com.bumbatech.bumbalearning.service.dto;

import com.bumbatech.bumbalearning.domain.enumeration.UserLevel;
import java.io.Serializable;

public class RankingEntryDTO implements Serializable {

    private Long userId;
    private String userName;
    private Integer totalXp;
    private UserLevel currentLevel;
    private Integer currentStreak;
    private String municipalityCode;
    private Integer rank;
    private Boolean isCurrentUser;

    public RankingEntryDTO(
        Long userId,
        String userName,
        Integer totalXp,
        UserLevel currentLevel,
        Integer currentStreak,
        String municipalityCode,
        Integer rank,
        Boolean isCurrentUser
    ) {
        this.userId = userId;
        this.userName = userName;
        this.totalXp = totalXp;
        this.currentLevel = currentLevel;
        this.currentStreak = currentStreak;
        this.municipalityCode = municipalityCode;
        this.rank = rank;
        this.isCurrentUser = isCurrentUser;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getTotalXp() {
        return totalXp;
    }

    public void setTotalXp(Integer totalXp) {
        this.totalXp = totalXp;
    }

    public UserLevel getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(UserLevel currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Integer getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(Integer currentStreak) {
        this.currentStreak = currentStreak;
    }

    public String getMunicipalityCode() {
        return municipalityCode;
    }

    public void setMunicipalityCode(String municipalityCode) {
        this.municipalityCode = municipalityCode;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Boolean getIsCurrentUser() {
        return isCurrentUser;
    }

    public void setIsCurrentUser(Boolean isCurrentUser) {
        this.isCurrentUser = isCurrentUser;
    }

    @Override
    public String toString() {
        return (
            "RankingEntryDTO{" +
            "userId=" +
            userId +
            ", userName='" +
            userName +
            '\'' +
            ", totalXp=" +
            totalXp +
            ", currentLevel=" +
            currentLevel +
            ", currentStreak=" +
            currentStreak +
            ", municipalityCode='" +
            municipalityCode +
            '\'' +
            ", rank=" +
            rank +
            ", isCurrentUser=" +
            isCurrentUser +
            '}'
        );
    }
}
