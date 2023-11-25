package com.github.jelmerbouma85.fm24analyzer.config;

import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    public int getSquadPlayerNameLocation() {
        return Optional.ofNullable(squadDataLocations.get("Player"))
                .orElseGet(() -> {
                    final var index = squadAttributeTableHeaders.indexOf(squadAttributeTableHeaders.stream()
                            .filter(element -> element.text()
                                    .replaceAll("<th>", "")
                                    .replaceAll("</th>","")
                                    .equalsIgnoreCase("Player"))
                            .findFirst()
                            .orElseThrow());
                    squadDataLocations.put("Player", index);
                    return index;
                });
    }

    public int getScoutPlayerNameLocation() {
        return Optional.ofNullable(squadDataLocations.get("Name"))
                .orElseGet(() -> {
                    final var index = scoutAttributeTableHeaders.indexOf(scoutAttributeTableHeaders.stream()
                            .filter(element -> element.text()
                                    .replaceAll("<th>", "")
                                    .replaceAll("</th>","")
                                    .equalsIgnoreCase("Name"))
                            .findFirst()
                            .orElseThrow());
                    squadDataLocations.put("Name", index);
                    return index;
                });
    }

    public int getPlayerAgeLocation() {
        return Optional.ofNullable(squadDataLocations.get("Age"))
                .orElseGet(() -> {
                    final var index = squadAttributeTableHeaders.indexOf(squadAttributeTableHeaders.stream()
                            .filter(element -> element.text()
                                    .replaceAll("<th>", "")
                                    .replaceAll("</th>","")
                                    .equalsIgnoreCase("Age"))
                            .findFirst()
                            .orElseThrow());
                    squadDataLocations.put("Age", index);
                    return index;
                });
    }

    public int getSquadPlayablePositions() {
        return Optional.ofNullable(squadDataLocations.get("Position"))
                .orElseGet(() -> {
                    final var index = squadAttributeTableHeaders.indexOf(squadAttributeTableHeaders.stream()
                            .filter(element -> element.text()
                                    .replaceAll("<th>", "")
                                    .replaceAll("</th>","")
                                    .equalsIgnoreCase("Position"))
                            .findFirst()
                            .orElseThrow());
                    squadDataLocations.put("Position", index);
                    return index;
                });
    }

    public int getScoutPlayablePositions() {
        return Optional.ofNullable(scoutDataLocations.get("Position"))
                .orElseGet(() -> {
                    final var index = scoutAttributeTableHeaders.indexOf(scoutAttributeTableHeaders.stream()
                            .filter(element -> element.text()
                                    .replaceAll("<th>", "")
                                    .replaceAll("</th>","")
                                    .equalsIgnoreCase("Position"))
                            .findFirst()
                            .orElseThrow());
                    scoutDataLocations.put("Position", index);
                    return index;
                });
    }

    public int getScoutTransferValue() {
        return Optional.ofNullable(scoutDataLocations.get("Transfer Value"))
                .orElseGet(() -> {
                    final var index = scoutAttributeTableHeaders.indexOf(scoutAttributeTableHeaders.stream()
                            .filter(element -> element.text()
                                    .replaceAll("<th>", "")
                                    .replaceAll("</th>","")
                                    .equalsIgnoreCase("Transfer Value"))
                            .findFirst()
                            .orElseThrow());
                    scoutDataLocations.put("Transfer Value", index);
                    return index;
                });
    }

    public int getScoutSalary() {
        return Optional.ofNullable(scoutDataLocations.get("Salary"))
                .orElseGet(() -> {
                    final var index = scoutAttributeTableHeaders.indexOf(scoutAttributeTableHeaders.stream()
                            .filter(element -> element.text()
                                    .replaceAll("<th>", "")
                                    .replaceAll("</th>","")
                                    .equalsIgnoreCase("Salary"))
                            .findFirst()
                            .orElseThrow());
                    scoutDataLocations.put("Salary", index);
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
