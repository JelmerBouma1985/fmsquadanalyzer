package com.github.jelmerbouma85.fm24analyzer.comparators;

import com.github.jelmerbouma85.fm24analyzer.domain.PlayerPositionalScore;
import com.github.jelmerbouma85.fm24analyzer.domain.Tactic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;

public class TacticComparator implements Comparator<Tactic> {

    @Override
    public int compare(Tactic o1, Tactic o2) {
        final var scoreTactic1 = o1.getPlayers().stream().map(PlayerPositionalScore::getScore).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
        final var scoreTactic2 = o2.getPlayers().stream().map(PlayerPositionalScore::getScore).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);

        return scoreTactic1.compareTo(scoreTactic2);
    }
}
