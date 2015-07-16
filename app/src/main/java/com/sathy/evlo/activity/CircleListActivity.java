package com.sathy.evlo.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sathy.evlo.listener.ListItemPartListener;
import com.sathy.evlo.listener.MultiSelectable;

public abstract class CircleListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, ActionMode.Callback, ListItemPartListener, AdapterView.OnItemClickListener {

  private ListView listView;
  private ActionMode actionMode;

  private CursorAdapter adapter;
  private boolean itemsDeleted;

  private Class activity;
  private Uri uri;
  private String itemType;

  public CircleListActivity(Class editActivity, Uri uri, String itemType) {
    super();
    this.activity = editActivity;
    this.uri = uri;
    this.itemType = itemType;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.search_results);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

    listView = (ListView) findViewById(android.R.id.list);
    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    listView.setOnItemClickListener(this);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    populate();
  }

  public abstract CursorAdapter getAdapter(Context context);

  public abstract String[] getColumns();

  public abstract String getCriteria();

  public abstract void preprocess(Cursor cursor);

  private void populate() {

    getSupportLoaderManager().initLoader(0, null, this);
    adapter = getAdapter(this);
    listView.setAdapter(adapter);
  }

  @Override
  public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
    edit(id);
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {

    CursorLoader cursorLoader = new CursorLoader(this,
        uri, getColumns(), getCriteria(), null, null);
    return cursorLoader;
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    preprocess(data);
    adapter.swapCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    adapter.swapCursor(null);
  }

  protected void edit(long id) {

    Uri uri = Uri.parse(this.uri + "/" + id);
    Intent intent = new Intent(this, activity);
    intent.putExtra(itemType, uri);
    startActivity(intent);

    int pos = -1;
    for (int i = 0; i < adapter.getCount(); i++) {
      if (adapter.getItemId(i) == id) {
        pos = i;
        break;
      }
    }
    listView.setItemChecked(pos, false);
  }

  @Override
  public void onItemSelected(View view, boolean status) {

    int index = listView.getPositionForView(view);
    listView.setItemChecked(index, status);

    long[] ids = listView.getCheckedItemIds();
    if ((ids == null || ids.length == 0) && actionMode != null) {
      actionMode.finish();
    } else if (actionMode == null) {
      actionMode = startSupportActionMode(this);
    }
  }

  // Called when the action mode is created; startActionMode() was called
  @Override
  public boolean onCreateActionMode(ActionMode mode, Menu menu) {

    itemsDeleted = false;
    // Inflate a menu resource providing context menu items
    MenuInflater inflater = mode.getMenuInflater();
    inflater.inflate(R.menu.delete, menu);
    return true;
  }

  // Called each time the action mode is shown. Always called after onCreateActionMode, but
  // may be called multiple times if the mode is invalidated.
  @Override
  public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
    return false; // Return false if nothing is done
  }

  // Called when the user selects a contextual menu item
  @Override
  public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_delete:

        long[] ids = listView.getCheckedItemIds();
        if (ids != null && ids.length > 0) {
          String[] args = new String[ids.length];
          for (int i = 0; i < ids.length; i++) {
            args[i] = String.valueOf(ids[i]);
          }

          getContentResolver().delete(uri, null, args);
          Toast.makeText(this, ids.length + " items deleted", Toast.LENGTH_SHORT).show();
          itemsDeleted = true;
        }

        mode.finish(); // Action picked, so close the CAB
        return true;
      default:
        return false;
    }
  }

  // Called when the user exits the action mode
  @Override
  public void onDestroyActionMode(ActionMode mode) {

    actionMode = null;
    if (itemsDeleted)
      return;

    long[] ids = listView.getCheckedItemIds();
    if (ids != null && ids.length > 0) {
      ((MultiSelectable) adapter).clear();
      listView.clearChoices();
      getSupportLoaderManager().restartLoader(0, null, this);
    }
  }

  public void onSearch() {
  }
}
