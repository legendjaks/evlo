package com.sathy.evlo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CursorAdapter;

import com.sathy.evlo.listener.ListItemPartListener;
import com.sathy.evlo.listener.MultiSelectable;
import com.twotoasters.sectioncursoradapter.SectionCursorAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sathy on 14/07/15.
 */
public abstract class SectionedCircleCursorAdapter extends SectionCursorAdapter implements MultiSelectable {

  protected LayoutInflater inflater;
  private Map<String, Boolean> itemsChecked;
  private ListItemPartListener listener;

  public SectionedCircleCursorAdapter(Context context, ListItemPartListener listener) {
    super(context, null, 0);
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
