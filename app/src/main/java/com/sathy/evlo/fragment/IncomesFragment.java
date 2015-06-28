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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sathy.evlo.activity.NewIncomeActivity;
import com.sathy.evlo.activity.R;
import com.sathy.evlo.adapter.IncomeCursorAdapter;
import com.sathy.evlo.data.Income;
import com.sathy.evlo.provider.DatabaseProvider;

public class IncomesFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private FloatingActionButton add;
    private View view;

    private IncomeCursorAdapter adapter;
    private static final String[] tableColumns = new String[] { Income.Id, Income.IncomeDate, Income.Amount, Income.Notes
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
        adapter = new IncomeCursorAdapter(view.getContext(), null);
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
}
