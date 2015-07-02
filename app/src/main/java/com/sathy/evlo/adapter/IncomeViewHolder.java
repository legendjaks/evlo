package com.sathy.evlo.adapter;

import android.view.View;
import android.widget.TextView;

import com.sathy.evlo.activity.R;
import com.sathy.evlo.util.TextFormat;

/**
 * Created by sathy on 2/7/15.
 */
public class IncomeViewHolder extends CircledViewHolder {

    private TextView date;
    private TextView amount;
    private TextView notes;
    private TextView source;

    public IncomeViewHolder(View view, CircledCursorAdapter adapter) {
        super(view, adapter);

        this.date = (TextView) view.findViewById(R.id.row_date);
        this.amount = (TextView) view.findViewById(R.id.row_amount);
        this.notes = (TextView) view.findViewById(R.id.row_notes);
        this.source = (TextView) view.findViewById(R.id.row_source);
    }

    @Override
    public String getSymbol() {
        return TextFormat.getSourceSymbol(String.valueOf(source.getText().charAt(0)));
    }

    @Override
    public String getKey() {
        return source.getText().toString();
    }

    public TextView getDate() {
        return date;
    }

    public TextView getAmount() {
        return amount;
    }

    public TextView getNotes() {
        return notes;
    }

    public TextView getSource() {
        return source;
    }
}
