package com.github.jelmerbouma85.fm24analyzer.domain.scouting;

import com.github.jelmerbouma85.fm24analyzer.domain.enums.Position;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class ScoutPlayer {

    private String name;
    private String age;
    private String transferValue;
    private String salary;

    @ToString.Exclude
    private Set<Position> positions;

    private List<ScoutAttribute> attributes;
}
