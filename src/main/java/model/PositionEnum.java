package model;

public enum PositionEnum {

  GUARD(1),
  FORWARD(3),
  CENTER(5);

  private final long id;

  PositionEnum(long id) {
    this.id = id;
  }

  public long getId(){
    return id;
  }

  public static PositionEnum getById(long id) {

    for (PositionEnum position : PositionEnum.values()) {
      if (position.id == id) {
        return position;
      }
    }

    throw new RuntimeException("The id " + id + " does not match with any postions");
  }

}
