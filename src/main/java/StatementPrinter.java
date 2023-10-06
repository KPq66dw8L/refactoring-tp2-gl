import java.text.NumberFormat;
import java.util.*;

public class StatementPrinter {
//TODO: .ftl freemarker for HTML
  public String print(Invoice invoice, HashMap<String, Play> plays) {
    int totalAmount = 0;
    int volumeCredits = 0;
    String result = String.format("Statement for %s\n", invoice.customer);

    NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);

    for (Performance perf : invoice.performances) {
      Play play = plays.get(perf.playID);
      int priceToPay = 0;

      switch (play.type) {
        case "tragedy":
          priceToPay = 400;
          if (perf.audience > 30) {
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
      totalAmount += priceToPay;
    }
    result += String.format("Amount owed is %s\n", frmt.format(totalAmount));
    result += String.format("You earned %s credits\n", volumeCredits);
    return result;
  }

}
