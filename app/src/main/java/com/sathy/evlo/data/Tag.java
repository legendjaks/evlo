package com.sathy.evlo.data;

import android.content.ContentValues;

public class Tag extends TableEntity {

    public static final String TableName = "tag";
    public static final String Order = "tag_order";
    public static final String Name = "name";

    private int order;
    private String name;

    public Tag() {
        super();
    }

    public Tag(String name) {
        this(0, 0, name);
    }

    public Tag(int order, String name) {
        this(0, order, name);
    }

    public Tag(long id, int order, String name) {
        this.id = id;
        this.order = order;
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
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
        values.put(Order, order);
        values.put(Name, name.trim());
        return values;
    }
}
