package com.sathy.evlo.fragment;

import android.content.Context;

import com.sathy.evlo.activity.NewIncomeActivity;
import com.sathy.evlo.adapter.CircledCursorAdapter;
import com.sathy.evlo.adapter.IncomeCursorAdapter;
import com.sathy.evlo.data.Income;
import com.sathy.evlo.provider.DatabaseProvider;

public class IncomesFragment extends FabListFragment {

    public IncomesFragment() {
        super(NewIncomeActivity.class, DatabaseProvider.INCOME_URI, DatabaseProvider.INCOME_ITEM_TYPE);
    }

    @Override
    public CircledCursorAdapter getAdapter(Context context) {
        return new IncomeCursorAdapter(context, this);
    }

    @Override
    public String[] getColumns() {
        return Income.Columns;
    }
}
