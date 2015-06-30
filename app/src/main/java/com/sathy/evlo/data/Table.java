package com.sathy.evlo.data;

/**
 * Created by sathy on 27/6/15.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public abstract class Table {

    private final Database evlo;
    protected SQLiteDatabase db;

    public Table(Context context) throws SQLException {

        evlo = new Database(context);
        db = evlo.getWritableDatabase();
    }

    public void close() {
        evlo.close();
    }

    public abstract long create(TableEntity entity);

    public abstract boolean update(TableEntity entity);

    public abstract boolean delete(long id);

    public abstract Cursor readAll();

    public abstract TableEntity read(long id) throws SQLException;

    protected String where(long id) {
        return TableEntity.Id + "=" + id;
    }
}
