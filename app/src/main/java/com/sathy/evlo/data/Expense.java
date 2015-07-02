package com.sathy.evlo.data;

public class Expense extends TableEntity {

    public static final String TableName = "expense";
    public static final String ExpenseDate = "expense_date";
    public static final String SourceId = "source_id";
    public static final String Amount = "amount";
    public static final String Notes = "notes";

    public static final String[] Columns = new String[]{Id, ExpenseDate, SourceId, Amount, Notes};

    private String expenseDate;
    private long sourceId;
    private double amount;
    private String notes;

    public Expense() {
        super();
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate = expenseDate;
    }

    public long getSourceId() {
        return sourceId;
    }

    public void setSourceId(long sourceId) {
        this.sourceId = sourceId;
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
}