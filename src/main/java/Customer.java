import java.util.UUID;

public class Customer {

    public String name;
    public UUID clientNumber;
    public int fidelityPoints;

    public Customer(String name) {
        this.name = name;
        this.clientNumber = UUID.randomUUID();
        this.fidelityPoints = 0;
    }
}
