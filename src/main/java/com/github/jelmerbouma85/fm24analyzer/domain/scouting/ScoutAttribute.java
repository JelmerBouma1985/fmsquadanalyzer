package com.github.jelmerbouma85.fm24analyzer.domain.scouting;

import com.github.jelmerbouma85.fm24analyzer.domain.enums.Attributes;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScoutAttribute {

    private Attributes attribute;
    private int min;
    private int max;
}
