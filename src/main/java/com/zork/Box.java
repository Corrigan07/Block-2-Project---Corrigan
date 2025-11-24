package com.zork;

import java.util.ArrayList;

public class Box<T extends Item> implements Item {

  private T value;
  private String name;
  private String description;
  private String location;
  private int id;
  private boolean isVisible;
  private boolean isCollectible;

  public Box(
    String name,
    String description,
    int id,
    boolean isCollectible,
    boolean isVisible
  ) {
    this.name = name;
    this.description = description;
    this.id = id;
    this.isCollectible = isCollectible;
    this.isVisible = isVisible;
  }

  public void setValue(T value) {
    this.value = value;
  }

  public T getValue() {
    return value;
  }

  public String getBoxDescription() {
    return description;
  }

  public String getDescription() {
    if (value instanceof Item) {
      return (getBoxDescription());
    }
    return "A box containing something.";
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
}
