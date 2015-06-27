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

        /*String createTags = CreateTable + Tag.TableName
                + "(_id integer primary key autoincrement, tagOrder integer not null,  name text not null)";
        String createPaymentTypes = CreateTable + PaymentType.TableName
                + "(_id integer primary key autoincrement, name text not null)";
        String createExpenses = CreateTable
                + Expense.TableName
                + "(_id integer primary key autoincrement, expensedate integer not null, paymenttype integer not null, amount real not null, comments text)";
        String createExpenseTags = CreateTable
                + ExpenseTag.TableName
                + "(expenseId integer not null, tagId integer not null, constraint expenseTags_pk primary key(expenseId, tagId))";*/
        String createIncomes = CreateTable
                + Income.TableName
                + "(_id integer primary key autoincrement, incomedate integer not null, amount real not null, notes text)";

        /*database.execSQL(createTags);
        database.execSQL(createPaymentTypes);
        database.execSQL(createExpenses);
        database.execSQL(createExpenseTags);*/
        database.execSQL(createIncomes);

        /*database.execSQL("INSERT INTO paymenttypes (name) VALUES ('Cash') ");
        database.execSQL("INSERT INTO paymenttypes (name) VALUES ('Credit Card') ");
        database.execSQL("INSERT INTO paymenttypes (name) VALUES ('Debit Card') ");
        database.execSQL("INSERT INTO paymenttypes (name) VALUES ('Cheque') ");
        database.execSQL("INSERT INTO paymenttypes (name) VALUES ('Wire Transfer') ");*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    }
}
