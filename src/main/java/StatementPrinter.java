import java.io.*;
import java.text.NumberFormat;
import java.util.*;
import freemarker.template.*;

public class StatementPrinter {

  private final Configuration cfg;

  public StatementPrinter() {
    cfg = configureFreeMarker();
  }

  public void printHTML(Invoice invoice, HashMap<String, Play> plays) throws IOException, TemplateException {
    Map<String, Object> root = prepareData(invoice, plays);
    toHTML(root);
  }

  public String printTXT(Invoice invoice, HashMap<String, Play> plays) throws IOException{
    Map<String, Object> root = prepareData(invoice, plays);
    return toText(root);
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

  // TODO: put in Invoice smh
  private Map<String, Object> prepareData(Invoice invoice, HashMap<String, Play> plays) {
    Map<String, Object> root = new HashMap<>();
    int totalAmount = 0;
    int volumeCredits = 0;
    root.put("client", invoice.customer);
    List<Map<String, Object>> performancesList = new ArrayList<>();

    for (Performance perf : invoice.performances) {
      Play play = plays.get(perf.playID);
      int priceToPay = play.computePrice(perf.audience);

      volumeCredits += computeVolumeCredits(play, perf);

      Map<String, Object> perfData = new HashMap<>();
      perfData.put("playName", play.name);
      perfData.put("price", priceToPay);
      perfData.put("audience", perf.audience);
      performancesList.add(perfData);
      totalAmount += priceToPay;
    }

    NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);
    root.put("performances", performancesList);
    root.put("totalAmount", frmt.format(totalAmount));
    root.put("fidelityPoints", volumeCredits);

    return root;
  }

  // TODO: put in Invoice smh
  private int computeVolumeCredits(Play play, Performance perf) {
    int volumeCredits = Math.max(perf.audience - 30, 0);
    if ("comedy".equals(play.type)) volumeCredits += Math.floor(perf.audience / 5);
    return volumeCredits;
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

    String filePath = "build/results/invoice.txt";
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      writer.write(result);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }
}
