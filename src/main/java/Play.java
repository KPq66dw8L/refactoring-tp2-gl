public abstract class Play {

  public String name;
  public String type;

  public Play(String name, String type) {
    this.name = name;
    this.type = type;
  }

  public abstract int computePrice(int audience);
}
