package com.github.jelmerbouma85.fm24analyzer.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PlayerPositionalScore {

    private Player player;
    private TacticPosition position;
    private BigDecimal score;
}
