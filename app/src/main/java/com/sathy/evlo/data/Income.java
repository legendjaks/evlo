package com.sathy.evlo.data;

import android.content.ContentValues;

/**
 * Created by sathy on 27/6/15.
 */
public class Income extends TableEntity {

    public static final String TableName = "incomes";
    public static final String IncomeDate = "incomedate";
    public static final String Amount = "amount";
    public static final String Notes = "notes";

    private String incomeDate;
    private double amount;
    private String notes;

    public Income() {
        super();
    }

    public Income(String incomeDate, double amount, String notes) {
        this(0, incomeDate, amount, notes);
    }

    public Income(long id, String expenseDate, double amount, String notes) {
        this.id = id;
        this.incomeDate = expenseDate;
        this.amount = amount;
        this.notes = notes;
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

    @Override
    public ContentValues getContentValues() {

        ContentValues values = new ContentValues();
        values.put(IncomeDate, incomeDate.trim());
        values.put(Amount, amount);
        values.put(Notes, notes.trim());
        return values;
    }
}
