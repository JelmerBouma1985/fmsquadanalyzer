package com.github.jelmerbouma85.fm24analyzer;

import com.github.jelmerbouma85.fm24analyzer.analyzers.ScoutPlayerAnalyzer;
import com.github.jelmerbouma85.fm24analyzer.analyzers.SquadAnalyzer;
import com.github.jelmerbouma85.fm24analyzer.output.HtmlOutputService;
import com.github.jelmerbouma85.fm24analyzer.readers.ScoutingReader;
import com.github.jelmerbouma85.fm24analyzer.readers.TacticReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Fm24analyzerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Fm24analyzerApplication.class)
                .run(args);
    }
}
