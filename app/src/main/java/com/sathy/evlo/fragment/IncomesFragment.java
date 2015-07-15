package com.sathy.evlo.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.CursorAdapter;

import com.sathy.evlo.activity.NewIncomeActivity;
import com.sathy.evlo.activity.SearchIncomeActivity;
import com.sathy.evlo.adapter.CircledCursorAdapter;
import com.sathy.evlo.adapter.IncomeCursorAdapter;
import com.sathy.evlo.data.Income;
import com.sathy.evlo.provider.DatabaseProvider;

public class IncomesFragment extends FabListFragment {

  public IncomesFragment() {
    super(NewIncomeActivity.class, DatabaseProvider.INCOME_URI, DatabaseProvider.INCOME_ITEM_TYPE);
  }

  @Override
  public CursorAdapter getAdapter(Context context) {
    return new IncomeCursorAdapter(context, this);
  }

  @Override
  public String[] getColumns() {
    return Income.Columns;
  }

  @Override
  public void onSearch() {
    Intent intent = new Intent(getActivity(), SearchIncomeActivity.class);
    startActivity(intent);
  }
}
