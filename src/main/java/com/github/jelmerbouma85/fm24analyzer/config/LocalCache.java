package com.github.jelmerbouma85.fm24analyzer.config;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

public class LocalCache {

    private final Map<String, Integer> squadDataLocations = new HashMap<>();
    private final Map<String, Integer> scoutDataLocations = new HashMap<>();
    private Elements squadAttributeTableHeaders;
    private Elements scoutAttributeTableHeaders;

    public int getSquadPlayerAttributeLocation(final String attribute) {
        return Optional.ofNullable(squadDataLocations.get(attribute))
                .orElseGet(() -> {
                    final var index = squadAttributeTableHeaders.indexOf(squadAttributeTableHeaders.stream()
                            .filter(element -> element.text()
                                    .replaceAll("<th>", "")
                                    .replaceAll("</th>","")
                                    .equalsIgnoreCase(attribute))
                            .reduce((first, second) -> second)
                            .orElseThrow());
                    squadDataLocations.put(attribute, index);
                    return index;
                });
    }

    public int getScoutPlayerAttributeLocation(final String attribute) {
        return Optional.ofNullable(scoutDataLocations.get(attribute))
                .orElseGet(() -> {
                    final var index = scoutAttributeTableHeaders.indexOf(scoutAttributeTableHeaders.stream()
                            .filter(element -> element.text()
                                    .replaceAll("<th>", "")
                                    .replaceAll("</th>","")
                                    .equalsIgnoreCase(attribute))
                            .reduce((first, second) -> second)
                            .orElseThrow());
                    scoutDataLocations.put(attribute, index);
                    return index;
                });
    }

    public void setSquadHeaderElements(final Elements headers) {
        this.squadAttributeTableHeaders = headers;
    }
    public void setScoutHeaderElements(final Elements headers) {
        this.scoutAttributeTableHeaders = headers;
    }
}
