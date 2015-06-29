package com.sathy.evlo.data;

import android.content.ContentValues;

/**
 * Created by sathy on 27/6/15.
 */
public abstract class TableEntity {

    public static final String Id = "_id";
    protected long id;

    public TableEntity() {
        id = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public abstract ContentValues getContentValues();
}
