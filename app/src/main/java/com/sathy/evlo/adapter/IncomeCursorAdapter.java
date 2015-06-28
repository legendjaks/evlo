package com.sathy.evlo.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sathy.evlo.activity.R;
import com.sathy.evlo.control.RoundedDrawable;
import com.sathy.evlo.data.Income;
import com.sathy.evlo.util.MaterialColorGenerator;
import com.sathy.evlo.util.TextFormat;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathy on 28/6/15.
 */
public class IncomeCursorAdapter extends CursorAdapter {

    private static final MaterialColorGenerator colors = new MaterialColorGenerator();
    private Map<String, Boolean> itemsChecked;

    private LayoutInflater inflater;

    public IncomeCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, false);
        inflater = LayoutInflater.from(context);
        itemsChecked = new HashMap<>();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(R.layout.income_row, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder holder = (ViewHolder) view.getTag();
        String id = cursor.getString(cursor.getColumnIndexOrThrow(Income.Id));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(Income.IncomeDate));
        String amount = cursor.getString(cursor.getColumnIndexOrThrow(Income.Amount));
        String note = cursor.getString(cursor.getColumnIndexOrThrow(Income.Notes));

        holder.id.setText(id);
        holder.date.setText(TextFormat.toDateText(date));
        holder.amount.setText(TextFormat.toDecimalText(amount));
        holder.notes.setText(note);

        update(holder);
    }

    private void update(ViewHolder holder){

        String key = holder.id.getText().toString();

        if(itemsChecked.containsKey(key) && itemsChecked.get(key)){
            holder.circle.setImageDrawable(new RoundedDrawable(" ", 0xff919191));
            holder.check.setVisibility(View.VISIBLE);
        } else {
            holder.circle.setImageDrawable(new RoundedDrawable(String.valueOf(holder.notes.getText().charAt(0)), colors.getColor(holder.notes.getText())));
            holder.check.setVisibility(View.GONE);
        }
    }

    private class ViewHolder implements View.OnClickListener {

        private ImageView circle;
        private ImageView check;
        private TextView id;
        private TextView date;
        private TextView amount;
        private TextView notes;

        private ViewHolder(View view) {
            this.circle = (ImageView) view.findViewById(R.id.circle_char);
            this.check = (ImageView) view.findViewById(R.id.check_icon);
            this.id = (TextView) view.findViewById(R.id.row_id);
            this.date = (TextView) view.findViewById(R.id.row_date);
            this.amount = (TextView) view.findViewById(R.id.row_amount);
            this.notes = (TextView) view.findViewById(R.id.row_notes);

            this.circle.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            String key = id.getText().toString();
            if(!itemsChecked.containsKey(key))
                itemsChecked.put(key, false);

            if(itemsChecked.get(key))
                itemsChecked.put(key, false);
            else
                itemsChecked.put(key, true);

            update(ViewHolder.this);
        }
    }
}
