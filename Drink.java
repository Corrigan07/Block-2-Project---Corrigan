import java.util.ArrayList;
import java.util.List;

public class Drink extends Item {

  private List<Item> drinks;

  public Drink(
    String name,
    String description,
    int id,
    boolean isCollectible,
    boolean isVisible
  ) {
    super(name, description, id, isCollectible, isVisible);
    this.drinks = new ArrayList<>();
  }

  public List<Item> getDrinks() {
    return drinks;
  }

  public void addDrink(Item drink) {
    if (drink == null) throw new IllegalArgumentException(
      "drink cannot be null"
    );
    drinks.add(drink);
  }

  public void removeDrink(Item drink) {
    drinks.remove(drink);
  }
}
