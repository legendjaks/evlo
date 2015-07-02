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
        String createExpense = CreateTable
                + Expense.TableName
                + "(_id integer primary key autoincrement, expense_date integer not null, source_id integer not null, amount real not null, notes text)";
        String createExpenseTag = CreateTable
                + ExpenseTag.TableName
                + "(expense_id integer not null, tag_id integer not null, constraint expense_tags_pk primary key(expense_id, tag_id))";
        String createIncome = CreateTable
                + Income.TableName
                + "(_id integer primary key autoincrement, income_date integer not null, amount real not null, source integer not null, notes text)";

        database.execSQL(createTag);
        database.execSQL(createSource);
        database.execSQL(createExpense);
        database.execSQL(createExpenseTag);
        database.execSQL(createIncome);

        database.execSQL("INSERT INTO source (name) VALUES ('CASH') ");
        database.execSQL("INSERT INTO source (name) VALUES ('CREDIT CARD') ");
        database.execSQL("INSERT INTO source (name) VALUES ('DEBIT CARD') ");
        database.execSQL("INSERT INTO source (name) VALUES ('ECS') ");
        database.execSQL("INSERT INTO source (name) VALUES ('WIRE TRANSFER') ");

        database.execSQL("INSERT INTO tag (tag_order, name) VALUES (1, 'HOME') ");
        database.execSQL("INSERT INTO tag (tag_order, name) VALUES (1, 'OFFICE') ");
        database.execSQL("INSERT INTO tag (tag_order, name) VALUES (1, 'GROCERIES') ");
        database.execSQL("INSERT INTO tag (tag_order, name) VALUES (1, 'TRAVEL') ");
        database.execSQL("INSERT INTO tag (tag_order, name) VALUES (1, 'FOOD') ");
        database.execSQL("INSERT INTO tag (tag_order, name) VALUES (1, 'EAT OUT') ");
        database.execSQL("INSERT INTO tag (tag_order, name) VALUES (1, 'SNACKS') ");
        database.execSQL("INSERT INTO tag (tag_order, name) VALUES (1, 'CAR') ");
        database.execSQL("INSERT INTO tag (tag_order, name) VALUES (1, 'BIKE') ");
        database.execSQL("INSERT INTO tag (tag_order, name) VALUES (1, 'FUEL') ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    }
}
