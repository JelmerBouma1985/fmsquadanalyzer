package com.github.jelmerbouma85.fm24analyzer.mappers;

import com.github.jelmerbouma85.fm24analyzer.domain.TacticPosition;
import com.github.jelmerbouma85.fm24analyzer.domain.enums.Position;
import com.github.jelmerbouma85.fm24analyzer.domain.scouting.ScoutPlayerPositionalScore;
import com.github.jelmerbouma85.fm24analyzer.service.dto.ScoutPlayerDto;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static com.github.jelmerbouma85.fm24analyzer.domain.enums.Position.*;

public class ScoutPlayerDtoMapper implements BiFunction<ScoutPlayerPositionalScore, List<TacticPosition>, ScoutPlayerDto> {

    @Override
    public ScoutPlayerDto apply(ScoutPlayerPositionalScore scoutPlayerPositionalScore, List<TacticPosition> tacticPositions) {
        final var scoutPlayer = ScoutPlayerDto.builder()
                .name(scoutPlayerPositionalScore.getPlayer().getName())
                .age(scoutPlayerPositionalScore.getPlayer().getAge())
                .transferValue(scoutPlayerPositionalScore.getPlayer().getTransferValue())
                .salary(scoutPlayerPositionalScore.getPlayer().getSalary());

        getPositionScore(scoutPlayerPositionalScore, tacticPositions.get(0))
                .ifPresent(entry -> {
                    scoutPlayer.position1Min(entry.getKey());
                    scoutPlayer.position1Max(entry.getValue());
                });
        getPositionScore(scoutPlayerPositionalScore, tacticPositions.get(1))
                .ifPresent(entry -> {
                    scoutPlayer.position2Min(entry.getKey());
                    scoutPlayer.position2Max(entry.getValue());
                });
        getPositionScore(scoutPlayerPositionalScore, tacticPositions.get(2))
                .ifPresent(entry -> {
                    scoutPlayer.position3Min(entry.getKey());
                    scoutPlayer.position3Max(entry.getValue());
                });
        getPositionScore(scoutPlayerPositionalScore, tacticPositions.get(3))
                .ifPresent(entry -> {
                    scoutPlayer.position4Min(entry.getKey());
                    scoutPlayer.position4Max(entry.getValue());
                });
        getPositionScore(scoutPlayerPositionalScore, tacticPositions.get(4))
                .ifPresent(entry -> {
                    scoutPlayer.position5Min(entry.getKey());
                    scoutPlayer.position5Max(entry.getValue());
                });
        getPositionScore(scoutPlayerPositionalScore, tacticPositions.get(5))
                .ifPresent(entry -> {
                    scoutPlayer.position6Min(entry.getKey());
                    scoutPlayer.position6Max(entry.getValue());
                });
        getPositionScore(scoutPlayerPositionalScore, tacticPositions.get(6))
                .ifPresent(entry -> {
                    scoutPlayer.position7Min(entry.getKey());
                    scoutPlayer.position7Max(entry.getValue());
                });
        getPositionScore(scoutPlayerPositionalScore, tacticPositions.get(7))
                .ifPresent(entry -> {
                    scoutPlayer.position8Min(entry.getKey());
                    scoutPlayer.position8Max(entry.getValue());
                });
        getPositionScore(scoutPlayerPositionalScore, tacticPositions.get(8))
                .ifPresent(entry -> {
                    scoutPlayer.position9Min(entry.getKey());
                    scoutPlayer.position9Max(entry.getValue());
                });
        getPositionScore(scoutPlayerPositionalScore, tacticPositions.get(9))
                .ifPresent(entry -> {
                    scoutPlayer.position10Min(entry.getKey());
                    scoutPlayer.position10Max(entry.getValue());
                });
        getPositionScore(scoutPlayerPositionalScore, tacticPositions.get(10))
                .ifPresent(entry -> {
                    scoutPlayer.position11Min(entry.getKey());
                    scoutPlayer.position11Max(entry.getValue());
                });

        return scoutPlayer.build();
    }

    private Optional<AbstractMap.SimpleImmutableEntry<BigDecimal, BigDecimal>> getPositionScore(final ScoutPlayerPositionalScore scoutPlayerPositionalScore, final TacticPosition position) {
        var playablePositionOptional = scoutPlayerPositionalScore.getPlayer().getPositions().stream()
                .anyMatch(playablePositionPredicate(position.getPosition()));

        if (playablePositionOptional) {
            var playablePosition = scoutPlayerPositionalScore.getPositions().stream()
                    .filter(scoutTacticPosition -> scoutTacticPosition.getPosition().equals(position.getPosition()))
                    .findFirst()
                    .orElseThrow();
            return Optional.of(new AbstractMap.SimpleImmutableEntry<>(playablePosition.getMinScore(), playablePosition.getMaxScore()));
        } else {
            return Optional.empty();
        }
    }

    private Predicate<Position> playablePositionPredicate(final Position tacticsPosition) {
        return position -> {
            switch (tacticsPosition) {
                case DCR, DCL -> {
                    return DC.equals(position);
                }
                case DMCR, DMCL -> {
                    return DM.equals(position);
                }
                case MCR, MCL -> {
                    return MC.equals(position);
                }
                case AMCR, AMCL -> {
                    return AMC.equals(position);
                }
                case STCR, STCL -> {
                    return STC.equals(position);
                }
                default -> {
                    return tacticsPosition.equals(position);
                }
            }
        };
    }
}
