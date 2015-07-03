package com.sathy.evlo.fragment;

import android.content.Context;

import com.sathy.evlo.activity.NewExpenseActivity;
import com.sathy.evlo.adapter.CircledCursorAdapter;
import com.sathy.evlo.adapter.ExpenseCursorAdapter;
import com.sathy.evlo.data.Expense;
import com.sathy.evlo.provider.DatabaseProvider;

public class ExpensesFragment extends FabListFragment {

    public ExpensesFragment() {
        super(NewExpenseActivity.class, DatabaseProvider.EXPENSE_URI, DatabaseProvider.EXPENSE_ITEM_TYPE);
    }

    @Override
    public CircledCursorAdapter getAdapter(Context context) {
        return new ExpenseCursorAdapter(context, this);
    }

    @Override
    public String[] getColumns() {
        return Expense.Columns;
    }
}
