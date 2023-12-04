package com.github.jelmerbouma85.fm24analyzer.controllers;

import com.github.jelmerbouma85.fm24analyzer.service.HtmlOutputService;
import com.github.jelmerbouma85.fm24analyzer.readers.PlayerReader;
import com.github.jelmerbouma85.fm24analyzer.readers.TacticReader;
import com.github.jelmerbouma85.fm24analyzer.validators.HtmlInputValidator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

@Controller
public class FmAnalyzerController {

    private final HtmlOutputService htmlOutputService;
    private final HtmlInputValidator htmlInputValidator;

    public FmAnalyzerController(HtmlOutputService htmlOutputService, PlayerReader playerReader, TacticReader tacticReader, HtmlInputValidator htmlInputValidator) {
        this.htmlOutputService = htmlOutputService;
        this.htmlInputValidator = htmlInputValidator;
    }

    @GetMapping
    public String getHomePage(HttpServletResponse response, Model model, @CookieValue(value = "user", defaultValue = "") String user) {
        if (user.isBlank()) {
            final var cookie = new Cookie("user", UUID.randomUUID().toString());
            response.addCookie(cookie);
        }
        return "index";
    }

    @PostMapping()
    public String data(
            Model model,
            @RequestParam(name = "squadFile", required = false) MultipartFile squadFile,
            @RequestParam(name = "tacticFile", required = false) MultipartFile tacticFile,
            @RequestParam(name = "scoutFile", required = false) MultipartFile scoutFile,
            @CookieValue(value = "user") String user) throws IOException {

        final var validationResult = htmlInputValidator.validateInput(user, squadFile, tacticFile);
        if (validationResult.isSquadAvailable() && validationResult.isTacticAvailable()) {
            final var scoutInputStream = Objects.nonNull(scoutFile) ? scoutFile.getInputStream() : InputStream.nullInputStream();
            htmlOutputService.buildTemplate(user, model, squadFile, tacticFile, scoutInputStream);
            return "index";
        } else {
            return "error";
        }
    }
}
