package com.sathy.evlo.holder;

import android.view.View;
import android.widget.TextView;

import com.sathy.evlo.activity.R;
import com.sathy.evlo.adapter.CircledCursorAdapter;
import com.sathy.evlo.util.TextFormat;

/**
 * Created by sathy on 2/7/15.
 */
public class ExpenseViewHolder extends CircledViewHolder {

    private TextView date;
    private TextView amount;
    private TextView notes;
    private TextView source;
    private String sourceName;

    public ExpenseViewHolder(View view, CircledCursorAdapter adapter) {
        super(view, adapter);

        this.date = (TextView) view.findViewById(R.id.row_date);
        this.amount = (TextView) view.findViewById(R.id.row_amount);
        this.notes = (TextView) view.findViewById(R.id.row_notes);
        this.source = (TextView) view.findViewById(R.id.row_source);
    }

    @Override
    public String getSymbol() {
        return TextFormat.getSourceSymbol(sourceName);
    }

    @Override
    public String getKey() {
        return sourceName;
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

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}
