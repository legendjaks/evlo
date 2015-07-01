package com.sathy.evlo.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.sathy.evlo.data.Database;
import com.sathy.evlo.data.Income;
import com.sathy.evlo.data.Source;

/**
 * Created by sathy on 27/6/15.
 */
public class DatabaseProvider extends ContentProvider {

    private static final int INCOMES = 10;
    private static final int INCOME_ID = 11;
    private static final int SOURCES = 20;
    private static final int SOURCE_ID = 21;
    private static final String AUTHORITY = "com.sathy.evlo.provider";
    private static final String INCOME = "income";
    private static final String SOURCE = "source";

    public static final Uri INCOME_URI = Uri.parse("content://" + AUTHORITY
            + "/" + INCOME);
    public static final String INCOME_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/income";
    public static final Uri SOURCE_URI = Uri.parse("content://" + AUTHORITY
            + "/" + SOURCE);
    public static final String SOURCE_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/source";

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, INCOME, INCOMES);
        uriMatcher.addURI(AUTHORITY, INCOME + "/#", INCOME_ID);
        uriMatcher.addURI(AUTHORITY, SOURCE, SOURCES);
        uriMatcher.addURI(AUTHORITY, SOURCE + "/#", SOURCE_ID);
    }

    private Database evlo;
    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {

        evlo = new Database(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        int uriType = uriMatcher.match(uri);
        String limit = null;
        switch (uriType) {
            case INCOMES:
                queryBuilder.setTables(Income.TableName);
                if (TextUtils.isEmpty(selection)) {
                    sortOrder = Income.IncomeDate + " DESC, " + Income.Id + " DESC ";
                    limit = " 10 ";
                }
                break;
            case INCOME_ID:
                queryBuilder.setTables(Income.TableName);
                queryBuilder.appendWhere(Income.Id + "="
                        + uri.getLastPathSegment());
                break;
            case SOURCES:
                queryBuilder.setTables(Source.TableName);
                sortOrder = " name ASC ";
                break;
            case SOURCE_ID:
                queryBuilder.setTables(Source.TableName);
                queryBuilder.appendWhere(Source.Id + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if (db == null)
            db = evlo.getWritableDatabase();

        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder, limit);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = uriMatcher.match(uri);

        if (db == null)
            db = evlo.getWritableDatabase();

        long id = 0;
        switch (uriType) {
            case INCOMES:
                id = db.insert(Income.TableName, null, values);
                break;
            case SOURCES:
                id = db.insert(Source.TableName, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(INCOME + "/" + id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int uriType = uriMatcher.match(uri);
        if (db == null)
            db = evlo.getWritableDatabase();

        int rowsUpdated = 0;
        String id = null;
        switch (uriType) {
            case INCOME_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(Income.TableName,
                            values,
                            Income.Id + "=" + id,
                            null);
                } else {
                    rowsUpdated = db.update(Income.TableName,
                            values,
                            Income.Id + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;

            case SOURCE_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(Source.TableName,
                            values,
                            Source.Id + "=" + id,
                            null);
                } else {
                    rowsUpdated = db.update(Source.TableName,
                            values,
                            Source.Id + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int uriType = uriMatcher.match(uri);
        if (db == null)
            db = evlo.getWritableDatabase();

        int rowsDeleted = 0;
        switch (uriType) {
            case INCOMES:
                for (String id : selectionArgs) {
                    db.delete(Income.TableName, Income.Id + "=" + id,
                            null);
                    rowsDeleted++;
                }
                break;
            case SOURCES:
                for (String id : selectionArgs) {
                    db.delete(Source.TableName, Source.Id + "=" + id,
                            null);
                    rowsDeleted++;
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }
}
