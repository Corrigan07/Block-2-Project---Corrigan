package com.zork;

public class Food implements PurchasableItem {

  private String name;
  private String description;
  private boolean isVisible;
  private boolean isCollectible;
  private int price;

  public Food(
    String name,
    String description,
    boolean isCollectible,
    boolean isVisible,
    int price
  ) {
    this.name = name;
    this.description = description;
    this.isCollectible = isCollectible;
    this.isVisible = isVisible;
    this.price = price;
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
