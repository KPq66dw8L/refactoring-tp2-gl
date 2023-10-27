
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import static org.approvaltests.Approvals.verify;

public class StatementPrinterTests {

    @Test
    void exampleStatementTXT() throws IOException {

        HashMap<String, Play> plays = new HashMap<>();
        plays.put("hamlet", new Tragedy("Hamlet"));
        plays.put("as-like", new Comedy("As You Like It"));
        plays.put("othello", new Tragedy("Othello"));

        Invoice invoice = new Invoice("BigCo", List.of(
                new Performance("hamlet", 55),
                new Performance("as-like", 35),
                new Performance("othello", 40)));

        StatementPrinter statementPrinter = new StatementPrinter();
        String result = statementPrinter.printTXT(invoice, plays);

        verify(result);
    }

    @Test
    void exampleStatementHTML() throws IOException, Exception {

        HashMap<String, Play> plays = new HashMap<>();
        plays.put("hamlet", new Tragedy("Hamlet"));
        plays.put("as-like", new Comedy("As You Like It"));
        plays.put("othello", new Tragedy("Othello"));

        Invoice invoice = new Invoice("BigCo", List.of(
                new Performance("hamlet", 55),
                new Performance("as-like", 35),
                new Performance("othello", 40)));

        StatementPrinter statementPrinter = new StatementPrinter();
        statementPrinter.printHTML(invoice, plays);

        // Read the approved HTML content.
        String result = Files.readString(Paths.get("src/test/java/StatementPrinterTests.exampleStatementHTML.approved.txt"));

        verify(result);
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
