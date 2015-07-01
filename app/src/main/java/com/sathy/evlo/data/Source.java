package com.sathy.evlo.data;

import android.content.ContentValues;

/**
 * Created by sathy on 29/6/15.
 */
public class Source extends TableEntity {

    public static final String TableName = "source";
    public static final String Name = "name";
    public static final String[] Columns = new String[]{Id, Name};

    private String name;

    public Source() {
        super();
    }

    public Source(String name) {
        this(0, name);
    }

    public Source(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(Name, name.trim());
        return values;
    }
}
