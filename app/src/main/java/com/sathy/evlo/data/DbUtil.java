package com.sathy.evlo.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sathy.evlo.model.PreviewInfo;

import static com.sathy.evlo.util.TextFormat.getCurrentMonthDates;
import static com.sathy.evlo.util.TextFormat.quotes;

/**
 * Created by sathy on 15/07/15.
 */
public class DbUtil {

  public static PreviewInfo getBalance(Context context) {

    Database evlo = new Database(context);
    SQLiteDatabase db = evlo.getReadableDatabase();

    String[] dates = getCurrentMonthDates();

    String last_expense = "Select total(amount) as total From Expense Where expense_date < " + quotes(dates[0]);
    String month_expense = "Select total(amount) as total From Expense Where expense_date >= " + quotes(dates[0]) + " And expense_date <= " + quotes(dates[1]);
    String income = "Select total(amount) as total From Income ";

    Cursor e = db.rawQuery(last_expense, null);
    Cursor c = db.rawQuery(month_expense, null);
    Cursor i = db.rawQuery(income, null);

    float last = 0.0f;
    if (e != null && e.getCount() > 0 && e.moveToFirst()) {
      last = e.getFloat(e.getColumnIndex(Expense.Total));
    }

    if (e != null)
      e.close();

    float current = 0.0f;
    if (c != null && c.getCount() > 0 && c.moveToFirst()) {
      current = c.getFloat(c.getColumnIndex(Expense.Total));
    }

    if (c != null)
      c.close();

    float totalIncome = 0.0f;
    if (i != null && i.getCount() > 0 && i.moveToFirst()) {
      totalIncome = i.getFloat(i.getColumnIndex(Expense.Total));
    }

    if (i != null)
      i.close();

    return new PreviewInfo((int) (totalIncome - last), (int) current);
  }
}
