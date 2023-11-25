package com.github.jelmerbouma85.fm24analyzer.domain;

import com.github.jelmerbouma85.fm24analyzer.domain.enums.Duty;
import com.github.jelmerbouma85.fm24analyzer.domain.enums.Position;
import com.github.jelmerbouma85.fm24analyzer.domain.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TacticPosition {

    private Position position;
    private Role role;
    private Duty duty;
}
