package com.github.jelmerbouma85.fm24analyzer.domain.scouting;

import com.github.jelmerbouma85.fm24analyzer.domain.TacticPosition;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ScoutPlayerPositionalScore {

    private ScoutPlayer player;
    private List<ScoutTacticPosition> positions = new ArrayList<>();
}
