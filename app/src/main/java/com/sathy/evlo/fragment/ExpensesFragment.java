package com.sathy.evlo.fragment;

import android.content.Context;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.sathy.evlo.activity.NewExpenseActivity;
import com.sathy.evlo.adapter.CircledCursorAdapter;
import com.sathy.evlo.adapter.ExpenseCursorAdapter;
import com.sathy.evlo.data.Expense;
import com.sathy.evlo.provider.DatabaseProvider;
import com.twotoasters.sectioncursoradapter.SectionCursorAdapter;

public class ExpensesFragment extends FabListFragment {

  public ExpensesFragment() {
    super(NewExpenseActivity.class, DatabaseProvider.EXPENSE_URI, DatabaseProvider.EXPENSE_ITEM_TYPE);
  }

  @Override
  public CursorAdapter getAdapter(Context context) {
    return new ExpenseCursorAdapter(context, this);
  }

  @Override
  public String[] getColumns() {
    return Expense.Columns;
  }

  @Override
  public void onListItemClick(ListView listView, View view, int position, long id) {

    ExpenseCursorAdapter adapter = (ExpenseCursorAdapter) listView.getAdapter();
    Object sectionObject = adapter.getItem(position);
    int cursorPosition = adapter.getCursorPositionWithoutSections(position);

    if (adapter.isSection(position) && sectionObject != null) {
      // Handle the section being clicked on.
      return;
    } else if (cursorPosition != SectionCursorAdapter.NO_CURSOR_POSITION) {
      // Handle the cursor item being clicked on.
      long _id = adapter.getItemId(position);
      edit(_id);
    }
  }
}
