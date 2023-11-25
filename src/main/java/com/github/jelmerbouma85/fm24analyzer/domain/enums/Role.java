package com.github.jelmerbouma85.fm24analyzer.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Role {

    BPD("Ball Playing Defender"),
    CD("Central Defender"),
    NCB("No-Nonsense Centre-Back"),
    L("Libero"),
    NFB("No-Nonense Full-Back"),
    FB("Full-Back"),
    WB("Wing-Back"),
    CWB("Complete Wing-Back"),
    IFB("Inverted Full-Back"),
    IWB("Inverted Wing-Back"),
    WCB("Wide Centre-Back"),
    DLP("Deep-Lying Playmaker"),
    SV("Segundo Valente"),
    RPM("Roaming Playmaker"),
    RGA("Regista"),
    HB("Half-Back"),
    A("Anchor man"),
    DM("Defensive Midfielder"),
    BWM("Ball-Winning Midfielder"),
    AF("Advanced Forward"),
    CM("Central Midfielder"),
    MEZ("Mezzala"),
    AP("Advanced Playmaker"),
    CAR("Carrilero"),
    BBM("Box-To-Box Midfielder"),
    W("Winger"),
    IW("Inverted Winger"),
    WP("Wide Playmaker"),
    DW("Defensive Winger"),
    WM("Wide Midfielder"),
    IF("Inside Forward"),
    RMD("Raumdeuter"),
    WT("Wide Target man"),
    T("Trequartista"),
    AM("Attacking Midfielder"),
    SS("Shadow Striker"),
    EG("Enganche"),
    DLF("Deep-Lying Forward"),
    F9("False Nine"),
    P("Poacher"),
    TF("Target man"),
    CFW("Complete Forward"),
    PF("Pressing Forward"),
    G("Goalkeeper"),
    SK("Sweeper Keeper");

    private final String tacticalName;

    public static Role fromTacticalName(final String tacticalName) {
        return Arrays.stream(values())
                .filter(role -> role.getTacticalName().equals(tacticalName))
                .findFirst()
                .orElseThrow();
    }
}
