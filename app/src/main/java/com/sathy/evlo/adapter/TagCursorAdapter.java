package com.sathy.evlo.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;

import com.sathy.evlo.activity.R;
import com.sathy.evlo.data.TableEntity;
import com.sathy.evlo.data.Tag;
import com.sathy.evlo.holder.TagViewHolder;
import com.sathy.evlo.listener.ListItemPartListener;

/**
 * Created by sathy on 28/6/15.
 */
public class TagCursorAdapter extends CircledCursorAdapter {

    public TagCursorAdapter(Context context, ListItemPartListener listener) {
        super(context, listener);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(R.layout.tag_item_row, parent, false);
        view.setTag(new TagViewHolder(view, this));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TagViewHolder holder = (TagViewHolder) view.getTag();

        holder.getId().setText(cursor.getString(cursor.getColumnIndexOrThrow(TableEntity.Id)));
        holder.getOrder().setText(cursor.getString(cursor.getColumnIndexOrThrow(Tag.Order)));
        holder.getName().setText(cursor.getString(cursor.getColumnIndexOrThrow(Tag.Name)));
        holder.update();
    }
}
