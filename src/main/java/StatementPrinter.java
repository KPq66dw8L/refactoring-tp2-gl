import java.io.*;
import java.text.NumberFormat;
import java.util.*;
import freemarker.template.*;

public class StatementPrinter {

  private final Configuration cfg;
  private Map<String, Object> statementData;

  public StatementPrinter() {
    cfg = configureFreeMarker();
  }

  public void printHTML() throws IOException, TemplateException {
    if (this.statementData == null) {
      throw new IllegalStateException("Statement data has not been generated. Call generateStatementData() first.");
    }
    toHTML(this.statementData);
  }

  public String printTXT() throws IOException {
    if (this.statementData == null) {
      throw new IllegalStateException("Statement data has not been generated. Call generateStatementData() first.");
    }
    return toText(this.statementData);
  }

  private Configuration configureFreeMarker() {
    Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
    try {
      cfg.setDirectoryForTemplateLoading(new File("src/ressources/templates"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    cfg.setDefaultEncoding("UTF-8");
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    cfg.setLogTemplateExceptions(false);
    cfg.setWrapUncheckedExceptions(true);
    cfg.setSQLDateAndTimeTimeZone(TimeZone.getDefault());

    return cfg;
  }

  public void generateStatementData(Invoice invoice, HashMap<String, Play> plays) {
    this.statementData = invoice.generateDataForStatement(plays);
  }


  private void toHTML(Map<String, Object> root) throws IOException, TemplateException {
    Template temp = cfg.getTemplate("test.ftlh");
    Writer out = new FileWriter(new File("build/results/invoice.html"));
    temp.process(root, out);
  }

  private String toText(Map<String, Object> root){
    NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);
    String result = String.format("Statement for %s\n", root.get("client"));
    List<Map<String, Object>> retrievedPerformancesList = (List<Map<String, Object>>) root.get("performances");
    for (Map<String, Object> performanceData : retrievedPerformancesList) {
      result += String.format("  %s: %s (%s seats)\n", performanceData.get("playName"), frmt.format(performanceData.get("price")), performanceData.get("audience"));
    }
    result += String.format("Amount owed is %s\n", root.get("totalAmount"));
    result += String.format("You earned %s credits\n", root.get("fidelityPoints"));
    if ((Boolean) root.get("promotionApplied")) {
      result += "A promotion of $15 has been applied to your invoice due to your fidelity points.\n";
    }

    String filePath = "build/results/invoice.txt";
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      writer.write(result);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }
}
