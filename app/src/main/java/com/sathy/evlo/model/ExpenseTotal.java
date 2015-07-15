package com.sathy.evlo.model;

/**
 * Created by sathy on 14/07/15.
 */
public class ExpenseTotal {

  private String tag;
  private String total;

  public ExpenseTotal() {
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public String getTotal() {
    return total;
  }

  public void setTotal(String total) {
    this.total = total;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ExpenseTotal that = (ExpenseTotal) o;

    return !(tag != null ? !tag.equals(that.tag) : that.tag != null);

  }

  @Override
  public int hashCode() {
    return tag != null ? tag.hashCode() : 0;
  }

  @Override
  public String toString() {
    return tag;
  }
}
