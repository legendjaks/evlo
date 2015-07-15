package com.sathy.evlo.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sathy.evlo.activity.R;
import com.sathy.evlo.data.Expense;
import com.sathy.evlo.data.Source;
import com.sathy.evlo.holder.ExpenseViewHolder;
import com.sathy.evlo.listener.ListItemPartListener;
import com.sathy.evlo.model.ExpenseTotal;
import com.sathy.evlo.util.TextFormat;

/**
 * Created by sathy on 28/6/15.
 */
public class ExpenseCursorAdapter extends SectionedCircleCursorAdapter {

  private Context context;

  public ExpenseCursorAdapter(Context context, ListItemPartListener listener) {
    super(context, listener);
    this.context = context;
  }

  @Override
  protected Object getSectionFromCursor(Cursor cursor) {

    ExpenseTotal header = new ExpenseTotal();
    header.setTag(cursor.getString(cursor.getColumnIndexOrThrow(Expense.Tag)));
    header.setTotal(context.getString(R.string.rs) + " " + TextFormat.toDecimalText(cursor.getDouble(cursor.getColumnIndexOrThrow(Expense.Total))));

    return header;
  }

  @Override
  protected View newSectionView(Context context, Object o, ViewGroup viewGroup) {
    View view = inflater.inflate(R.layout.expense_section_header, viewGroup, false);
    view.setTag(new SectionHolder(view));
    return view;
  }

  @Override
  protected void bindSectionView(View view, Context context, int i, Object o) {
    SectionHolder holder = (SectionHolder)view.getTag();
    ExpenseTotal header = (ExpenseTotal) o;
    holder.tag.setText(header.getTag());
    holder.total.setText(header.getTotal());
  }

  @Override
  protected View newItemView(Context context, Cursor cursor, ViewGroup viewGroup) {
    View view = inflater.inflate(R.layout.expense_item_row, viewGroup, false);
    view.setTag(new ExpenseViewHolder(view, this));
    return view;
  }

  @Override
  protected void bindItemView(View view, Context context, Cursor cursor) {
    ExpenseViewHolder holder = (ExpenseViewHolder) view.getTag();
    double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(Expense.Amount));

    holder.getId().setText(cursor.getString(cursor.getColumnIndexOrThrow(Expense.Id)));
    holder.getDate().setText(TextFormat.toDateText(cursor.getString(cursor.getColumnIndexOrThrow(Expense.ExpenseDate))));
    holder.getAmount().setText(context.getString(R.string.rs) + " " + TextFormat.toDecimalText(amount));
    holder.getNotes().setText(cursor.getString(cursor.getColumnIndexOrThrow(Expense.Notes)));
    holder.getSource().setText(cursor.getString(cursor.getColumnIndexOrThrow(Expense.SourceId)));
    holder.setSourceName(cursor.getString(cursor.getColumnIndexOrThrow(Expense.Source)));

    holder.update();
  }

  private class SectionHolder {

    private TextView tag;
    private TextView total;

    public SectionHolder(View view){
      tag = (TextView) view.findViewById(R.id.row_tag);
      total = (TextView) view.findViewById(R.id.row_total);
    }
  }
}
