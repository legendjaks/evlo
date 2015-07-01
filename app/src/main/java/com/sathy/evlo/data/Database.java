package com.sathy.evlo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sathy on 27/6/15.
 */
public class Database extends SQLiteOpenHelper {

    private static final String CreateTable = "Create Table ";
    private static final String DB = "evlo.db";

    public Database(Context context) {
        super(context, DB, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        String createTag = CreateTable + Tag.TableName
                + "(_id integer primary key autoincrement, tag_order integer not null,  name text not null)";
        String createSource = CreateTable + Source.TableName
                + "(_id integer primary key autoincrement, name text not null)";
        /*String createExpenses = CreateTable
                + Expense.TableName
                + "(_id integer primary key autoincrement, expensedate integer not null, paymenttype integer not null, amount real not null, comments text)";
        String createExpenseTags = CreateTable
                + ExpenseTag.TableName
                + "(expenseId integer not null, tagId integer not null, constraint expenseTags_pk primary key(expenseId, tagId))";*/
        String createIncome = CreateTable
                + Income.TableName
                + "(_id integer primary key autoincrement, income_date integer not null, amount real not null, source integer not null, notes text)";

        database.execSQL(createTag);
        database.execSQL(createSource);
        /*
        database.execSQL(createExpenses);
        database.execSQL(createExpenseTags);*/
        database.execSQL(createIncome);

        database.execSQL("INSERT INTO source (name) VALUES ('CASH') ");
        database.execSQL("INSERT INTO source (name) VALUES ('CREDIT CARD') ");
        database.execSQL("INSERT INTO source (name) VALUES ('DEBIT CARD') ");
        database.execSQL("INSERT INTO source (name) VALUES ('CHEQUE') ");
        database.execSQL("INSERT INTO source (name) VALUES ('WIRE TRANSFER') ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    }
}
