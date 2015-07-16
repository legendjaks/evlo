package com.sathy.evlo.activity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.widget.TextView;

import com.sathy.evlo.adapter.ExpenseCursorAdapter;
import com.sathy.evlo.data.Expense;
import com.sathy.evlo.provider.DatabaseProvider;
import com.sathy.evlo.util.TextFormat;

import static com.sathy.evlo.util.TextFormat.quotes;

/**
 * Created by sathy on 15/07/15.
 */
public class ExpenseResultsActivity extends CircleListActivity {

  private TextView expenseTotal;
  private String query;
  private double total = 0;

  public ExpenseResultsActivity() {
    super(NewExpenseActivity.class, DatabaseProvider.EXPENSE_URI, DatabaseProvider.EXPENSE_ITEM_TYPE);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {

    String header = null;
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      String from = extras.getString("start_date");
      String to = extras.getString("end_date");
      String source = extras.getString("source");
      String tag = extras.getString("tag");

      query = "SELECT e._id, e.expense_date, e.source_id, e.tag_id, e.amount, e.notes, s.name as source, t.name as tag,";
      query += "(Select total(amount) From Expense where tag_id = e.tag_id And expense_date >= " + quotes(from) + " And expense_date <= " + quotes(to);
      if (source != null)
        query += " And source_id = " + source;
      query += ") as total ";
      query += " From expense e INNER JOIN source s ON e.source_id = s._id INNER JOIN tag t ON e.tag_id = t._id ";
      query += " Where e.expense_date >= " + quotes(from) + " And e.expense_date <= " + quotes(to);
      if (source != null)
        query += " And e.source_id = " + source;
      if (tag != null)
        query += " And e.tag_id = " + tag;
      query += " Order By t.name ASC, e.expense_date DESC ";

      header = from + " to " + to;
    }

    super.onCreate(savedInstanceState);
    TextView criteria = (TextView) findViewById(R.id.search_criteria);
    expenseTotal = (TextView) findViewById(R.id.search_total);
    criteria.setText(header);
  }

  @Override
  public CursorAdapter getAdapter(Context context) {
    return new ExpenseCursorAdapter(context, this);
  }

  @Override
  public String[] getColumns() {
    return Expense.Columns;
  }

  @Override
  public String getCriteria() {
    return query;
  }

  @Override
  public void preprocess(Cursor cursor) {

    if (cursor == null)
      return;

    if ((cursor.getCount() > 0) && cursor.moveToFirst()) {

      do {
        double amount = cursor.getDouble(cursor.getColumnIndex(Expense.Amount));
        total += amount;
      } while (cursor.moveToNext());

      expenseTotal.setText(TextFormat.toDecimalText(total) + " " + getString(R.string.rs));
    }
  }
}
