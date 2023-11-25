package com.github.jelmerbouma85.fm24analyzer.config;

import com.github.jelmerbouma85.fm24analyzer.domain.enums.Attributes;
import com.github.jelmerbouma85.fm24analyzer.domain.enums.Duty;
import com.github.jelmerbouma85.fm24analyzer.domain.enums.Position;
import com.github.jelmerbouma85.fm24analyzer.domain.enums.Role;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "fm24")
public class AttributeProperties {

    private List<AttributesForPositionRoleAndDuty> positions = new ArrayList<>();

    @Data
    public static class AttributesForPositionRoleAndDuty {
        private List<Position> position;
        private Duty duty;
        private Role role;
        private List<Attributes> primary = new ArrayList<>();
        private List<Attributes> secondary = new ArrayList<>();
    }
}
