package com.sathy.evlo.fragment;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;

import com.sathy.evlo.activity.NewTagActivity;
import com.sathy.evlo.activity.R;
import com.sathy.evlo.adapter.CircledCursorAdapter;
import com.sathy.evlo.adapter.TagCursorAdapter;
import com.sathy.evlo.data.Tag;
import com.sathy.evlo.provider.DatabaseProvider;

/**
 * Created by sathy on 2/7/15.
 */
public class TagsFragment extends FabListFragment {

  public TagsFragment() {
    super(NewTagActivity.class, DatabaseProvider.TAG_URI, DatabaseProvider.TAG_ITEM_TYPE);
  }

  @Override
  public CircledCursorAdapter getAdapter(Context context) {
    return new TagCursorAdapter(context, this);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    if (menu != null) {
      menu.findItem(R.id.action_search).setVisible(false);
    }
  }

  @Override
  public String[] getColumns() {
    return Tag.Columns;
  }
}
