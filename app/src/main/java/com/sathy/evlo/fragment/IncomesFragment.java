package com.sathy.evlo.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.sathy.evlo.activity.NewExpenseActivity;
import com.sathy.evlo.activity.NewIncomeActivity;
import com.sathy.evlo.activity.R;
import com.sathy.evlo.dao.IncomeDao;
import com.sathy.evlo.data.Income;
import com.sathy.evlo.data.TableEntity;
import com.sathy.evlo.provider.DatabaseProvider;

import java.text.DecimalFormat;

public class IncomesFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final DecimalFormat decimalformat = new DecimalFormat("##,##,##,##0.00");

    private FloatingActionButton add;
    private View view;

    private SimpleCursorAdapter adapter;
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

        int[] columnNames = new int[] { R.id.row_id, R.id.row_date, R.id.row_amount, R.id.row_notes,
                 };

        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(view.getContext(), R.layout.income_row, null, tableColumns,
                columnNames, 0) {
            @Override
            public void setViewText(TextView view, String text) {
                super.setViewText(view, formatText(view, text));
            }
        };

        setListAdapter(adapter);
    }

    private String formatText(TextView view, String text) {
        /*switch (view.getId()) {
            case R.id.row_amount:
                double amount = filteredIncomes.getDouble(filteredIncomes.getColumnIndex(Income.Amount));
                return toDecimalText(amount);
        }*/
        return text;
    }

    public static String toDecimalText(double value) {
        return decimalformat.format(value);
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
        //startActivityForResult(intent, 0);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if (resultCode != RESULT_OK)
            return;
        //filteredIncomes.requery();
    }
}
