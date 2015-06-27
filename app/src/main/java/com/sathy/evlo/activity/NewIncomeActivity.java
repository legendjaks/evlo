package com.sathy.evlo.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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
    private IncomeDao incomeDao;

    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_income);

        id = 0;
        incomeDao = new IncomeDao(this);

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
            id = extras.getLong(TableEntity.Id);
            setTitle(R.string.edit_income);
        } else {
            setTitle(R.string.new_income);
        }

        populate();
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
            if(saveIncome())
                this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void populate() {
        if (id == 0) {
            return;
        }

        Income income = (Income) incomeDao.read(id);
        date.setText(income.getIncomeDate());
        amount.setText(String.valueOf(income.getAmount()));
        notes.setText(income.getNotes());
    }

    private boolean saveIncome() {

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

        Income income = new Income(id, incomedate, incomeAmount, note);
        if (id == 0) {
            id = incomeDao.create(income);
        } else {
            incomeDao.update(income);
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
