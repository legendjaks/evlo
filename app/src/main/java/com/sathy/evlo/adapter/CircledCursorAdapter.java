package com.sathy.evlo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CursorAdapter;

import com.sathy.evlo.listener.ListItemPartListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathy on 28/6/15.
 */
public abstract class CircledCursorAdapter extends CursorAdapter {

    protected LayoutInflater inflater;
    private Map<String, Boolean> itemsChecked;
    private ListItemPartListener listener;

    public CircledCursorAdapter(Context context, ListItemPartListener listener) {
        super(context, null, false);
        inflater = LayoutInflater.from(context);
        itemsChecked = new HashMap<>();
        this.listener = listener;
    }

    public void clear() {
        itemsChecked.clear();
    }

    public Map<String, Boolean> getItemsChecked() {
        return itemsChecked;
    }

    public ListItemPartListener getListItemPartListener() {
        return listener;
    }
}
