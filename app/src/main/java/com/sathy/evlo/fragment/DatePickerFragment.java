package com.sathy.evlo.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import com.sathy.evlo.listener.DateSetListener;
import com.sathy.evlo.util.TextFormat;

import java.util.Calendar;

/**
 * Created by sathy on 30/6/15.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private Activity activity;
    private Calendar calendar;
    private DateSetListener listener;

    public DatePickerFragment(Activity activity, Calendar calendar, DateSetListener listener){
        this.activity = activity;
        this.calendar = calendar;
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(activity, this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        if(listener != null)
            listener.onDateSet();
    }
}
