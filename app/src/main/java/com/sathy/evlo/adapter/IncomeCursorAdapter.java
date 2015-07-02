package com.sathy.evlo.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;

import com.sathy.evlo.activity.R;
import com.sathy.evlo.data.Income;
import com.sathy.evlo.listener.ListItemPartListener;
import com.sathy.evlo.util.TextFormat;

/**
 * Created by sathy on 28/6/15.
 */
public class IncomeCursorAdapter extends CircledCursorAdapter {

    public IncomeCursorAdapter(Context context, ListItemPartListener listener) {
        super(context, listener);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(R.layout.income_item_row, parent, false);
        view.setTag(new IncomeViewHolder(view, this));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        IncomeViewHolder holder = (IncomeViewHolder) view.getTag();
        double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(Income.Amount));

        holder.getId().setText(cursor.getString(cursor.getColumnIndexOrThrow(Income.Id)));
        holder.getDate().setText(TextFormat.toDateText(cursor.getString(cursor.getColumnIndexOrThrow(Income.IncomeDate))));
        holder.getAmount().setText(context.getString(R.string.rs) + " " + TextFormat.toDecimalText(amount));
        holder.getNotes().setText(cursor.getString(cursor.getColumnIndexOrThrow(Income.Notes)));
        holder.getSource().setText(cursor.getString(cursor.getColumnIndexOrThrow(Income.Source)));

        holder.update();
    }
}
