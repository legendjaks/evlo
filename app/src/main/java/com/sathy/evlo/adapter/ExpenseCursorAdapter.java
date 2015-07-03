package com.sathy.evlo.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;

import com.sathy.evlo.activity.R;
import com.sathy.evlo.data.Expense;
import com.sathy.evlo.data.Source;
import com.sathy.evlo.holder.ExpenseViewHolder;
import com.sathy.evlo.listener.ListItemPartListener;
import com.sathy.evlo.util.TextFormat;

/**
 * Created by sathy on 28/6/15.
 */
public class ExpenseCursorAdapter extends CircledCursorAdapter {

    public ExpenseCursorAdapter(Context context, ListItemPartListener listener) {
        super(context, listener);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(R.layout.expense_item_row, parent, false);
        view.setTag(new ExpenseViewHolder(view, this));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ExpenseViewHolder holder = (ExpenseViewHolder) view.getTag();
        double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(Expense.Amount));

        holder.getId().setText(cursor.getString(cursor.getColumnIndexOrThrow(Expense.Id)));
        holder.getDate().setText(TextFormat.toDateText(cursor.getString(cursor.getColumnIndexOrThrow(Expense.ExpenseDate))));
        holder.getAmount().setText(context.getString(R.string.rs) + " " + TextFormat.toDecimalText(amount));
        holder.getNotes().setText(cursor.getString(cursor.getColumnIndexOrThrow(Expense.Notes)));
        holder.getSource().setText(cursor.getString(cursor.getColumnIndexOrThrow(Expense.SourceId)));
        holder.setSourceName(cursor.getString(cursor.getColumnIndexOrThrow(Source.Name)));

        holder.update();
    }
}
