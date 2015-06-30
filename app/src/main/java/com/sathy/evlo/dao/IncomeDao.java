package com.sathy.evlo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.sathy.evlo.data.Income;
import com.sathy.evlo.data.Table;
import com.sathy.evlo.data.TableEntity;

/**
 * Created by sathy on 27/6/15.
 */
public class IncomeDao extends Table {

    private static final String[] Columns = new String[]{TableEntity.Id, Income.IncomeDate,
            Income.Amount, Income.Notes};

    public IncomeDao(Context context) throws SQLException {
        super(context);
    }

    @Override
    public long create(TableEntity entity) {
        ContentValues values = entity.getContentValues();
        return db.insert(Income.TableName, null, values);
    }

    @Override
    public boolean update(TableEntity entity) {
        ContentValues values = entity.getContentValues();
        return db.update(Income.TableName, values, where(entity.getId()), null) > 0;
    }

    @Override
    public boolean delete(long id) {
        return db.delete(Income.TableName, where(id), null) > 0;
    }

    @Override
    public Cursor readAll() {

        String query = " Select i._id, i.incomedate, i.amount, i.notes From incomes i ";
        return db.rawQuery(query, null);
    }

    @Override
    public TableEntity read(long id) throws SQLException {
        Income entity = null;

        Cursor cursor = db.query(true, Income.TableName, Columns, where(id), null, null, null, null,
                null);

        if (cursor.moveToFirst()) {
            entity = new Income();
            entity.setId(cursor.getLong(cursor.getColumnIndex(TableEntity.Id)));
            entity.setIncomeDate(cursor.getString(cursor.getColumnIndex(Income.IncomeDate)));
            entity.setAmount(cursor.getDouble(cursor.getColumnIndex(Income.Amount)));
            entity.setNotes(cursor.getString(cursor.getColumnIndex(Income.Notes)));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return entity;
    }
}
