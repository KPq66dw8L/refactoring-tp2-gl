public class Tragedy extends Play{

    public Tragedy(String name) {
        super(name, "tragedy");
    }


    @Override
    public int computePrice(int audience) {
        int priceToPay = 400;
        if (audience > 30) {
            priceToPay += 10 * (audience - 30);
        }
        return priceToPay;
    }
}
