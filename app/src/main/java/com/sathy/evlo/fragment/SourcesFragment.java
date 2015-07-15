package com.sathy.evlo.fragment;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;

import com.sathy.evlo.activity.NewSourceActivity;
import com.sathy.evlo.activity.R;
import com.sathy.evlo.adapter.CircledCursorAdapter;
import com.sathy.evlo.adapter.SourceCursorAdapter;
import com.sathy.evlo.data.Source;
import com.sathy.evlo.provider.DatabaseProvider;

/**
 * Created by sathy on 2/7/15.
 */
public class SourcesFragment extends FabListFragment {

  public SourcesFragment() {
    super(NewSourceActivity.class, DatabaseProvider.SOURCE_URI, DatabaseProvider.SOURCE_ITEM_TYPE);
  }

  @Override
  public CircledCursorAdapter getAdapter(Context context) {
    return new SourceCursorAdapter(context, this);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    if (menu != null) {
      menu.findItem(R.id.action_search).setVisible(false);
    }
  }

  @Override
  public String[] getColumns() {
    return Source.Columns;
  }
}
