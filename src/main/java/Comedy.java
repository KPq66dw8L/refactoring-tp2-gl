public class Comedy extends Play{

    public Comedy(String name) {
        super(name, "comedy");
    }

    @Override
    public int computePrice(int audience) {
        int priceToPay = 300;
        if (audience > 20) {
            priceToPay += 100 + 5 * (audience - 20);
        }
        priceToPay += 3 * audience;
        return priceToPay;
    }
}
