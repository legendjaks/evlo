package com.sathy.evlo.adapter;

import android.view.View;
import android.widget.TextView;

import com.sathy.evlo.activity.R;
import com.sathy.evlo.util.TextFormat;

/**
 * Created by sathy on 2/7/15.
 */
public class SourceViewHolder extends CircledViewHolder {

    private TextView name;

    public SourceViewHolder(View view, CircledCursorAdapter adapter) {
        super(view, adapter);
        this.name = (TextView) view.findViewById(R.id.row_name);
    }

    @Override
    public String getSymbol() {
        return TextFormat.getSourceSymbol(name.getText().toString());
    }

    @Override
    public String getKey() {
        return name.getText().toString();
    }

    public TextView getName() {
        return name;
    }
}
