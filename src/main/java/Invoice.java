import java.util.*;
import java.text.NumberFormat;

public class Invoice {

  public Customer customer;
  public List<Performance> performances;
  public boolean promotionApplied = false;

  public Invoice(Customer customer, List<Performance> performances) {
    this.customer = customer;
    this.performances = performances;
  }

  public Map<String, Object> generateDataForStatement(HashMap<String, Play> plays) {
    Map<String, Object> root = new HashMap<>();
    int totalAmount = 0;
    int volumeCredits = 0;
    root.put("client", customer.name);
    List<Map<String, Object>> performancesList = new ArrayList<>();

    for (Performance perf : performances) {
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

    int totalFidelityPoints = customer.fidelityPoints + volumeCredits;  // Calculate total fidelity points before applying any promotions

    if (totalFidelityPoints > 150) {
      totalAmount -= 15;
      volumeCredits = totalFidelityPoints - 150;
      this.promotionApplied = true;
    } else {
      customer.fidelityPoints = totalFidelityPoints;
    }


    NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);
    root.put("performances", performancesList);
    root.put("totalAmount", frmt.format(totalAmount));
    root.put("fidelityPoints", volumeCredits);
    root.put("promotionApplied", promotionApplied);

    return root;
  }

  private int computeVolumeCredits(Play play, Performance perf) {
    int volumeCredits = Math.max(perf.audience - 30, 0);
    if ("comedy".equals(play.type)) volumeCredits += Math.floor(perf.audience / 5);
    return volumeCredits;
  }
}
