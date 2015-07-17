package com.sathy.evlo.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.sathy.evlo.data.Expense;
import com.sathy.evlo.data.Tag;
import com.sathy.evlo.provider.DatabaseProvider;
import com.sathy.evlo.util.MaterialColorGenerator;
import com.sathy.evlo.util.TextFormat;

import java.util.ArrayList;
import java.util.List;

import static com.sathy.evlo.util.TextFormat.quotes;

/**
 * Created by sathy on 15/07/15.
 */
public class ExpenseResultsOverviewActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, OnChartValueSelectedListener {

  private PieChart chart;

  private ArrayList<Long> tagIds;

  private TextView criteria;
  private TextView expenseTotal;
  private String from;
  private String to;
  private String source;
  private String query;

  private Cursor cursor;

  public ExpenseResultsOverviewActivity() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.expense_results_overview);

    String header = null;
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      from = extras.getString("start_date");
      to = extras.getString("end_date");
      source = extras.getString("source");

      query = "SELECT t._id, t.name as tag, total(e.amount) as total From expense e, Tag t Where e.tag_id = t._id ";
      query += " And e.expense_date >= " + quotes(from) + " And e.expense_date <= " + quotes(to);
      if (source != null)
        query += " And e.source_id = " + source;
      query += " Group By t.name Order BY total ASC";

      header = from + " to " + to;
    }

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    criteria = (TextView) findViewById(R.id.search_criteria);
    expenseTotal = (TextView) findViewById(R.id.search_total);
    criteria.setText(header);

    chart = (PieChart) findViewById(R.id.chart);

    tagIds = new ArrayList<>();

    initialize();
  }

  private void initialize() {

    chart.setOnChartValueSelectedListener(this);
    chart.setUsePercentValues(false);
    chart.setDescription("");

    chart.setDragDecelerationFrictionCoef(0.95f);

    chart.setDrawHoleEnabled(true);
    chart.setHoleColorTransparent(true);

    chart.setTransparentCircleColor(Color.WHITE);

    chart.setHoleRadius(30f);
    chart.setTransparentCircleRadius(30f);

    chart.setRotationAngle(0);
    chart.setRotationEnabled(true);

    chart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
    chart.spin(2000, 0, 360, Easing.EasingOption.EaseInOutQuad);

    getSupportLoaderManager().initLoader(0, null, this);
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {

    CursorLoader cursorLoader = new CursorLoader(this,
        DatabaseProvider.EXPENSE_URI, Expense.Columns, query, null, null);
    return cursorLoader;
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor newCursor) {

    if (cursor != null)
      cursor.close();

    cursor = newCursor;

    if (cursor == null || cursor.getCount() == 0) {
      criteria.setText("No match found. Refine your search.");
      expenseTotal.setText("");
      chart.setVisibility(View.GONE);
      return;
    } else
      chart.setVisibility(View.VISIBLE);

    if ((cursor.getCount() == 0) || !cursor.moveToFirst())
      return;

    tagIds.clear();

    List<String> xAxis = new ArrayList<>();
    List<Entry> yAxis = new ArrayList<>();

    float amount = 0;
    int index = 0;
    do {
      float total = (float) cursor.getDouble(cursor.getColumnIndex(Expense.Total));
      xAxis.add(cursor.getString(cursor.getColumnIndex(Expense.Tag)));
      yAxis.add(new Entry(total, index));
      tagIds.add(cursor.getLong(cursor.getColumnIndex(Tag.Id)));

      amount += total;
      index++;
    } while (cursor.moveToNext());

    PieDataSet dataSet = new PieDataSet(yAxis, "");
    dataSet.setSliceSpace(3f);
    dataSet.setSelectionShift(0f);
    dataSet.setDrawValues(false);
    dataSet.setColors(MaterialColorGenerator.getColors());

    PieData data = new PieData(xAxis, dataSet);
    data.setValueTextSize(12f);
    data.setValueTextColor(Color.WHITE);

    chart.setData(data);
    chart.highlightValues(null);
    chart.getLegend().setEnabled(false);
    chart.invalidate();

    expenseTotal.setText(getString(R.string.rs) + " " + TextFormat.toDecimalText(amount));
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    if (cursor != null) {
      cursor.close();
      cursor = null;
    }
  }

  @Override
  public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    if (e == null)
      return;

    Intent intent = new Intent(this, ExpenseResultsActivity.class);
    intent.putExtra("start_date", from);
    intent.putExtra("end_date", to);
    intent.putExtra("source", source);
    intent.putExtra("tag", String.valueOf(tagIds.get(e.getXIndex())));

    startActivity(intent);
  }

  @Override
  public void onNothingSelected() {
  }
}
