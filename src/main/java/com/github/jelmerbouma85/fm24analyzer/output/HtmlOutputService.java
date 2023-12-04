package com.github.jelmerbouma85.fm24analyzer.output;

import com.github.jelmerbouma85.fm24analyzer.analyzers.ScoutPlayerAnalyzer;
import com.github.jelmerbouma85.fm24analyzer.analyzers.SquadAnalyzer;
import com.github.jelmerbouma85.fm24analyzer.comparators.PlayerPositionalScoreComparator;
import com.github.jelmerbouma85.fm24analyzer.comparators.TacticComparator;
import com.github.jelmerbouma85.fm24analyzer.config.LocalCache;
import com.github.jelmerbouma85.fm24analyzer.domain.TacticPosition;
import com.github.jelmerbouma85.fm24analyzer.domain.scouting.ScoutPlayerPositionalScore;
import com.github.jelmerbouma85.fm24analyzer.mappers.ScoutPlayerDtoMapper;
import com.github.jelmerbouma85.fm24analyzer.mappers.SquadPlayerDtoMapper;
import com.github.jelmerbouma85.fm24analyzer.output.dto.ScoutPlayerColumnDto;
import com.github.jelmerbouma85.fm24analyzer.output.dto.ScoutPlayerDto;
import com.github.jelmerbouma85.fm24analyzer.output.dto.SquadPlayerDto;
import com.github.jelmerbouma85.fm24analyzer.readers.PlayerReader;
import com.github.jelmerbouma85.fm24analyzer.readers.ScoutingReader;
import com.github.jelmerbouma85.fm24analyzer.readers.TacticReader;
import com.github.jelmerbouma85.fm24analyzer.validators.HtmlInputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HtmlOutputService {

    private final SquadAnalyzer service;
    private final ScoutingReader scoutingReader;
    private final TacticReader tacticReader;
    private final ScoutPlayerAnalyzer scoutPlayerAnalyzer;
    private final PlayerReader playerReader;

    public HtmlOutputService(SquadAnalyzer service, ScoutingReader scoutingReader, TacticReader tacticReader, ScoutPlayerAnalyzer scoutPlayerAnalyzer, PlayerReader playerReader) {
        this.service = service;
        this.scoutingReader = scoutingReader;
        this.tacticReader = tacticReader;
        this.scoutPlayerAnalyzer = scoutPlayerAnalyzer;
        this.playerReader = playerReader;
    }

    public void buildTemplate(final String user, final Model model, final InputStream squadInputstream, final InputStream tacticInputStream, final InputStream scoutPlayersInputStream) {
        final var localCache = new LocalCache();
        final var squad = playerReader.readPlayerFromHtml(user, squadInputstream, localCache);
        final var tacticPositions = tacticReader.readTacticFromHtml(tacticInputStream, user);

        final var scoutPlayers = scoutingReader.readPlayersFromHtml(scoutPlayersInputStream, localCache);
        final var scoutedPlayers = scoutPlayerAnalyzer.analyzePlayers(scoutPlayers, tacticPositions);

        final var overallBestTactic = service.buildOverallBestSquad(squad, tacticPositions);
        var overallbestSquadDto = overallBestTactic.stream()
                .max(new TacticComparator())
                .stream()
                .flatMap(t -> t.getPlayers().stream())
                .sorted(new PlayerPositionalScoreComparator())
                .map(p -> new SquadPlayerDtoMapper().apply(p))
                .toList();

        final var currentBestTactic = service.buildCurrentBestSquad(squad, tacticPositions);
        var currentBestSquadDto = currentBestTactic.stream()
                .max(new TacticComparator())
                .stream()
                .flatMap(t -> t.getPlayers().stream())
                .sorted(new PlayerPositionalScoreComparator())
                .map(p -> new SquadPlayerDtoMapper().apply(p))
                .toList();

        model.addAttribute("players", overallbestSquadDto);
        model.addAttribute("currentPlayers", currentBestSquadDto);
        model.addAttribute("header", addHeaders(overallbestSquadDto, tacticPositions));
        model.addAttribute("scoutedPlayers", getScoutPlayerDto(scoutedPlayers, tacticPositions));
    }

    private ScoutPlayerColumnDto addHeaders(final List<SquadPlayerDto> squadPlayers, final java.util.List<TacticPosition> tacticPositions) {
        return ScoutPlayerColumnDto.builder()
                .column1(buildColumnHeader(tacticPositions.get(0), squadPlayers))
                .column2(buildColumnHeader(tacticPositions.get(1), squadPlayers))
                .column3(buildColumnHeader(tacticPositions.get(2), squadPlayers))
                .column4(buildColumnHeader(tacticPositions.get(3), squadPlayers))
                .column5(buildColumnHeader(tacticPositions.get(4), squadPlayers))
                .column6(buildColumnHeader(tacticPositions.get(5), squadPlayers))
                .column7(buildColumnHeader(tacticPositions.get(6), squadPlayers))
                .column8(buildColumnHeader(tacticPositions.get(7), squadPlayers))
                .column9(buildColumnHeader(tacticPositions.get(8), squadPlayers))
                .column10(buildColumnHeader(tacticPositions.get(9), squadPlayers))
                .column11(buildColumnHeader(tacticPositions.get(10), squadPlayers))
                .build();
    }

    private String buildColumnHeader(TacticPosition tacticPosition, final List<SquadPlayerDto> squadPlayers) {
        return tacticPosition.getPosition().name()
                + " ("
                + squadPlayers.stream()
                .filter(player -> player.getPosition().equals(tacticPosition.getPosition().name()))
                .map(SquadPlayerDto::getScore)
                .findFirst()
                .orElseThrow()
                + ")";
    }

    private java.util.List<ScoutPlayerDto> getScoutPlayerDto(final java.util.List<ScoutPlayerPositionalScore> players, final List<TacticPosition> tacticPositions) {
        return players.stream()
                .map(player -> new ScoutPlayerDtoMapper().apply(player, tacticPositions))
                .collect(Collectors.toList());
    }
}
