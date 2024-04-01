package org.ibanfirst.tradeparser.cli;

import org.ibanfirst.tradeparser.model.TradeConfirmation;
import org.ibanfirst.tradeparser.service.TradeParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class TradeParserRunner implements ApplicationRunner {

    @Autowired
    private TradeParserService tradeParserService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String[] nonOptionArgs = args.getSourceArgs();

        if (nonOptionArgs.length != 1) {
            System.err.println("Usage: bin/console app:email:parser <path_to_email_file>");
            System.exit(1);
        }

        String filePath = nonOptionArgs[0];
        try {
            String emailContent = new String(Files.readAllBytes(Paths.get(filePath)));
            TradeConfirmation tradeConfirmation = tradeParserService.parseEmailContent(emailContent);
            System.out.println(tradeConfirmation.toJson());
        } catch (Exception e) {
            System.err.println("Error parsing email: " + e.getMessage());
            System.exit(1);
        }
    }
}

