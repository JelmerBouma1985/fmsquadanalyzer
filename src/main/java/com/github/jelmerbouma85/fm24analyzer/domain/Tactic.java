package com.github.jelmerbouma85.fm24analyzer.domain;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Tactic {

    private Set<PlayerPositionalScore> players = new HashSet<>();

    public void addPlayer(PlayerPositionalScore player) {
        this.players.add(player);
    }
}
