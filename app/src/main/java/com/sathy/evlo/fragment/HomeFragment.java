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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.sathy.evlo.activity.ExpenseResultsActivity;
import com.sathy.evlo.activity.NewExpenseActivity;
import com.sathy.evlo.activity.R;
import com.sathy.evlo.activity.SearchExpenseActivity;
import com.sathy.evlo.data.DbUtil;
import com.sathy.evlo.data.Expense;
import com.sathy.evlo.data.Tag;
import com.sathy.evlo.listener.Searchable;
import com.sathy.evlo.model.PreviewInfo;
import com.sathy.evlo.provider.DatabaseProvider;
import com.sathy.evlo.util.MaterialColorGenerator;

import java.util.ArrayList;
import java.util.List;

import static com.sathy.evlo.util.TextFormat.getCurrentMonth;
import static com.sathy.evlo.util.TextFormat.getCurrentMonthDates;
import static com.sathy.evlo.util.TextFormat.quotes;

public class HomeFragment extends Fragment implements Searchable, LoaderManager.LoaderCallbacks<Cursor>, OnChartValueSelectedListener {

  private FloatingActionButton addExpense;
  private PieChart chart;
  private ProgressBar utilization;
  private TextView month;
  private TextView utilized;
  private TextView totalAmount;
  private TextView no_data;
  private LinearLayout utilization_layout;
  private Typeface font;
  private Context context;

  private ArrayList<Long> tagIds;
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
    context = view != null ? view.getContext() : null;
    addExpense = (FloatingActionButton) view.findViewById(R.id.fab);
    addExpense.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent myIntent = new Intent(getActivity(), NewExpenseActivity.class);
        startActivity(myIntent);
      }
    });

    utilization_layout = (LinearLayout) view.findViewById(R.id.utilized_layout);
    no_data = (TextView) view.findViewById(R.id.no_data);
    month = (TextView) view.findViewById(R.id.month);
    utilized = (TextView) view.findViewById(R.id.utilized);
    totalAmount = (TextView) view.findViewById(R.id.total);
    utilization = (ProgressBar) view.findViewById(R.id.utilization);
    chart = (PieChart) view.findViewById(R.id.chart);

    font = Typeface.create("sans-serif", Typeface.NORMAL);
    tagIds = new ArrayList<>();

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
    chart.setCenterText("DISTRIBUTION");

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
    String query = "SELECT t._id, t.name as tag, total(e.amount) as total From expense e, Tag t Where e.tag_id = t._id ";
    query += " And e.expense_date >= " + quotes(dates[0]) + " And e.expense_date <= " + quotes(dates[1]);
    query += " Group By t.name Order BY total ASC";

    return new CursorLoader(context,
        DatabaseProvider.EXPENSE_URI, Expense.Columns, query, null, null);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor newCursor) {

    PreviewInfo info = DbUtil.getBalance(context);
    if (info.getIncome() != 0 || info.getExpense() != 0) {

      utilization_layout.setVisibility(View.VISIBLE);
      utilization.setVisibility(View.VISIBLE);
      no_data.setVisibility(View.GONE);

      if (info.getExpense() != 0)
        chart.setVisibility(View.VISIBLE);

      int progress = 100;
      if (info.getIncome() > 0)
        progress = info.getExpense() * 100 / info.getIncome();

      if (progress > 100)
        progress = 100;

      utilization.setProgress(progress);
      if (progress < 80)
        utilization.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.green_progress));
      else if (progress < 90)
        utilization.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.yellow_progress));
      else
        utilization.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.red_progress));

      utilized.setText(currency + info.getExpense() + " utilized");
      totalAmount.setText(currency + String.valueOf(info.getIncome()));
    } else {
      utilization_layout.setVisibility(View.GONE);
      utilization.setVisibility(View.GONE);
      chart.setVisibility(View.GONE);
      no_data.setVisibility(View.VISIBLE);
    }

    if (cursor != null)
      cursor.close();

    cursor = newCursor;
    if (cursor == null || cursor.getCount() == 0) {
      chart.setVisibility(View.GONE);
      return;
    }

    if (!cursor.moveToFirst())
      return;

    tagIds.clear();

    List<String> xAxis = new ArrayList<>();
    List<Entry> yAxis = new ArrayList<>();

    int index = 0;
    do {
      float total = (float) cursor.getDouble(cursor.getColumnIndex(Expense.Total));
      xAxis.add(cursor.getString(cursor.getColumnIndex(Expense.Tag)));
      yAxis.add(new Entry(total, index));
      tagIds.add(cursor.getLong(cursor.getColumnIndex(Tag.Id)));

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

    String[] dates = getCurrentMonthDates();
    Intent intent = new Intent(getActivity(), ExpenseResultsActivity.class);
    intent.putExtra("start_date", dates[0]);
    intent.putExtra("end_date", dates[1]);
    intent.putExtra("tag", String.valueOf(tagIds.get(e.getXIndex())));

    startActivity(intent);
  }

  @Override
  public void onNothingSelected() {
    chart.setCenterText("DISTRIBUTION");
  }
}
