package com.sathy.evlo.listener;

import java.util.Map;

/**
 * Created by sathy on 14/07/15.
 */
public interface MultiSelectable {
  void clear();
  Map<String, Boolean> getItemsChecked();
  ListItemPartListener getListItemPartListener();
}
