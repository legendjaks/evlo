package com.sathy.evlo.activity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.widget.TextView;

import com.sathy.evlo.adapter.IncomeCursorAdapter;
import com.sathy.evlo.data.Income;
import com.sathy.evlo.provider.DatabaseProvider;
import com.sathy.evlo.util.TextFormat;

import static com.sathy.evlo.util.TextFormat.quotes;

/**
 * Created by sathy on 15/07/15.
 */
public class IncomeResultsActivity extends CircleListActivity {

  private TextView criteria;
  private TextView incomeTotal;
  private String searchCriteria;
  private double total = 0;

  public IncomeResultsActivity() {
    super(NewIncomeActivity.class, DatabaseProvider.INCOME_URI, DatabaseProvider.INCOME_ITEM_TYPE);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {

    String header = null;
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      String from = extras.getString("start_date");
      String to = extras.getString("end_date");
      String source = extras.getString("source");

      searchCriteria = " income_date >= " + quotes(from) + " And income_date <= " + quotes(to);
      if(source != null)
      searchCriteria += " And source = " + quotes(source);

      header = from + " to " + to;
    }

    super.onCreate(savedInstanceState);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    criteria = (TextView) findViewById(R.id.search_criteria);
    incomeTotal = (TextView) findViewById(R.id.search_total);
    criteria.setText(header);
  }

  @Override
  public CursorAdapter getAdapter(Context context) {
    return new IncomeCursorAdapter(context, this);
  }

  @Override
  public String[] getColumns() {
    return Income.Columns;
  }

  @Override
  public String getCriteria() {
    return searchCriteria;
  }

  @Override
  public void preprocess(Cursor cursor) {

    if (cursor == null || cursor.getCount() == 0) {
      criteria.setText("No match found. Refine your search.");
      incomeTotal.setText("");
      return;
    }

    total = 0;
    if ((cursor.getCount() > 0) && cursor.moveToFirst()) {

      do {
        double amount = cursor.getDouble(cursor.getColumnIndex(Income.Amount));
        total += amount;
      } while (cursor.moveToNext());

      incomeTotal.setText(TextFormat.toDecimalText(total) + " " + getString(R.string.rs));
    }
  }
}
