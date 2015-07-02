package com.sathy.evlo.data;

/**
 * Created by sathy on 27/6/15.
 */
public class Income extends TableEntity {

    public static final String TableName = "income";
    public static final String IncomeDate = "income_date";
    public static final String Amount = "amount";
    public static final String Source = "source";
    public static final String Notes = "notes";

    public static final String[] Columns = new String[]{Id, IncomeDate, Amount, Source, Notes};

    private String incomeDate;
    private double amount;
    private int source;
    private String notes;

    public Income() {
        super();
    }

    public String getIncomeDate() {
        return incomeDate;
    }

    public void setIncomeDate(String expenseDate) {
        this.incomeDate = expenseDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }
}
