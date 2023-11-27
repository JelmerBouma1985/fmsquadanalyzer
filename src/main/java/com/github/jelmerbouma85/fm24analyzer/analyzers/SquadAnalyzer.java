package com.github.jelmerbouma85.fm24analyzer.analyzers;

import com.github.jelmerbouma85.fm24analyzer.domain.PlayerPositionalScore;
import com.github.jelmerbouma85.fm24analyzer.domain.Squad;
import com.github.jelmerbouma85.fm24analyzer.domain.Tactic;
import com.github.jelmerbouma85.fm24analyzer.domain.TacticPosition;
import com.github.jelmerbouma85.fm24analyzer.domain.enums.Position;
import com.github.jelmerbouma85.fm24analyzer.readers.PlayerReader;
import com.github.jelmerbouma85.fm24analyzer.readers.TacticReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

@Service
@Slf4j
public class SquadAnalyzer {

    private final PlayerReader playerReader;
    private final TacticReader tacticReader;
    private final PlayerAnalyzer playerAnalyzer;

    public SquadAnalyzer(PlayerReader playerReader, TacticReader tacticReader, PlayerAnalyzer playerAnalyzer) {
        this.playerReader = playerReader;
        this.tacticReader = tacticReader;
        this.playerAnalyzer = playerAnalyzer;
    }

    public Set<Tactic> buildOverallBestSquad(final Squad squad, final List<TacticPosition> positions) {
        final var positionalRatings = playerAnalyzer.analyzeOverallBest11(squad, positions);

        final var tactic = new Tactic();
        return buildTactics(tactic, positions, positionalRatings, new HashSet<>());
    }

    public Set<Tactic> buildCurrentBestSquad(final Squad squad, final List<TacticPosition> positions) {
        final var positionalRatings = playerAnalyzer.analyzeCurrentBest11(squad, positions);

        final var tactic = new Tactic();
        return buildTactics(tactic, positions, positionalRatings, new HashSet<>());
    }

    private Set<Tactic> buildTactics(final Tactic tactic, final List<TacticPosition> positions, final MultiValueMap<Position, PlayerPositionalScore> positionalRatings, final Set<Tactic> tactics) {
        for (var position : positions) {
            var players = positionalRatings.get(position.getPosition()).stream()
                    .sorted(Comparator.comparing(PlayerPositionalScore::getScore).reversed())
                    .toList();

            if (tactic.getPlayers().stream().noneMatch(pps -> pps.getPosition().equals(position))) {
                for (var player : players) {
                    if (tactic.getPlayers().stream().noneMatch(p -> p.getPlayer().equals(player.getPlayer()))) {
                        tactic.addPlayer(player);
                        break;
                    } else {
                        var newTactic = new Tactic();
                        newTactic.addPlayer(player);

                        var currentPositionPlayer = tactic.getPlayers().stream().filter(pps -> pps.getPlayer().equals(player.getPlayer())).findFirst().orElseThrow();

                        final MultiValueMap<Position, PlayerPositionalScore> customPositionalRatings = new LinkedMultiValueMap<>();
                        positionalRatings.forEach((key, value) -> customPositionalRatings.put(key, new ArrayList<>(value)));

                        Objects.requireNonNull(customPositionalRatings.get(currentPositionPlayer.getPosition().getPosition())).remove(currentPositionPlayer);
                        buildTactics(newTactic, positions, customPositionalRatings, tactics);
                    }
                }
            }
        }
        tactics.add(tactic);
        return tactics;
    }
}
