//import org.junit.Test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import static org.approvaltests.Approvals.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatementPrinterTests {

    @Test
    void exampleStatement() throws IOException, Exception {

        HashMap<String, Play> plays = new HashMap<>();
        plays.put("hamlet", new Tragedy("Hamlet"));
        plays.put("as-like", new Comedy("As You Like It"));
        plays.put("othello", new Tragedy("Othello"));

        Invoice invoice = new Invoice("BigCo", List.of(
                new Performance("hamlet", 55),
                new Performance("as-like", 35),
                new Performance("othello", 40)));

        StatementPrinter statementPrinter = new StatementPrinter();
        statementPrinter.print(invoice, plays);

        // Read the generated HTML content.
        String generatedContent = Files.readString(Paths.get("build/results/invoice.html"));

        // Read the approved HTML content.
        String approvedContent = Files.readString(Paths.get("src/test/java/StatementPrinterTests.exampleStatement.approved.html"));

        String cleanedApprovedContent = approvedContent.replaceAll("[^a-zA-Z0-9]", "");
        String cleanedGeneratedContent = generatedContent.replaceAll("[^a-zA-Z0-9]", "");

        assertEquals(cleanedApprovedContent, cleanedGeneratedContent);
    }

//    @Test
//    void statementWithNewPlayTypes() {
//
//        HashMap<String, Play> plays = new HashMap<>();
//        plays.put("henry-v",  new Play("Henry V", "history"));
//        plays.put("as-like",  new Play("As You Like It", "pastoral"));
//
//        Invoice invoice = new Invoice("BigCo", List.of(
//                new Performance("henry-v", 53),
//                new Performance("as-like", 55)));
//
//        StatementPrinter statementPrinter = new StatementPrinter();
//        Assertions.assertThrows(Error.class, () -> {
//            statementPrinter.print(invoice, plays);
//        });
//    }
}
