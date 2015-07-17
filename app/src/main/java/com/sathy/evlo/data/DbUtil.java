package com.sathy.evlo.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sathy.evlo.model.PreviewInfo;

/**
 * Created by sathy on 15/07/15.
 */
public class DbUtil {

  public static PreviewInfo getBalance(Context context) {

    Database evlo = new Database(context);
    SQLiteDatabase db = evlo.getReadableDatabase();

    String expense = "Select total(amount) as total From Expense ";
    String income = "Select total(amount) as total From Income ";

    Cursor e = db.rawQuery(expense, null);
    Cursor i = db.rawQuery(income, null);

    float totalExpense = 0.0f;
    if (e != null && e.getCount() > 0 && e.moveToFirst()) {
      totalExpense = e.getFloat(e.getColumnIndex(Expense.Total));
    }

    if (e != null)
      e.close();

    float totalIncome = 0.0f;
    if (i != null && i.getCount() > 0 && i.moveToFirst()) {
      totalIncome = i.getFloat(i.getColumnIndex(Expense.Total));
    }

    if (i != null)
      i.close();

    return new PreviewInfo((int) totalIncome, (int) totalExpense);
  }
}
