package com.github.jelmerbouma85.fm24analyzer.comparators;

import com.github.jelmerbouma85.fm24analyzer.domain.PlayerPositionalScore;
import com.github.jelmerbouma85.fm24analyzer.domain.enums.Position;

import java.util.Comparator;
import java.util.List;

public class PlayerPositionalScoreComparator implements Comparator<PlayerPositionalScore> {
    private static final List<Position> ORDER = List.of(
            Position.GK,
            Position.DR,
            Position.DCR,
            Position.DC,
            Position.DCL,
            Position.DL,
            Position.WBR,
            Position.DM,
            Position.WBL,
            Position.MR,
            Position.MCR,
            Position.MC,
            Position.MCL,
            Position.ML,
            Position.AMR,
            Position.AMCR,
            Position.AMC,
            Position.AMCL,
            Position.AML,
            Position.STC
    );

    @Override
    public int compare(PlayerPositionalScore o1, PlayerPositionalScore o2) {
        final var index1 = ORDER.indexOf(o1.getPosition().getPosition());
        final var index2 = ORDER.indexOf(o2.getPosition().getPosition());

        return Integer.compare(index1, index2);
    }
}
