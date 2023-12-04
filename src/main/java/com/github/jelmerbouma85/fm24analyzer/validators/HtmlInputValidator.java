package com.github.jelmerbouma85.fm24analyzer.validators;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

@Service
public class HtmlInputValidator {

    private final CacheManager cacheManager;

    public HtmlInputValidator(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public HtmlInputValidationResult validateInput(final String user, final MultipartFile squadInput, final MultipartFile tacticInput) {
        final var squadAvailable =  !squadInput.isEmpty() || isSquadAvailable(user);
        final var tacticAvailable =  !tacticInput.isEmpty() || isTacticAvailable(user);
        return HtmlInputValidationResult.builder()
                .squadAvailable(squadAvailable)
                .tacticAvailable(tacticAvailable)
                .build();
    }

    private boolean isSquadAvailable(final String user) {
        return Optional.ofNullable(Objects.requireNonNull(cacheManager.getCache("squadCache")).get(user)).isPresent();
    }

    private boolean isTacticAvailable(final String user) {
        return Optional.ofNullable(Objects.requireNonNull(cacheManager.getCache("tacticCache")).get(user)).isPresent();
    }
}
