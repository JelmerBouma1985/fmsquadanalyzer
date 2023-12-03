package com.github.jelmerbouma85.fm24analyzer.readers;

import com.github.jelmerbouma85.fm24analyzer.config.LocalCache;
import com.github.jelmerbouma85.fm24analyzer.domain.Player;
import com.github.jelmerbouma85.fm24analyzer.domain.Squad;
import com.github.jelmerbouma85.fm24analyzer.domain.enums.Attributes;
import com.github.jelmerbouma85.fm24analyzer.domain.enums.Condition;
import com.github.jelmerbouma85.fm24analyzer.domain.enums.Position;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.github.jelmerbouma85.fm24analyzer.domain.enums.Position.*;

@Component
public class PlayerReader {
    public Squad readPlayerFromHtml(final InputStream squadInputStream, final LocalCache localCache) {
        try {
            final var document = Jsoup.parse(squadInputStream, StandardCharsets.UTF_8.name(), "www.fm24analyzer.com");
            final var table = document.selectFirst("table");
            final var rows = Objects.requireNonNull(table, "Er is geen table gevonden").select("tr");
            final List<Player> players = new ArrayList<>();
            localCache.setSquadHeaderElements(rows.select("th"));
            for (Element row : rows) {
                final var tableData = row.select("td");
                if (!tableData.isEmpty() && isPlayerNotOnTrial(tableData, localCache)) {
                    var player = Player.builder()
                            .name(tableData.get(localCache.getSquadPlayerAttributeLocation("Player")).text().replaceAll(" - Pick Player", ""))
                            .age(Integer.parseInt(tableData.get(localCache.getSquadPlayerAttributeLocation("Age")).text()))
                            .positions(getPlayablePositions(tableData.get(localCache.getSquadPlayerAttributeLocation("Position")).text()))
                            .status(getStatusInfo(tableData, localCache))
                            .condition(getCondition(tableData, localCache))
                            .attributes(getAttributes(tableData, localCache))
                            .build();
                    players.add(player);
                }
            }

            final var squad = new Squad();
            squad.setGk(players.stream().filter(player -> player.getPositions().contains(GK)).collect(Collectors.toSet()));
            squad.setDr(players.stream().filter(player -> player.getPositions().contains(DR)).collect(Collectors.toSet()));
            squad.setDc(players.stream().filter(player -> player.getPositions().stream().anyMatch(position -> Set.of(DCR, DC, DCL).contains(position))).collect(Collectors.toSet()));
            squad.setDl(players.stream().filter(player -> player.getPositions().contains(DL)).collect(Collectors.toSet()));
            squad.setWbr(players.stream().filter(player -> player.getPositions().contains(WBR)).collect(Collectors.toSet()));
            squad.setDm(players.stream().filter(player -> player.getPositions().contains(DM)).collect(Collectors.toSet()));
            squad.setWbl(players.stream().filter(player -> player.getPositions().contains(WBL)).collect(Collectors.toSet()));
            squad.setMr(players.stream().filter(player -> player.getPositions().contains(MR)).collect(Collectors.toSet()));
            squad.setMc(players.stream().filter(player -> player.getPositions().stream().anyMatch(position -> Set.of(MCR, MC, MCL).contains(position))).collect(Collectors.toSet()));
            squad.setMl(players.stream().filter(player -> player.getPositions().contains(ML)).collect(Collectors.toSet()));
            squad.setAmr(players.stream().filter(player -> player.getPositions().contains(AMR)).collect(Collectors.toSet()));
            squad.setAmc(players.stream().filter(player -> player.getPositions().stream().anyMatch(position -> Set.of(AMCR, AMC, AMCL).contains(position))).collect(Collectors.toSet()));
            squad.setAml(players.stream().filter(player -> player.getPositions().contains(AML)).collect(Collectors.toSet()));
            squad.setSc(players.stream().filter(player -> player.getPositions().contains(STC)).collect(Collectors.toSet()));

            return squad;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<String> getStatusInfo(final Elements tableData, final LocalCache localCache) {
        final var playerStatus = tableData.get(localCache.getSquadPlayerAttributeLocation("Player Status")).text();
        if (!playerStatus.isBlank()) {
            return Arrays.stream(playerStatus.split("-"))
                    .map(String::trim)
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    private Condition getCondition(final Elements tableData, final LocalCache localCache) {
        return Condition.fromCode(tableData.get(localCache.getSquadPlayerAttributeLocation("CON")).text());
    }

    private boolean isPlayerNotOnTrial(final Elements tabledata, LocalCache localCache) {
        for (Attributes attribute : Arrays.stream(Attributes.values()).filter(att -> !List.of(Attributes.OVO, Attributes.LTH).contains(att)).toList()) {
            if (!Pattern.compile("\\d+").matcher(tabledata.get(localCache.getSquadPlayerAttributeLocation(attribute.name())).text()).matches()) {
                return false;
            }
        }
        return true;
    }

    private Set<Position> getPlayablePositions(String position) {
        final String[] positions = position.split(",");

        final Set<Position> playablePositions = new HashSet<>();
        for (String playerPosition : positions) {
            playerPosition = playerPosition.trim();
            if (playerPosition.equals(GK.name()) || playerPosition.equals(DM.name())) {
                playablePositions.add(Position.valueOf(playerPosition));
            } else {
                final int indexOfOpeningBrace = playerPosition.indexOf("(");
                final int indexOfClosingBrace = playerPosition.indexOf(")");
                final String fieldPosition = playerPosition.substring(0, indexOfOpeningBrace).trim();
                final String[] allFieldPositions = fieldPosition.split("/");
                char[] leftRightCenter = playerPosition.substring(indexOfOpeningBrace + 1, indexOfClosingBrace).toCharArray();
                for (String p : allFieldPositions) {
                    for (char lrc : leftRightCenter) {
                        playablePositions.add(Position.valueOf(p + lrc));
                    }
                }
            }
        }
        return playablePositions;
    }

    private Map<Attributes, Integer> getAttributes(final Elements tabledata, LocalCache localCache) {
        final Map<Attributes, Integer> attributes = new HashMap<>();
        attributes.put(Attributes.ACC, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.ACC.name())).text()));
        attributes.put(Attributes.WOR, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.WOR.name())).text()));
        attributes.put(Attributes.VIS, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.VIS.name())).text()));
        attributes.put(Attributes.THR, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.THR.name())).text()));
        attributes.put(Attributes.TEC, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.TEC.name())).text()));
        attributes.put(Attributes.TEA, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.TEA.name())).text()));
        attributes.put(Attributes.TCK, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.TCK.name())).text()));
        attributes.put(Attributes.STR, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.STR.name())).text()));
        attributes.put(Attributes.STA, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.STA.name())).text()));
        attributes.put(Attributes.TRO, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.TRO.name())).text()));
        attributes.put(Attributes.REF, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.REF.name())).text()));
        attributes.put(Attributes.PUN, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.PUN.name())).text()));
        attributes.put(Attributes.POS, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.POS.name())).text()));
        attributes.put(Attributes.PEN, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.PEN.name())).text()));
        attributes.put(Attributes.PAS, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.PAS.name())).text()));
        attributes.put(Attributes.PAC, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.PAC.name())).text()));
        attributes.put(Attributes.OVO, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation("1v1")).text()));
        attributes.put(Attributes.OTB, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.OTB.name())).text()));
        attributes.put(Attributes.NAT, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.NAT.name())).text()));
        attributes.put(Attributes.MAR, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.MAR.name())).text()));
        attributes.put(Attributes.LTH, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation("L th")).text()));
        attributes.put(Attributes.LON, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.LON.name())).text()));
        attributes.put(Attributes.LDR, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.LDR.name())).text()));
        attributes.put(Attributes.KIC, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.KIC.name())).text()));
        attributes.put(Attributes.JUM, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.JUM.name())).text()));
        attributes.put(Attributes.HEA, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.HEA.name())).text()));
        attributes.put(Attributes.HAN, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.HAN.name())).text()));
        attributes.put(Attributes.FRE, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.FRE.name())).text()));
        attributes.put(Attributes.FLA, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.FLA.name())).text()));
        attributes.put(Attributes.FIR, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.FIR.name())).text()));
        attributes.put(Attributes.FIN, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.FIN.name())).text()));
        attributes.put(Attributes.ECC, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.ECC.name())).text()));
        attributes.put(Attributes.DRI, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.DRI.name())).text()));
        attributes.put(Attributes.DET, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.DET.name())).text()));
        attributes.put(Attributes.DEC, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.DEC.name())).text()));
        attributes.put(Attributes.CRO, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.CRO.name())).text()));
        attributes.put(Attributes.COR, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.COR.name())).text()));
        attributes.put(Attributes.CNT, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.CNT.name())).text()));
        attributes.put(Attributes.CMP, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.CMP.name())).text()));
        attributes.put(Attributes.COM, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.COM.name())).text()));
        attributes.put(Attributes.CMD, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.CMD.name())).text()));
        attributes.put(Attributes.BRA, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.BRA.name())).text()));
        attributes.put(Attributes.BAL, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.BAL.name())).text()));
        attributes.put(Attributes.ANT, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.ANT.name())).text()));
        attributes.put(Attributes.AGI, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.AGI.name())).text()));
        attributes.put(Attributes.AGG, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.AGG.name())).text()));
        attributes.put(Attributes.AER, Integer.parseInt(tabledata.get(localCache.getSquadPlayerAttributeLocation(Attributes.AER.name())).text()));

        return attributes;
    }
}
