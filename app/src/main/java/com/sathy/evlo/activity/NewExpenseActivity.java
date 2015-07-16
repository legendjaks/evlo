package com.sathy.evlo.activity;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.sathy.evlo.data.Expense;
import com.sathy.evlo.data.Source;
import com.sathy.evlo.data.Tag;
import com.sathy.evlo.fragment.DatePickerFragment;
import com.sathy.evlo.listener.DateSetListener;
import com.sathy.evlo.provider.DatabaseProvider;
import com.sathy.evlo.util.TextFormat;

import java.util.Calendar;

/**
 * Created by sathy on 24/6/15.
 */
public class NewExpenseActivity extends AppCompatActivity implements DateSetListener {

  private Toolbar toolbar;
  private EditText date;
  private EditText amount;
  private Spinner source;
  private Spinner tag;
  private EditText notes;

  private Calendar calendar = Calendar.getInstance();
  private Uri uri;

  private SimpleCursorAdapter sourceAdapter;
  private SimpleCursorAdapter tagAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.new_expense);

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    date = (EditText) findViewById(R.id.date);
    amount = (EditText) findViewById(R.id.amount);
    source = (Spinner) findViewById(R.id.source);
    tag = (Spinner) findViewById(R.id.tag);
    notes = (EditText) findViewById(R.id.notes);

    date.setFocusable(false);

    int[] columns = {R.id.row_id, R.id.row_name};

    Cursor cursor = getContentResolver().query(
        DatabaseProvider.SOURCE_URI,
        Source.Columns,
        null,
        null,
        null
    );

    sourceAdapter = new SimpleCursorAdapter(
        this,
        R.layout.spinner,
        cursor,
        Source.Columns,
        columns,
        0
    );
    sourceAdapter.setDropDownViewResource(R.layout.spinner);
    source.setAdapter(sourceAdapter);

    cursor = getContentResolver().query(
        DatabaseProvider.TAG_URI,
        Tag.Columns,
        null,
        null,
        null
    );

    tagAdapter = new SimpleCursorAdapter(
        this,
        R.layout.spinner,
        cursor,
        Tag.Columns,
        columns,
        0
    );
    tagAdapter.setDropDownViewResource(R.layout.spinner);
    tag.setAdapter(tagAdapter);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    date.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        DialogFragment newFragment = new DatePickerFragment(NewExpenseActivity.this, calendar, NewExpenseActivity.this);
        newFragment.show(getFragmentManager(), "datePicker");
      }
    });

    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      setTitle(R.string.edit_expense);
      uri = extras.getParcelable(DatabaseProvider.EXPENSE_ITEM_TYPE);
      populate();
    } else
      setTitle(R.string.new_expense);
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

    Cursor cursor = getContentResolver().query(uri, Expense.Columns, null, null,
        null);
    if (cursor != null) {
      cursor.moveToFirst();

      date.setText(cursor.getString(cursor
          .getColumnIndexOrThrow(Expense.ExpenseDate)));
      amount.setText(cursor.getString(cursor
          .getColumnIndexOrThrow(Expense.Amount)));
      notes.setText(cursor.getString(cursor
          .getColumnIndexOrThrow(Expense.Notes)));

      int selected = cursor.getInt(cursor
          .getColumnIndexOrThrow(Expense.SourceId));
      for (int i = 0; i < sourceAdapter.getCount(); i++) {
        if (sourceAdapter.getItemId(i) == selected) {
          source.setSelection(i);
          break;
        }
      }

      selected = cursor.getInt(cursor
          .getColumnIndexOrThrow(Expense.TagId));
      for (int i = 0; i < tagAdapter.getCount(); i++) {
        if (tagAdapter.getItemId(i) == selected) {
          tag.setSelection(i);
          break;
        }
      }
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
    values.put(Expense.ExpenseDate, incomedate);
    values.put(Expense.Amount, incomeAmount);
    values.put(Expense.SourceId, source.getSelectedItemId());
    values.put(Expense.TagId, tag.getSelectedItemId());
    values.put(Expense.Notes, note);

    if (uri == null) {
      uri = getContentResolver().insert(DatabaseProvider.EXPENSE_URI, values);
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
