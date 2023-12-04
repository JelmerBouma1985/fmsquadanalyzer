package com.github.jelmerbouma85.fm24analyzer.validators;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HtmlInputValidationResult {

    private boolean squadAvailable;
    private boolean tacticAvailable;
}
