package com.sathy.evlo.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.sathy.evlo.data.Source;
import com.sathy.evlo.data.Tag;
import com.sathy.evlo.fragment.DatePickerFragment;
import com.sathy.evlo.listener.DateSetListener;
import com.sathy.evlo.provider.DatabaseProvider;
import com.sathy.evlo.util.TextFormat;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by sathy on 24/6/15.
 */
public class SearchExpenseActivity extends AppCompatActivity {

  private Toolbar toolbar;
  private EditText from;
  private EditText to;
  private Spinner source;
  private Switch source_switch;
  private Spinner tag;
  private Switch tag_switch;

  private Calendar cal_from = Calendar.getInstance();
  private Calendar cal_to = Calendar.getInstance();

  private ArrayList<String> sources;
  private DateSetListener fromDateListener = new DateSetListener() {
    @Override
    public void onDateSet() {
      from.setText(TextFormat.toDisplayDateText(cal_from.getTime()));
    }
  };
  private DateSetListener toDateListener = new DateSetListener() {
    @Override
    public void onDateSet() {
      to.setText(TextFormat.toDisplayDateText(cal_to.getTime()));
    }
  };

  private SimpleCursorAdapter sourceAdapter;
  private SimpleCursorAdapter tagAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.search_expense);

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    from = (EditText) findViewById(R.id.from);
    to = (EditText) findViewById(R.id.to);
    source = (Spinner) findViewById(R.id.source);
    source_switch = (Switch) findViewById(R.id.source_switch);
    tag = (Spinner) findViewById(R.id.tag);
    tag_switch = (Switch) findViewById(R.id.tag_switch);

    from.setFocusable(false);
    to.setFocusable(false);

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

    from.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setParameters(SearchExpenseActivity.this, cal_from, fromDateListener);
        newFragment.show(getFragmentManager(), "datePicker");
      }
    });

    to.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setParameters(SearchExpenseActivity.this, cal_to, toDateListener);
        newFragment.show(getFragmentManager(), "datePicker");
      }
    });

    source_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (source_switch.isChecked())
          source.setVisibility(View.VISIBLE);
        else
          source.setVisibility(View.GONE);
      }
    });

    tag_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (tag_switch.isChecked())
          tag.setVisibility(View.VISIBLE);
        else
          tag.setVisibility(View.GONE);
      }
    });
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
      if (search())
        this.finish();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private boolean search() {

    String start = from.getText().toString();
    if (start.length() == 0) return false;

    String end = to.getText().toString();
    if (end.length() == 0) return false;

    if (start.compareTo(end) > 0) {
      String temp = start;
      start = end;
      end = temp;
    }

    Intent intent;

    if (!tag_switch.isChecked())
      intent = new Intent(this, ExpenseResultsOverviewActivity.class);
    else
      intent = new Intent(this, ExpenseResultsActivity.class);

    intent.putExtra("start_date", start);
    intent.putExtra("end_date", end);
    if (source_switch.isChecked())
      intent.putExtra("source", String.valueOf(source.getSelectedItemId()));
    if (tag_switch.isChecked())
      intent.putExtra("tag", String.valueOf(tag.getSelectedItemId()));

    startActivity(intent);

    return true;
  }
}
