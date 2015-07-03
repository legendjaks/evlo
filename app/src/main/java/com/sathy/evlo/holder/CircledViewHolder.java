package com.sathy.evlo.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sathy.evlo.activity.R;
import com.sathy.evlo.adapter.CircledCursorAdapter;
import com.sathy.evlo.control.RoundedDrawable;
import com.sathy.evlo.util.MaterialColorGenerator;

import java.util.Map;

/**
 * Created by sathy on 2/7/15.
 */
public abstract class CircledViewHolder implements View.OnClickListener {

    private static final int CIRCLE_BG = 0xff919191;

    private View parent;
    private ImageView circle;
    private ImageView check;
    private TextView id;

    private CircledCursorAdapter adapter;

    public CircledViewHolder(View view, CircledCursorAdapter adapter) {
        this.parent = view;
        this.circle = (ImageView) view.findViewById(R.id.circle_char);
        this.check = (ImageView) view.findViewById(R.id.check_icon);
        this.id = (TextView) view.findViewById(R.id.row_id);

        this.circle.setOnClickListener(this);
        this.adapter = adapter;
    }

    @Override
    public void onClick(View view) {

        Map<String, Boolean> itemsChecked = adapter.getItemsChecked();

        String key = id.getText().toString();
        if (!itemsChecked.containsKey(key))
            itemsChecked.put(key, false);

        boolean checked = !itemsChecked.get(key);
        itemsChecked.put(key, checked);
        adapter.getListItemPartListener().onItemSelected(parent, checked);

        update();
    }

    public void update() {

        Map<String, Boolean> itemsChecked = adapter.getItemsChecked();
        String key = id.getText().toString();

        if (itemsChecked.containsKey(key) && itemsChecked.get(key)) {
            circle.setImageDrawable(new RoundedDrawable(" ", CIRCLE_BG));
            check.setVisibility(View.VISIBLE);
            parent.setBackgroundColor(Color.LTGRAY);
        } else {
            circle.setImageDrawable(new RoundedDrawable(getSymbol(), MaterialColorGenerator.get(getKey())));
            check.setVisibility(View.GONE);
            parent.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public TextView getId() {
        return id;
    }

    abstract public String getSymbol();

    abstract public String getKey();
}
