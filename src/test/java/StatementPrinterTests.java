
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import static org.approvaltests.Approvals.verify;

public class StatementPrinterTests {

    @Test
    void exampleStatementTXT() throws IOException, Exception {

        HashMap<String, Play> plays = new HashMap<>();
        plays.put("hamlet", new Tragedy("Hamlet"));
        plays.put("as-like", new Comedy("As You Like It"));
        plays.put("othello", new Tragedy("Othello"));

        Customer bigCo = new Customer("BigCo");

        Invoice invoice = new Invoice(bigCo, List.of(
                new Performance("hamlet", 55),
                new Performance("as-like", 35),
                new Performance("othello", 40)));

        StatementPrinter statementPrinter = new StatementPrinter();
        statementPrinter.generateStatementData(invoice, plays);
        String result = statementPrinter.printTXT();

        verify(result);
    }

    @Test
    void exampleStatementHTML() throws IOException, Exception {

        HashMap<String, Play> plays = new HashMap<>();
        plays.put("hamlet", new Tragedy("Hamlet"));
        plays.put("as-like", new Comedy("As You Like It"));
        plays.put("othello", new Tragedy("Othello"));

        Customer bigCo = new Customer("BigCo");

        Invoice invoice = new Invoice(bigCo, List.of(
                new Performance("hamlet", 55),
                new Performance("as-like", 35),
                new Performance("othello", 40)));

        StatementPrinter statementPrinter = new StatementPrinter();
        statementPrinter.generateStatementData(invoice, plays);
        statementPrinter.printHTML();

        String result = Files.readString(Paths.get("build/results/invoice.html"));

        verify(result);
    }
    @Test
    void promotionAppliedStatementTXT() throws IOException {

        HashMap<String, Play> plays = new HashMap<>();
        plays.put("hamlet", new Tragedy("Hamlet"));
        plays.put("as-like", new Comedy("As You Like It"));
        plays.put("othello", new Tragedy("Othello"));

        Customer bigCo = new Customer("BigCo");
        bigCo.fidelityPoints = 155; // Setting the fidelity points over 150 to apply the promotion

        Invoice invoice = new Invoice(bigCo, List.of(
                new Performance("hamlet", 55),
                new Performance("as-like", 35),
                new Performance("othello", 40)));

        StatementPrinter statementPrinter = new StatementPrinter();
        statementPrinter.generateStatementData(invoice, plays);
        String result = statementPrinter.printTXT();

        verify(result);
    }

    @Test
    void promotionAppliedStatementHTML() throws IOException, Exception {

        HashMap<String, Play> plays = new HashMap<>();
        plays.put("hamlet", new Tragedy("Hamlet"));
        plays.put("as-like", new Comedy("As You Like It"));
        plays.put("othello", new Tragedy("Othello"));

        Customer bigCo = new Customer("BigCo");
        bigCo.fidelityPoints = 155; // Setting the fidelity points over 150 to apply the promotion

        Invoice invoice = new Invoice(bigCo, List.of(
                new Performance("hamlet", 55),
                new Performance("as-like", 35),
                new Performance("othello", 40)));

        StatementPrinter statementPrinter = new StatementPrinter();
        statementPrinter.generateStatementData(invoice, plays);
        statementPrinter.printHTML();

        String result = Files.readString(Paths.get("build/results/invoice.html"));
        verify(result);
    }

}
