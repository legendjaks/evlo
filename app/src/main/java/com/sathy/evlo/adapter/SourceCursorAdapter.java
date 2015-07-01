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
import com.sathy.evlo.data.Source;
import com.sathy.evlo.listener.ListItemPartListener;
import com.sathy.evlo.util.MaterialColorGenerator;
import com.sathy.evlo.util.TextFormat;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathy on 28/6/15.
 */
public class SourceCursorAdapter extends CursorAdapter {

    private static final MaterialColorGenerator colors = new MaterialColorGenerator();
    private static final int CIRCLE_BG = 0xff919191;
    private Map<String, Boolean> itemsChecked;

    private LayoutInflater inflater;
    private ListItemPartListener listener;

    public SourceCursorAdapter(Context context, Cursor cursor, ListItemPartListener listener) {
        super(context, cursor, false);
        inflater = LayoutInflater.from(context);
        itemsChecked = new HashMap<>();
        this.listener = listener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(R.layout.source_item_row, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder holder = (ViewHolder) view.getTag();

        holder.id.setText(cursor.getString(cursor.getColumnIndexOrThrow(Income.Id)));
        holder.name.setText(cursor.getString(cursor.getColumnIndexOrThrow(Source.Name)));
        update(holder);
    }

    public void clear() {
        itemsChecked.clear();
    }

    private void update(ViewHolder holder) {

        String key = holder.id.getText().toString();

        if (itemsChecked.containsKey(key) && itemsChecked.get(key)) {
            holder.circle.setImageDrawable(new RoundedDrawable(" ", CIRCLE_BG));
            holder.check.setVisibility(View.VISIBLE);
            holder.parent.setBackgroundColor(Color.LTGRAY);
        } else {
            String name = holder.name.getText().toString();
            String symbol = TextFormat.getSourceSymbol(name);
            holder.circle.setImageDrawable(new RoundedDrawable(symbol, colors.getColor(name)));
            holder.check.setVisibility(View.GONE);
            holder.parent.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private class ViewHolder implements View.OnClickListener {

        private View parent;
        private ImageView circle;
        private ImageView check;
        private TextView id;
        private TextView name;

        private ViewHolder(View view) {
            this.parent = view;
            this.circle = (ImageView) view.findViewById(R.id.circle_char);
            this.check = (ImageView) view.findViewById(R.id.check_icon);
            this.id = (TextView) view.findViewById(R.id.row_id);
            this.name = (TextView) view.findViewById(R.id.row_name);

            this.circle.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            String key = id.getText().toString();
            if (!itemsChecked.containsKey(key))
                itemsChecked.put(key, false);

            boolean checked = !itemsChecked.get(key);
            itemsChecked.put(key, checked);
            listener.onItemSelected(parent, checked);

            update(ViewHolder.this);
        }
    }
}
