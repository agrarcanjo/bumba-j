package com.bumbatech.bumbalearning.service.dto;

import java.io.Serializable;
import java.util.List;

public class RankingDTO implements Serializable {

    private String period;
    private String municipalityCode;
    private Integer currentUserRank;
    private Integer totalUsers;
    private List<RankingEntryDTO> rankings;

    public RankingDTO() {}

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getMunicipalityCode() {
        return municipalityCode;
    }

    public void setMunicipalityCode(String municipalityCode) {
        this.municipalityCode = municipalityCode;
    }

    public Integer getCurrentUserRank() {
        return currentUserRank;
    }

    public void setCurrentUserRank(Integer currentUserRank) {
        this.currentUserRank = currentUserRank;
    }

    public Integer getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Integer totalUsers) {
        this.totalUsers = totalUsers;
    }

    public List<RankingEntryDTO> getRankings() {
        return rankings;
    }

    public void setRankings(List<RankingEntryDTO> rankings) {
        this.rankings = rankings;
    }

    @Override
    public String toString() {
        return (
            "RankingDTO{" +
            "period='" +
            period +
            '\'' +
            ", municipalityCode='" +
            municipalityCode +
            '\'' +
            ", currentUserRank=" +
            currentUserRank +
            ", totalUsers=" +
            totalUsers +
            ", rankings=" +
            rankings +
            '}'
        );
    }
}
