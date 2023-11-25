package com.github.jelmerbouma85.fm24analyzer.analyzers;

import com.github.jelmerbouma85.fm24analyzer.config.AttributeProperties;
import com.github.jelmerbouma85.fm24analyzer.domain.TacticPosition;
import com.github.jelmerbouma85.fm24analyzer.domain.enums.Attributes;
import com.github.jelmerbouma85.fm24analyzer.domain.scouting.ScoutAttribute;
import com.github.jelmerbouma85.fm24analyzer.domain.scouting.ScoutPlayer;
import com.github.jelmerbouma85.fm24analyzer.domain.scouting.ScoutPlayerPositionalScore;
import com.github.jelmerbouma85.fm24analyzer.domain.scouting.ScoutTacticPosition;
import com.github.jelmerbouma85.fm24analyzer.readers.TacticReader;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

@Service
public class ScoutPlayerAnalyzer {

    private static final BigDecimal MULITPLIER_14 = new BigDecimal("14").setScale(2, RoundingMode.HALF_UP);
    private static final BigDecimal MULITPLIER_20 = new BigDecimal("20").setScale(2, RoundingMode.HALF_UP);
    private final AttributeProperties attributeProperties;
    private final TacticReader tacticReader;

    public ScoutPlayerAnalyzer(AttributeProperties attributeProperties, TacticReader tacticReader) {
        this.attributeProperties = attributeProperties;
        this.tacticReader = tacticReader;
    }

    public List<ScoutPlayerPositionalScore> analyzePlayers(final List<ScoutPlayer> players, final List<TacticPosition> tacticPositions) {
        final List<ScoutPlayerPositionalScore> playersRatings = new ArrayList<>();

        for (var scoutPlayer : players) {
            final List<ScoutTacticPosition> positions = new ArrayList<>();

            for (var position : tacticPositions) {
                var minMax = analyzePlayer(position, scoutPlayer);
                positions.add(
                        ScoutTacticPosition.builder()
                                .position(position.getPosition())
                                .role(position.getRole())
                                .duty(position.getDuty())
                                .minScore(minMax.getKey())
                                .maxScore(minMax.getValue())
                                .build()
                );
            }

            playersRatings.add(
                    ScoutPlayerPositionalScore.builder()
                            .player(scoutPlayer)
                            .positions(positions)
                            .build()
            );
        }

        return playersRatings;
    }

    private AbstractMap.SimpleImmutableEntry<BigDecimal, BigDecimal> analyzePlayer(TacticPosition tacticPosition, ScoutPlayer player) {
        final var attributes = attributeProperties.getPositions().stream()
                .filter(att -> att.getPosition().contains(tacticPosition.getPosition()))
                .filter(att -> att.getRole().equals(tacticPosition.getRole()))
                .filter(att -> att.getDuty().equals(tacticPosition.getDuty()))
                .findFirst()
                .orElseThrow();

        final var maxScorePrimary = createBigDecimal(attributes.getPrimary().size()).multiply(MULITPLIER_20);
        final var maxScoreSecondary = createBigDecimal(attributes.getSecondary().size()).multiply(MULITPLIER_14);
        final var maxScore = maxScorePrimary.add(maxScoreSecondary);
        final var playerMinPrimaryScore = getScore(player, attributes.getPrimary(), ScoutAttribute::getMin);
        final var playerMinSecondaryScore = getScore(player, attributes.getSecondary(), ScoutAttribute::getMin).multiply(createBigDecimal("0.7"));
        final var playerMinTotalScore = playerMinPrimaryScore.add(playerMinSecondaryScore);
        final var playerMaxPrimaryScore = getScore(player, attributes.getPrimary(), ScoutAttribute::getMax);
        final var playerMaxSecondaryScore = getScore(player, attributes.getSecondary(), ScoutAttribute::getMax).multiply(createBigDecimal("0.7"));
        final var playerMaxTotalScore = playerMaxPrimaryScore.add(playerMaxSecondaryScore);
        final var minPositionalScore = playerMinTotalScore
                .divide(maxScore, RoundingMode.HALF_UP)
                .multiply(createBigDecimal(100))
                .setScale(2, RoundingMode.HALF_UP);
        final var maxPositionalScore = playerMaxTotalScore
                .divide(maxScore, RoundingMode.HALF_UP)
                .multiply(createBigDecimal(100))
                .setScale(2, RoundingMode.HALF_UP);
        return new AbstractMap.SimpleImmutableEntry<>(minPositionalScore, maxPositionalScore);
    }

    private BigDecimal getScore(final ScoutPlayer player, List<Attributes> attributes, ToIntFunction<ScoutAttribute> minMaxFunction) {
        final int scoreAttributes = player.getAttributes().stream()
                .filter(att -> attributes.contains(att.getAttribute()))
                .mapToInt(minMaxFunction)
                .sum();

        return createBigDecimal(scoreAttributes);
    }

    private BigDecimal createBigDecimal(Object waarde) {
        return new BigDecimal(waarde.toString()).setScale(2, RoundingMode.HALF_UP);
    }
}
