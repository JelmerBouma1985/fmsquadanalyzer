package com.github.jelmerbouma85.fm24analyzer.service;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class CacheService {

    private final CacheManager cacheManager;

    public CacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void evictCaches(final String user, final MultipartFile squadInput, final MultipartFile tacticInput) {
        if (!squadInput.isEmpty()) {
            Optional.ofNullable(cacheManager.getCache("squadCache")).ifPresent(cache -> cache.evict(user));
        }
        if (!tacticInput.isEmpty()) {
            Optional.ofNullable(cacheManager.getCache("tacticCache")).ifPresent(cache -> cache.evict(user));
        }
    }
}
