import java.io.*;
import java.text.NumberFormat;
import java.util.*;

import freemarker.template.*;

public class StatementPrinter {
  public String print(Invoice invoice, HashMap<String, Play> plays) throws IOException, TemplateException {

    // Configuration de freemarker ----------------------------------
    Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
    cfg.setDirectoryForTemplateLoading(new File("src/ressources/templates"));
    cfg.setDefaultEncoding("UTF-8");
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    cfg.setLogTemplateExceptions(false);
    cfg.setWrapUncheckedExceptions(true);
    cfg.setSQLDateAndTimeTimeZone(TimeZone.getDefault());

    // Create the root hash. We use a Map here, but it could be a JavaBean too.
    Map<String, Object> root = new HashMap<>();
    // Fin de configuration de freemarker ---------------------------

    int totalAmount = 0;
    int volumeCredits = 0;
    String result = String.format("Statement for %s\n", invoice.customer);

    NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);
    List<Map<String, Object>> performancesList = new ArrayList<>();
    for (Performance perf : invoice.performances) {
      Play play = plays.get(perf.playID);
      int priceToPay = 0;

      switch (play.type) {
        case "tragedy":
          priceToPay = 400; //TODO: cette valeur devrait être dans l'objet tragedy
          if (perf.audience > 30) { //TODO: cette opération devrait être dans l'objet tragedy
            priceToPay += 10 * (perf.audience - 30);
          }
          break;
        case "comedy":
          priceToPay = 300;
          if (perf.audience > 20) {
            priceToPay += 100 + 5 * (perf.audience - 20);
          }
          priceToPay += 3 * perf.audience;
          break;
        default:
          throw new Error("unknown type: ${play.type}");
      }

      // add volume credits
      volumeCredits += Math.max(perf.audience - 30, 0);
      // add extra credit for every ten comedy attendees
      if ("comedy".equals(play.type)) volumeCredits += Math.floor(perf.audience / 5);

      // print line for this order
      result += String.format("  %s: %s (%s seats)\n", play.name, frmt.format(priceToPay), perf.audience);
      Map<String, Object> perfData = new HashMap<>();
      perfData.put("playName", play.name);
      perfData.put("price", priceToPay);
      perfData.put("audience", perf.audience);
      performancesList.add(perfData);
      totalAmount += priceToPay;
    }
    root.put("performances", performancesList);

    result += String.format("Amount owed is %s\n", frmt.format(totalAmount));
    result += String.format("You earned %s credits\n", volumeCredits);
    root.put("totalAmount", frmt.format(totalAmount));
    root.put("fidelityPoints", frmt.format(volumeCredits));

    Template temp = cfg.getTemplate("test.ftlh");
    Writer out = new FileWriter(new File("build/results/invoice.html"));
    temp.process(root, out);

    return result;
  }

}
