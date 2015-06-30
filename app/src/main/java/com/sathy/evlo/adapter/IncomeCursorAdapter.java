package com.sathy.evlo.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sathy.evlo.activity.R;
import com.sathy.evlo.control.RoundedDrawable;
import com.sathy.evlo.data.Income;
import com.sathy.evlo.listener.ActionModeListener;
import com.sathy.evlo.util.MaterialColorGenerator;
import com.sathy.evlo.util.TextFormat;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathy on 28/6/15.
 */
public class IncomeCursorAdapter extends CursorAdapter {

    private static final MaterialColorGenerator colors = new MaterialColorGenerator();
    private static final int CIRCLE_BG = 0xff919191;
    private Map<String, Boolean> itemsChecked;

    private LayoutInflater inflater;
    private ActionModeListener listener;
    private int selected;

    public IncomeCursorAdapter(Context context, Cursor cursor, ActionModeListener listener) {
        super(context, cursor, false);
        inflater = LayoutInflater.from(context);
        itemsChecked = new HashMap<>();
        this.listener = listener;
        selected = 0;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(R.layout.income_item_row, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder holder = (ViewHolder) view.getTag();
        double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(Income.Amount));
        String note = cursor.getString(cursor.getColumnIndexOrThrow(Income.Notes));

        holder.id.setText(cursor.getString(cursor.getColumnIndexOrThrow(Income.Id)));
        holder.date.setText(TextFormat.toDateText(cursor.getString(cursor.getColumnIndexOrThrow(Income.IncomeDate))));
        holder.amount.setText(context.getString(R.string.rs) + " " + TextFormat.toDecimalText(amount));
        holder.notes.setText(note);
        holder.source.setText(cursor.getString(cursor.getColumnIndexOrThrow(Income.Source)));

        update(holder);
    }

    private void update(ViewHolder holder) {

        String key = holder.id.getText().toString();

        if (itemsChecked.containsKey(key) && itemsChecked.get(key)) {
            holder.circle.setImageDrawable(new RoundedDrawable(" ", CIRCLE_BG));
            holder.check.setVisibility(View.VISIBLE);
            holder.parent.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.circle.setImageDrawable(new RoundedDrawable(String.valueOf(holder.source.getText().charAt(0)), colors.getColor(holder.source.getText())));
            holder.check.setVisibility(View.GONE);
            holder.parent.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private class ViewHolder implements View.OnClickListener {

        private View parent;
        private ImageView circle;
        private ImageView check;
        private TextView id;
        private TextView date;
        private TextView amount;
        private TextView notes;
        private TextView source;

        private ViewHolder(View view) {
            this.parent = view;
            this.circle = (ImageView) view.findViewById(R.id.circle_char);
            this.check = (ImageView) view.findViewById(R.id.check_icon);
            this.id = (TextView) view.findViewById(R.id.row_id);
            this.date = (TextView) view.findViewById(R.id.row_date);
            this.amount = (TextView) view.findViewById(R.id.row_amount);
            this.notes = (TextView) view.findViewById(R.id.row_notes);
            this.source = (TextView) view.findViewById(R.id.row_source);

            this.circle.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            String key = id.getText().toString();
            if (!itemsChecked.containsKey(key))
                itemsChecked.put(key, false);

            boolean checked = false;
            if (itemsChecked.get(key)) {
                itemsChecked.put(key, false);
                selected--;
            } else {
                itemsChecked.put(key, true);
                checked = true;
                selected++;
            }

            if (selected < 0)
                selected = 0;

            Log.d("ICA", "Selected: " + selected);

            /*
            if (listener != null)
                listener.showActionBar(selected > 0);
            */

            listener.onItemSelected(parent, checked);

            update(ViewHolder.this);
        }
    }
}
