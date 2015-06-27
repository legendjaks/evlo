package com.sathy.evlo.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.sathy.evlo.dao.IncomeDao;
import com.sathy.evlo.data.Income;
import com.sathy.evlo.data.TableEntity;
import com.sathy.evlo.provider.DatabaseProvider;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by sathy on 24/6/15.
 */
public class NewIncomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText date;
    private EditText amount;
    private EditText notes;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
    private Calendar calendar = Calendar.getInstance();

    private Uri uri;

    private static final String[] tableColumns = new String[] { Income.Id, Income.IncomeDate, Income.Amount, Income.Notes
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_income);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        date = (EditText) findViewById(R.id.date);
        amount = (EditText) findViewById(R.id.amount);
        notes = (EditText)findViewById(R.id.notes);

        date.setFocusable(false);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            setTitle(R.string.edit_income);
            uri = extras.getParcelable(DatabaseProvider.CONTENT_ITEM_TYPE);
            populate();
        }else
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
            if(save())
                this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void populate() {
        if (uri == null) {
            return;
        }

        Cursor cursor = getContentResolver().query(uri, tableColumns, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();

            date.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(Income.IncomeDate)));
            amount.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(Income.Amount)));
            notes.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(Income.Notes)));

            // always close the cursor
            cursor.close();
        }
    }

    private boolean save() {

        String incomedate = date.getText().toString();
        if(incomedate.length() == 0){
            incomedate = sdf.format(calendar.getTime());
        }

        String note = notes.getText().toString();
        if (amount.getText().toString().trim().length() == 0)
            return false;
        double incomeAmount = Double.parseDouble(amount.getText().toString());
        if (incomeAmount == 0.0)
            return false;

        ContentValues values = new ContentValues();
        values.put(Income.IncomeDate, incomedate);
        values.put(Income.Amount, incomeAmount);
        values.put(Income.Notes, note);

        if (uri == null) {
            uri = getContentResolver().insert(DatabaseProvider.CONTENT_URI, values);
        } else {
            getContentResolver().update(uri, values, null, null);
        }

        return true;
    }

    public class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);

            date.setText(sdf.format(calendar.getTime()));
        }
    }
}
