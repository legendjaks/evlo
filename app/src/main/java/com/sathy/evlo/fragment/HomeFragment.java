package com.sathy.evlo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.sathy.evlo.activity.NewExpenseActivity;
import com.sathy.evlo.activity.R;
import com.sathy.evlo.activity.SearchExpenseActivity;
import com.sathy.evlo.data.DbUtil;
import com.sathy.evlo.data.Expense;
import com.sathy.evlo.listener.Searchable;
import com.sathy.evlo.provider.DatabaseProvider;
import com.sathy.evlo.util.MaterialColorGenerator;

import java.util.ArrayList;
import java.util.Calendar;

import static com.sathy.evlo.util.TextFormat.dateValue;
import static com.sathy.evlo.util.TextFormat.getCurrentMonth;
import static com.sathy.evlo.util.TextFormat.quotes;

public class HomeFragment extends Fragment implements Searchable, LoaderManager.LoaderCallbacks<Cursor>, OnChartValueSelectedListener {

  private FloatingActionButton addExpense;
  private PieChart chart;
  private ProgressBar utilization;
  private TextView month;
  private TextView utilized;
  private TextView totalAmount;
  private Typeface font;
  private Context context;

  private ArrayList<String> xAxis;
  private ArrayList<Entry> yAxis;
  private String currency;
  private Cursor cursor;

  public HomeFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    final View view = getView();
    context = view.getContext();
    addExpense = (FloatingActionButton) view.findViewById(R.id.fab);
    addExpense.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent myIntent = new Intent(getActivity(), NewExpenseActivity.class);
        startActivity(myIntent);
      }
    });

    month = (TextView) view.findViewById(R.id.month);
    utilized = (TextView) view.findViewById(R.id.utilized);
    totalAmount = (TextView) view.findViewById(R.id.total);
    utilization = (ProgressBar) view.findViewById(R.id.utilization);
    chart = (PieChart) view.findViewById(R.id.chart);

    font = Typeface.create("sans-serif", Typeface.NORMAL);
    xAxis = new ArrayList<String>();
    yAxis = new ArrayList<Entry>();

    initialize();
  }

  private void initialize() {

    currency = context.getString(R.string.rs) + " ";
    month.setText(getCurrentMonth());

    chart.setOnChartValueSelectedListener(this);
    chart.setUsePercentValues(false);
    chart.setDescription("");

    chart.setDragDecelerationFrictionCoef(0.95f);
    chart.setCenterTextTypeface(font);
    chart.setCenterTextSize(14);

    chart.setDrawHoleEnabled(true);
    chart.setHoleColorTransparent(true);

    chart.setTransparentCircleColor(Color.WHITE);

    chart.setHoleRadius(50f);
    chart.setTransparentCircleRadius(50f);

    chart.setRotationAngle(0);
    chart.setRotationEnabled(true);

    chart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
    chart.spin(2000, 0, 360, Easing.EasingOption.EaseInOutQuad);

    getLoaderManager().initLoader(0, null, this);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.home, container, false);
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

  @Override
  public void onSearch() {
    Intent intent = new Intent(getActivity(), SearchExpenseActivity.class);
    startActivity(intent);
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {

    String[] dates = getCurrentMonthDates();
    String query = "SELECT t.name as tag, total(e.amount) as total From expense e, Tag t Where e.tag_id = t._id ";
    query += " And e.expense_date >= " + quotes(dates[0]) + " And e.expense_date <= " + quotes(dates[1]);
    query += " Group By t.name Order BY total ASC";

    CursorLoader cursorLoader = new CursorLoader(context,
        DatabaseProvider.EXPENSE_URI, Expense.Columns, query, null, null);
    return cursorLoader;
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor newCursor) {

    if (cursor != null)
      cursor.close();

    cursor = newCursor;
    if (cursor == null)
      return;

    if ((cursor.getCount() == 0) || !cursor.moveToFirst())
      return;

    xAxis.clear();
    yAxis.clear();

    int balance = (int) DbUtil.getBalance(getView().getContext());
    float expenseTotal = 0;
    int index = 0;
    do {
      float total = (float) cursor.getDouble(cursor.getColumnIndex(Expense.Total));
      xAxis.add(cursor.getString(cursor.getColumnIndex(Expense.Tag)));
      yAxis.add(new Entry(total, index));

      expenseTotal += total;
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

    chart.setCenterText("DISTRIBUTION");
    chart.setData(data);
    chart.highlightValues(null);
    chart.getLegend().setEnabled(false);
    chart.invalidate();

    int total = balance + (int) expenseTotal;
    int progress = (int) expenseTotal * 100 / total;

    if (balance < 0)
      progress = 100;

    utilization.setProgress(progress);
    if (progress >= 80)
      utilization.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.red_progress));
    else
      utilization.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.green_progress));

    utilized.setText(currency + (int) expenseTotal + " utilized");
    totalAmount.setText(currency + String.valueOf(total));
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    if (cursor != null) {
      cursor.close();
      cursor = null;
    }
  }

  private String[] getCurrentMonthDates() {
    Calendar from = Calendar.getInstance();
    from.set(Calendar.DATE, 1);

    Calendar to = Calendar.getInstance();
    to.set(Calendar.DATE, 1);
    to.add(Calendar.MONTH, 1);
    to.add(Calendar.DATE, -1);

    return new String[]{dateValue(from), dateValue(to)};
  }

  @Override
  public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
    if (e == null)
      return;

    chart.setCenterText(xAxis.get(e.getXIndex()) + " " + currency + (int) e.getVal());
  }

  @Override
  public void onNothingSelected() {
    chart.setCenterText("DISTRIBUTION");
  }
}
