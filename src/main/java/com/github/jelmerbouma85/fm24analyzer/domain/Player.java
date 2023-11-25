package com.github.jelmerbouma85.fm24analyzer.domain;

import com.github.jelmerbouma85.fm24analyzer.domain.enums.Attributes;
import com.github.jelmerbouma85.fm24analyzer.domain.enums.Position;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Map;
import java.util.Set;

@Data
@Builder
public class Player {

    private String name;
    private int age;
    private boolean injured;

    @ToString.Exclude
    private Set<Position> positions;

    @ToString.Exclude
    private Map<Attributes, Integer> attributes;
}
