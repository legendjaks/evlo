package com.sathy.evlo.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.sathy.evlo.data.Income;
import com.sathy.evlo.fragment.DatePickerFragment;
import com.sathy.evlo.listener.DateSetListener;
import com.sathy.evlo.provider.DatabaseProvider;
import com.sathy.evlo.util.TextFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by sathy on 24/6/15.
 */
public class NewIncomeActivity extends AppCompatActivity implements DateSetListener {

  private Toolbar toolbar;
  private EditText date;
  private EditText amount;
  private Spinner source;
  private EditText notes;

  private Calendar calendar = Calendar.getInstance();
  private Uri uri;
  private ArrayList<String> sources;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.new_income);

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    date = (EditText) findViewById(R.id.date);
    amount = (EditText) findViewById(R.id.amount);
    source = (Spinner) findViewById(R.id.source);
    notes = (EditText) findViewById(R.id.notes);

    date.setFocusable(false);
    sources = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.income_sources)));

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    date.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setParameters(NewIncomeActivity.this, calendar, NewIncomeActivity.this);
        newFragment.show(getFragmentManager(), "datePicker");
      }
    });

    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      setTitle(R.string.edit_income);
      uri = extras.getParcelable(DatabaseProvider.INCOME_ITEM_TYPE);
      populate();
    } else
      setTitle(R.string.new_income);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.save, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    if (id == R.id.action_save) {
      if (save())
        this.finish();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void populate() {
    if (uri == null) {
      return;
    }

    Cursor cursor = getContentResolver().query(uri, Income.Columns, null, null,
        null);
    if (cursor != null) {
      cursor.moveToFirst();

      date.setText(cursor.getString(cursor
          .getColumnIndexOrThrow(Income.IncomeDate)));
      amount.setText(cursor.getString(cursor
          .getColumnIndexOrThrow(Income.Amount)));
      String selectedSource = cursor.getString(cursor
          .getColumnIndexOrThrow(Income.Source));
      notes.setText(cursor.getString(cursor
          .getColumnIndexOrThrow(Income.Notes)));

      int sourceId = sources.indexOf(selectedSource);
      source.setSelection(sourceId);
      // always close the cursor
      cursor.close();
    }
  }

  private boolean save() {

    if (amount.getText().toString().trim().length() == 0)
      return false;
    double incomeAmount = Double.parseDouble(amount.getText().toString());
    if (incomeAmount == 0.0)
      return false;

    String incomedate = date.getText().toString();
    if (incomedate.length() == 0) {
      incomedate = TextFormat.toDisplayDateText(calendar.getTime());
    }

    String note = notes.getText().toString();

    ContentValues values = new ContentValues();
    values.put(Income.IncomeDate, incomedate);
    values.put(Income.Amount, incomeAmount);
    values.put(Income.Source, source.getSelectedItem().toString());
    values.put(Income.Notes, note);

    if (uri == null) {
      uri = getContentResolver().insert(DatabaseProvider.INCOME_URI, values);
    } else {
      getContentResolver().update(uri, values, null, null);
    }

    return true;
  }

  @Override
  public void onDateSet() {
    date.setText(TextFormat.toDisplayDateText(calendar.getTime()));
  }
}
