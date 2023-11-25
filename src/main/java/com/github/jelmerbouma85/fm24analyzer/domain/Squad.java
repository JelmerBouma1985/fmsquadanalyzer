package com.github.jelmerbouma85.fm24analyzer.domain;

import lombok.Data;

import java.util.Set;

@Data
public class Squad {

    private Set<Player> gk;
    private Set<Player> dr;
    private Set<Player> dc;
    private Set<Player> dl;
    private Set<Player> wbr;
    private Set<Player> dm;
    private Set<Player> wbl;
    private Set<Player> mr;
    private Set<Player> mc;
    private Set<Player> ml;
    private Set<Player> amr;
    private Set<Player> amc;
    private Set<Player> aml;
    private Set<Player> sc;
}
