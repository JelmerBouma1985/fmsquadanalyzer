package com.github.jelmerbouma85.fm24analyzer.domain.scouting;

import com.github.jelmerbouma85.fm24analyzer.domain.enums.Duty;
import com.github.jelmerbouma85.fm24analyzer.domain.enums.Position;
import com.github.jelmerbouma85.fm24analyzer.domain.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ScoutTacticPosition {

    private Position position;
    private Role role;
    private Duty duty;
    private BigDecimal minScore;
    private BigDecimal maxScore;
}
