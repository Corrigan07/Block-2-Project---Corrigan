import java.io.Serializable;

public interface Item extends Serializable {
  //public Item(
  //String name,
  //String description,
  //int id,
  //boolean isCollectible,
  //boolean isVisible
  //) {
  //this.name = name;
  //this.description = description;
  //this.isVisible = true;
  //this.id = id;
  //this.isCollectible = isCollectible;
  //this.isVisible = isVisible;
  //}

  public String getDescription();

  public void setDescription(String description);

  public String getName();

  public void setName(String name);

  public String getLocation();

  public void setLocation(String location);

  public int getId();

  public void setId(int id);

  public boolean isVisible();

  public void setVisible(boolean visible);

  public boolean isCollectible();

  public void setCollectible(boolean collectible);
}
