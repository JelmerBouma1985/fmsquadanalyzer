package com.github.jelmerbouma85.fm24analyzer.mappers;

import com.github.jelmerbouma85.fm24analyzer.domain.PlayerPositionalScore;
import com.github.jelmerbouma85.fm24analyzer.service.dto.SquadPlayerDto;

import java.util.function.Function;

public class SquadPlayerDtoMapper implements Function<PlayerPositionalScore, SquadPlayerDto> {
    @Override
    public SquadPlayerDto apply(PlayerPositionalScore playerPositionalScore) {
        return SquadPlayerDto.builder()
                .name(playerPositionalScore.getPlayer().getName())
                .position(playerPositionalScore.getPosition().getPosition().name())
                .role(playerPositionalScore.getPosition().getRole().name())
                .duty(playerPositionalScore.getPosition().getDuty().name())
                .score(String.valueOf(playerPositionalScore.getScore()))
                .build();
    }
}
