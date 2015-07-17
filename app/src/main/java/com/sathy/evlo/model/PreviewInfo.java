package com.sathy.evlo.model;

/**
 * Created by sathy on 16/07/15.
 */
public class PreviewInfo {

  private int income;
  private int expense;

  public PreviewInfo() {
  }

  public PreviewInfo(int income, int expense) {
    this.income = income;
    this.expense = expense;
  }

  public int getIncome() {
    return income;
  }

  public void setIncome(int income) {
    this.income = income;
  }

  public int getExpense() {
    return expense;
  }

  public void setExpense(int expense) {
    this.expense = expense;
  }
}
