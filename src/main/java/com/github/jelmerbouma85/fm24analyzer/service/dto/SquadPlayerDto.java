package com.github.jelmerbouma85.fm24analyzer.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SquadPlayerDto {

    private String name;
    private String position;
    private String role;
    private String duty;
    private String score;
}
