package com.sathy.evlo.data;

public class Tag extends TableEntity {

  public static final String TableName = "tag";
  public static final String Name = "name";

  public static final String[] Columns = new String[]{Id, Name};

  private String name;

  public Tag() {
    super();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
