package com.github.jelmerbouma85.fm24analyzer.readers;

import com.github.jelmerbouma85.fm24analyzer.domain.TacticPosition;
import com.github.jelmerbouma85.fm24analyzer.domain.enums.Duty;
import com.github.jelmerbouma85.fm24analyzer.domain.enums.Position;
import com.github.jelmerbouma85.fm24analyzer.domain.enums.Role;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Component
public class TacticReader {

    public List<TacticPosition> readTacticFromHtml(final InputStream tacticInputStream) {
        try {
            final var document = Jsoup.parse(tacticInputStream, StandardCharsets.UTF_8.name(), "www.fm24analyzer.com");
            final var table = document.selectFirst("table");
            final var rows = Objects.requireNonNull(table, "Er is geen table gevonden").select("tr");
            final List<TacticPosition> tacticalPositions = new ArrayList<>();
            for (Element row : rows) {
                final var tableData = row.select("td");
                if (!tableData.isEmpty() && !tableData.get(0).text().isBlank() && !tableData.get(0).text().matches("S(\\d+)")) {
                    var tacticPosition = TacticPosition.builder()
                            .position(Position.valueOf(tableData.get(0).text()))
                            .role(getRole(tableData.get(1)))
                            .duty(getDuty(tableData.get(1)))
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
}
