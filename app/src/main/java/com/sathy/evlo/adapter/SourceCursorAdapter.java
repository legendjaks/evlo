package com.sathy.evlo.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;

import com.sathy.evlo.activity.R;
import com.sathy.evlo.data.Source;
import com.sathy.evlo.data.TableEntity;
import com.sathy.evlo.holder.SourceViewHolder;
import com.sathy.evlo.listener.ListItemPartListener;

/**
 * Created by sathy on 28/6/15.
 */
public class SourceCursorAdapter extends CircledCursorAdapter {

  public SourceCursorAdapter(Context context, ListItemPartListener listener) {
    super(context, listener);
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    View view = inflater.inflate(R.layout.source_item_row, parent, false);
    view.setTag(new SourceViewHolder(view, this));
    return view;
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {

    SourceViewHolder holder = (SourceViewHolder) view.getTag();

    holder.getId().setText(cursor.getString(cursor.getColumnIndexOrThrow(TableEntity.Id)));
    holder.getName().setText(cursor.getString(cursor.getColumnIndexOrThrow(Source.Name)));
    holder.update();
  }
}
