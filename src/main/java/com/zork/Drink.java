package com.zork;

import java.util.ArrayList;
import java.util.List;

public class Drink implements PurchasableItem {

  private String name;
  private String description;
  private String location;
  private int id;
  private boolean isVisible;
  private boolean isCollectible;
  private List<Item> drinks;
  private int price;

  public Drink(
    String name,
    String description,
    int id,
    boolean isCollectible,
    boolean isVisible,
    int price
  ) {
    this.name = name;
    this.description = description;
    this.id = id;
    this.isCollectible = isCollectible;
    this.isVisible = isVisible;
    this.price = price;
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

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getLocation() {
    return null;
  }

  @Override
  public void setLocation(String location) {
    this.location = location;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public boolean isVisible() {
    return isVisible;
  }

  @Override
  public void setVisible(boolean visible) {
    this.isVisible = visible;
  }

  @Override
  public boolean isCollectible() {
    return isCollectible;
  }

  @Override
  public void setCollectible(boolean collectible) {
    this.isCollectible = collectible;
  }

  @Override
  public int getPrice() {
    return price;
  }
}
