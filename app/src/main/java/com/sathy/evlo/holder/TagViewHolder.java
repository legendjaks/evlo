package com.sathy.evlo.holder;


import android.view.View;
import android.widget.TextView;

import com.sathy.evlo.activity.R;
import com.sathy.evlo.adapter.CircledCursorAdapter;

/**
 * Created by sathy on 2/7/15.
 */
public class TagViewHolder extends CircledViewHolder {

  private TextView name;

  public TagViewHolder(View view, CircledCursorAdapter adapter) {
    super(view, adapter);
    this.name = (TextView) view.findViewById(R.id.row_name);
  }

  @Override
  public String getSymbol() {
    return String.valueOf(name.getText().charAt(0));
  }

  @Override
  public String getKey() {
    return name.getText().toString();
  }

  public TextView getName() {
    return name;
  }
}
