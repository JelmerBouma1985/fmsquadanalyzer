package com.github.jelmerbouma85.fm24analyzer.controllers;

import com.github.jelmerbouma85.fm24analyzer.output.HtmlOutputService;
import com.github.jelmerbouma85.fm24analyzer.readers.PlayerReader;
import com.github.jelmerbouma85.fm24analyzer.readers.TacticReader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;

@Controller
public class FmAnalyzerController {

    private final HtmlOutputService htmlOutputService;

    public FmAnalyzerController(HtmlOutputService htmlOutputService, PlayerReader playerReader, TacticReader tacticReader) {
        this.htmlOutputService = htmlOutputService;
    }

    @GetMapping
    public String getHomePage(Model model) {
        return "index";
    }

    @PostMapping() public String data(Model model, @RequestParam(name = "squadFile") MultipartFile squadFile, @RequestParam(name = "tacticFile") MultipartFile tacticFile, @RequestParam(name = "scoutFile", required = false) MultipartFile scoutFile) throws IOException {
        if(Objects.nonNull(scoutFile)) {
            htmlOutputService.buildTemplate(model, squadFile.getInputStream(), tacticFile.getInputStream(), scoutFile.getInputStream());
        } else {
            htmlOutputService.buildTemplate(model, squadFile.getInputStream(), tacticFile.getInputStream(), InputStream.nullInputStream());
        }
        return "index";
    }
}
