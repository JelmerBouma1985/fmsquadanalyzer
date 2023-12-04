package com.github.jelmerbouma85.fm24analyzer.readers;

import com.github.jelmerbouma85.fm24analyzer.domain.TacticPosition;
import com.github.jelmerbouma85.fm24analyzer.domain.enums.Duty;
import com.github.jelmerbouma85.fm24analyzer.domain.enums.Position;
import com.github.jelmerbouma85.fm24analyzer.domain.enums.Role;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Component
public class TacticReader {

    @Cacheable(cacheNames = "tacticCache", key = "#user")
    public List<TacticPosition> readTacticFromHtml(final MultipartFile tacticInputStream, final String user) {
        try {
            final var document = Jsoup.parse(tacticInputStream.getInputStream(), StandardCharsets.UTF_8.name(), "www.fm24analyzer.com");
            final var table = document.selectFirst("table");
            final var rows = Objects.requireNonNull(table, "Er is geen table gevonden").select("tr");
            final List<TacticPosition> tacticalPositions = new ArrayList<>();
            for (Element row : rows) {
                final var tableData = row.select("td");
                if (isPlayablePosition(tableData, rows)) {
                    var tacticPosition = TacticPosition.builder()
                            .position(Position.valueOf(tableData.get(getHeaderIndex("Position Selected", rows)).text()))
                            .role(getRole(tableData.get(getHeaderIndex("Position/Role/Duty", rows))))
                            .duty(getDuty(tableData.get(getHeaderIndex("Position/Role/Duty", rows))))
                            .build();
                    tacticalPositions.add(tacticPosition);
                }
            }
            return tacticalPositions;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Role getRole(final Element tableData) {
        return Role.fromTacticalName(tableData.text().split("\\(")[0].trim());
    }

    private Duty getDuty(final Element tableData) {
        return Duty.valueOf(tableData.text().split("\\(")[1].replace(")", "").trim().toUpperCase(Locale.ROOT));
    }

    private  boolean isPlayablePosition(final Elements tableData, final Elements headers) {
        if(tableData.isEmpty()) {
            return false;
        }
        final String position = tableData.get(getHeaderIndex("Position Selected", headers)).text();
        return !position.isBlank()
                && !position.matches("S(\\d+)")
                && !position.trim().equalsIgnoreCase("-");

    }
    private int getHeaderIndex(final String name, final Elements rows) {
        final var headers = rows.select("th");
        return headers.indexOf(headers.stream().filter(header -> header.text().equalsIgnoreCase(name)).findFirst().orElseThrow());
    }
}
