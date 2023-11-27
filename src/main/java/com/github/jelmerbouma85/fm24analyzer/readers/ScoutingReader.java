package com.github.jelmerbouma85.fm24analyzer.readers;

import com.github.jelmerbouma85.fm24analyzer.config.LocalCache;
import com.github.jelmerbouma85.fm24analyzer.domain.enums.Attributes;
import com.github.jelmerbouma85.fm24analyzer.domain.enums.Position;
import com.github.jelmerbouma85.fm24analyzer.domain.scouting.ScoutAttribute;
import com.github.jelmerbouma85.fm24analyzer.domain.scouting.ScoutPlayer;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.github.jelmerbouma85.fm24analyzer.domain.enums.Position.DM;
import static com.github.jelmerbouma85.fm24analyzer.domain.enums.Position.GK;

@Slf4j
@Component
public class ScoutingReader {

    public List<ScoutPlayer> readPlayersFromHtml(final InputStream scoutInputStream, final LocalCache localCache) {
        try {
            final var document = Jsoup.parse(scoutInputStream, StandardCharsets.UTF_8.name(), "www.fm24analyzer.com");
            final var table = document.selectFirst("table");
            if (Objects.nonNull(table)) {
                final var rows = table.select("tr");
                localCache.setScoutHeaderElements(rows.select("th"));
                final List<ScoutPlayer> players = new ArrayList<>();
                for (Element row : rows) {
                    final var tableData = row.select("td");
                    if (!tableData.isEmpty() && !tableData.stream().allMatch(element -> element.text().isBlank())) {
                        var player = ScoutPlayer.builder()
                                .name(tableData.get(localCache.getScoutPlayerAttributeLocation("Name")).text())
                                .transferValue(tableData.get(localCache.getScoutPlayerAttributeLocation("Transfer Value")).text())
                                .salary(tableData.get(localCache.getScoutPlayerAttributeLocation("Salary")).text())
                                .positions(getPlayablePositions(tableData.get(localCache.getScoutPlayerAttributeLocation("Position")).text()))
                                .attributes(getAttributes(tableData, localCache))
                                .build();
                        players.add(player);
                    }
                }
                return players;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Collections.emptyList();
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

    private List<ScoutAttribute> getAttributes(final Elements tabledata, final LocalCache localCache) {
        final List<ScoutAttribute> attributes = new ArrayList<>();

        attributes.add(buildScoutAttribute(Attributes.ACC, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.ACC.name()))));
        attributes.add(buildScoutAttribute(Attributes.WOR, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.WOR.name()))));
        attributes.add(buildScoutAttribute(Attributes.VIS, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.VIS.name()))));
        attributes.add(buildScoutAttribute(Attributes.THR, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.THR.name()))));
        attributes.add(buildScoutAttribute(Attributes.TEC, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.TEC.name()))));
        attributes.add(buildScoutAttribute(Attributes.TEA, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.TEA.name()))));
        attributes.add(buildScoutAttribute(Attributes.TCK, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.TCK.name()))));
        attributes.add(buildScoutAttribute(Attributes.STR, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.STR.name()))));
        attributes.add(buildScoutAttribute(Attributes.STA, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.STA.name()))));
        attributes.add(buildScoutAttribute(Attributes.TRO, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.TRO.name()))));
        attributes.add(buildScoutAttribute(Attributes.REF, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.REF.name()))));
        attributes.add(buildScoutAttribute(Attributes.PUN, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.PUN.name()))));
        attributes.add(buildScoutAttribute(Attributes.POS, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.POS.name()))));
        attributes.add(buildScoutAttribute(Attributes.PEN, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.PEN.name()))));
        attributes.add(buildScoutAttribute(Attributes.PAS, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.PAS.name()))));
        attributes.add(buildScoutAttribute(Attributes.PAC, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.PAC.name()))));
        attributes.add(buildScoutAttribute(Attributes.OVO, tabledata.get(localCache.getScoutPlayerAttributeLocation("1V1"))));
        attributes.add(buildScoutAttribute(Attributes.OTB, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.OTB.name()))));
        attributes.add(buildScoutAttribute(Attributes.NAT, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.NAT.name()))));
        attributes.add(buildScoutAttribute(Attributes.MAR, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.MAR.name()))));
        attributes.add(buildScoutAttribute(Attributes.LTH, tabledata.get(localCache.getScoutPlayerAttributeLocation("L TH"))));
        attributes.add(buildScoutAttribute(Attributes.LON, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.LON.name()))));
        attributes.add(buildScoutAttribute(Attributes.LDR, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.LDR.name()))));
        attributes.add(buildScoutAttribute(Attributes.KIC, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.KIC.name()))));
        attributes.add(buildScoutAttribute(Attributes.JUM, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.JUM.name()))));
        attributes.add(buildScoutAttribute(Attributes.HEA, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.HEA.name()))));
        attributes.add(buildScoutAttribute(Attributes.HAN, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.HAN.name()))));
        attributes.add(buildScoutAttribute(Attributes.FRE, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.FRE.name()))));
        attributes.add(buildScoutAttribute(Attributes.FLA, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.FLA.name()))));
        attributes.add(buildScoutAttribute(Attributes.FIR, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.FIR.name()))));
        attributes.add(buildScoutAttribute(Attributes.FIN, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.FIN.name()))));
        attributes.add(buildScoutAttribute(Attributes.ECC, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.ECC.name()))));
        attributes.add(buildScoutAttribute(Attributes.DRI, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.DRI.name()))));
        attributes.add(buildScoutAttribute(Attributes.DET, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.DET.name()))));
        attributes.add(buildScoutAttribute(Attributes.DEC, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.DEC.name()))));
        attributes.add(buildScoutAttribute(Attributes.CRO, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.CRO.name()))));
        attributes.add(buildScoutAttribute(Attributes.COR, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.COR.name()))));
        attributes.add(buildScoutAttribute(Attributes.CNT, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.CNT.name()))));
        attributes.add(buildScoutAttribute(Attributes.CMP, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.CMP.name()))));
        attributes.add(buildScoutAttribute(Attributes.COM, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.COM.name()))));
        attributes.add(buildScoutAttribute(Attributes.CMD, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.CMD.name()))));
        attributes.add(buildScoutAttribute(Attributes.BRA, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.BRA.name()))));
        attributes.add(buildScoutAttribute(Attributes.BAL, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.BAL.name()))));
        attributes.add(buildScoutAttribute(Attributes.ANT, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.ANT.name()))));
        attributes.add(buildScoutAttribute(Attributes.AGI, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.AGI.name()))));
        attributes.add(buildScoutAttribute(Attributes.AGG, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.AGG.name()))));
        attributes.add(buildScoutAttribute(Attributes.AER, tabledata.get(localCache.getScoutPlayerAttributeLocation(Attributes.AER.name()))));

        return attributes;
    }

    private AbstractMap.SimpleImmutableEntry<Integer, Integer> getMinMax(final Element attribute) {
        final var attributeValue = attribute.text().split("-");
        if (attributeValue.length == 0) {
            return new AbstractMap.SimpleImmutableEntry<>(1, 20);
        } else if (attributeValue.length == 1) {
            return new AbstractMap.SimpleImmutableEntry<>(Integer.parseInt(attributeValue[0]), Integer.parseInt(attributeValue[0]));
        } else {
            return new AbstractMap.SimpleImmutableEntry<>(Integer.parseInt(attributeValue[0]), Integer.parseInt(attributeValue[1]));
        }
    }

    private ScoutAttribute buildScoutAttribute(final Attributes attribute, final Element attributeValue) {
        final var minMax = getMinMax(attributeValue);
        return ScoutAttribute.builder()
                .attribute(attribute)
                .min(minMax.getKey())
                .max(minMax.getValue())
                .build();
    }
}
