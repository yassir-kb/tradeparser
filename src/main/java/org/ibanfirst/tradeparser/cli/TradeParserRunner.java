package org.ibanfirst.tradeparser.cli;

import org.ibanfirst.tradeparser.model.TradeConfirmation;
import org.ibanfirst.tradeparser.service.TradeParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The TradeParserRunner class is a component that implements the ApplicationRunner interface.
 * It is responsible for parsing email content and generating a TradeConfirmation object.
 */
@Component
public class TradeParserRunner implements ApplicationRunner {

    /**
     * The TradeParserService instance is used to parse email content and generate a TradeConfirmation object.
     */
    @Autowired
    private TradeParserService tradeParserService;

    /**
     * The run method is invoked by the Spring Boot application context when the application starts.
     * It takes an ApplicationArguments object as a parameter, which contains the command line arguments passed to the application.
     * In this case, the only argument is the path to the email file.
     * The method first checks if the number of arguments is correct. If not, it displays an error message and exits the application.
     * If the argument is correct, the method reads the email content from the file, invokes the TradeParserService to parse the content,
     * and then prints the TradeConfirmation object as JSON.
     */
    @Override
    public void run(ApplicationArguments args) {
        String[] nonOptionArgs = args.getSourceArgs();

        if (nonOptionArgs.length!= 1) {
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