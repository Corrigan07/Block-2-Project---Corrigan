package com.zork;

public class Weapon implements Item {

  private String name;
  private String description;
  private int damage;
  private boolean isVisible;
  private boolean isCollectible;

  public Weapon(
    String name,
    String description,
    int damage,
    boolean isVisible,
    boolean isCollectible
  ) {
    this.name = name;
    this.description = description;
    this.damage = damage;
    this.isVisible = isVisible;
    this.isCollectible = isCollectible;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDescription() {
    return description;
  }

  public int getDamage() {
    return damage;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean isVisible() {
    return isVisible;
  }

  @Override
  public boolean isCollectible() {
    return isCollectible;
  }

  @Override
  public void setVisible(boolean visible) {
    this.isVisible = visible;
  }

  @Override
  public void setCollectible(boolean collectible) {
    this.isCollectible = collectible;
  }

  public int attack(Character character) {
    System.out.println(
      character.getName() +
      " attacks with the " +
      this.getName() +
      " dealing " +
      this.getDamage() +
      " damage."
    );
    return this.getDamage();
  }
}
