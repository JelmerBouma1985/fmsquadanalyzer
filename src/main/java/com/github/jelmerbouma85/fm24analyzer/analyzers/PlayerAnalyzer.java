package com.github.jelmerbouma85.fm24analyzer.analyzers;

import com.github.jelmerbouma85.fm24analyzer.config.AttributeProperties;
import com.github.jelmerbouma85.fm24analyzer.domain.Player;
import com.github.jelmerbouma85.fm24analyzer.domain.PlayerPositionalScore;
import com.github.jelmerbouma85.fm24analyzer.domain.Squad;
import com.github.jelmerbouma85.fm24analyzer.domain.TacticPosition;
import com.github.jelmerbouma85.fm24analyzer.domain.enums.Attributes;
import com.github.jelmerbouma85.fm24analyzer.domain.enums.Condition;
import com.github.jelmerbouma85.fm24analyzer.domain.enums.Position;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class PlayerAnalyzer {

    private static final Set<String> INJURED_AND_INTERLAND_STATUS = Set.of("Inj", "Int");
    private static final Set<Condition> GOOD_CONDITION = Set.of(Condition.EXCELLENT, Condition.PEAK);
    private static final BigDecimal MULITPLIER_14 = new BigDecimal("14").setScale(2, RoundingMode.HALF_UP);
    private static final BigDecimal MULITPLIER_20 = new BigDecimal("20").setScale(2, RoundingMode.HALF_UP);
    private final AttributeProperties attributeProperties;

    public PlayerAnalyzer(AttributeProperties attributeProperties) {
        this.attributeProperties = attributeProperties;
    }

    public MultiValueMap<Position, PlayerPositionalScore> analyzeOverallBest11(final Squad squad, final List<TacticPosition> tacticPositions) {
        final MultiValueMap<Position, PlayerPositionalScore> positionalRatings = new LinkedMultiValueMap<>();
        for (var position : tacticPositions) {
            var players = getCorrectPlayers(position.getPosition(), squad);
            for (var player : players) {
                var playerScore = PlayerPositionalScore.builder()
                        .player(player)
                        .position(position)
                        .score(analyzePlayer(position, player))
                        .build();

                positionalRatings.add(position.getPosition(), playerScore);
            }
        }
        return positionalRatings;
    }

    public MultiValueMap<Position, PlayerPositionalScore> analyzeCurrentBest11(final Squad squad, final List<TacticPosition> tacticPositions) {
        final MultiValueMap<Position, PlayerPositionalScore> positionalRatings = new LinkedMultiValueMap<>();
        for (var position : tacticPositions) {
            var players = getCorrectPlayers(position.getPosition(), squad);
            for (var player : players) {
                if (isPlayerAvailableAndFit(player)) {
                    var playerScore = PlayerPositionalScore.builder()
                            .player(player)
                            .position(position)
                            .score(analyzePlayer(position, player))
                            .build();

                    positionalRatings.add(position.getPosition(), playerScore);
                }
            }
        }
        return positionalRatings;
    }

    private boolean isPlayerAvailableAndFit(final Player player) {
        return player.getStatus().stream().noneMatch(INJURED_AND_INTERLAND_STATUS::contains)
                && GOOD_CONDITION.contains(player.getCondition());
    }

    private Set<Player> getCorrectPlayers(final Position position, final Squad squad) {
        switch (position) {
            case GK -> {
                return squad.getGk();
            }
            case DR -> {
                return squad.getDr();
            }
            case DCR, DC, DCL -> {
                return squad.getDc();
            }
            case DL -> {
                return squad.getDl();
            }
            case WBR -> {
                return squad.getWbr();
            }
            case DM -> {
                return squad.getDm();
            }
            case WBL -> {
                return squad.getWbl();
            }
            case MR -> {
                return squad.getMr();
            }
            case MCR, MC, MCL -> {
                return squad.getMc();
            }
            case ML -> {
                return squad.getMl();
            }
            case AMR -> {
                return squad.getAmr();
            }
            case AMCR, AMC, AMCL -> {
                return squad.getAmc();
            }
            case AML -> {
                return squad.getAml();
            }
            case STC -> {
                return squad.getSc();
            }
            default -> throw new IllegalArgumentException("Kan geen spelers ophale voor positie");
        }
    }

    private BigDecimal analyzePlayer(TacticPosition tacticPosition, Player player) {
        final var attributes = attributeProperties.getPositions().stream()
                .filter(att -> att.getPosition().contains(tacticPosition.getPosition()))
                .filter(att -> att.getRole().equals(tacticPosition.getRole()))
                .filter(att -> att.getDuty().equals(tacticPosition.getDuty()))
                .findFirst()
                .orElseThrow();

        final var maxScorePrimary = createBigDecimal(attributes.getPrimary().size()).multiply(MULITPLIER_20);
        final var maxScoreSecondary = createBigDecimal(attributes.getSecondary().size()).multiply(MULITPLIER_14);
        final var maxScore = maxScorePrimary.add(maxScoreSecondary);
        final var playerPrimaryScore = getScore(player, attributes.getPrimary());
        final var playerSecondaryScore = getScore(player, attributes.getSecondary()).multiply(createBigDecimal("0.7"));
        final var playerTotalScore = playerPrimaryScore.add(playerSecondaryScore);

        return playerTotalScore
                .divide(maxScore, RoundingMode.HALF_UP)
                .multiply(createBigDecimal(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getScore(final Player player, List<Attributes> attributes) {
        final int scoreAttributes = player.getAttributes().entrySet().stream()
                .filter(entry -> attributes.contains(entry.getKey()))
                .mapToInt(Map.Entry::getValue)
                .sum();

        return createBigDecimal(scoreAttributes);
    }

    private BigDecimal createBigDecimal(Object waarde) {
        return new BigDecimal(waarde.toString()).setScale(2, RoundingMode.HALF_UP);
    }
}
