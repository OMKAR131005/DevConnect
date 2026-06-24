package com.devconnect.bakend.ranking.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RankingResponse {
    private int rank;
    private String username;
    private double score;
}
