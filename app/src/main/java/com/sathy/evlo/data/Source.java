package com.sathy.evlo.data;

/**
 * Created by sathy on 29/6/15.
 */
public class Source extends TableEntity {

  public static final String TableName = "source";
  public static final String Name = "name";
  public static final String[] Columns = new String[]{Id, Name};

  private String name;

  public Source() {
    super();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
