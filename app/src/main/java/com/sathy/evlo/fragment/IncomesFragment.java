package com.sathy.evlo.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ListView;

import com.sathy.evlo.activity.NewIncomeActivity;
import com.sathy.evlo.activity.R;
import com.sathy.evlo.adapter.IncomeCursorAdapter;
import com.sathy.evlo.data.Income;
import com.sathy.evlo.listener.ActionModeListener;
import com.sathy.evlo.provider.DatabaseProvider;

public class IncomesFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, ActionModeListener {

    private FloatingActionButton add;
    private View view;
    private ListView listView;
    private ActionMode actionMode;

    private IncomeCursorAdapter adapter;
    private static final String[] tableColumns = new String[]{Income.Id, Income.IncomeDate, Income.Amount, Income.Source, Income.Notes
    };

    public IncomesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        view = getView();
        listView = (ListView) view.findViewById(android.R.id.list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        add = (FloatingActionButton) view.findViewById(R.id.fab);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewIncomeActivity.class);
                startActivity(intent);
            }
        });

        populate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.incomes, container, false);
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    private void populate() {

        getLoaderManager().initLoader(0, null, this);
        adapter = new IncomeCursorAdapter(view.getContext(), null, this);
        setListAdapter(adapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        CursorLoader cursorLoader = new CursorLoader(view.getContext(),
                DatabaseProvider.CONTENT_URI, tableColumns, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        editIncome(id);
    }

    private void editIncome(long id) {

        Uri uri = Uri.parse(DatabaseProvider.CONTENT_URI + "/" + id);
        Intent intent = new Intent(getActivity(), NewIncomeActivity.class);
        intent.putExtra(DatabaseProvider.CONTENT_ITEM_TYPE, uri);
        startActivity(intent);
    }

    private ActionMode.Callback amCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
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
                    Log.d("CAB", "delete called");
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
        }
    };

    @Override
    public void showActionBar(boolean flag) {
        Log.d("IF", "SAB: " + flag);
        if (flag) {
            if (actionMode == null) {
                AppCompatActivity activity = (AppCompatActivity) getActivity();
                actionMode = activity.startSupportActionMode(amCallback);
            }
        } else {
            if (actionMode != null)
                actionMode.finish();
        }
    }

    @Override
    public void onItemSelected(View view, boolean status) {
        Log.d("IF", "Selected Index: " + listView.getPositionForView(view));
        int index = listView.getPositionForView(view);
        listView.setItemChecked(index, status);

        long[] ids = listView.getCheckedItemIds();
        if (ids == null || ids.length == 0) {
            if (actionMode != null)
                actionMode.finish();
        } else {
            if (actionMode == null) {
                AppCompatActivity activity = (AppCompatActivity) getActivity();
                actionMode = activity.startSupportActionMode(amCallback);
            }
        }
    }
}
