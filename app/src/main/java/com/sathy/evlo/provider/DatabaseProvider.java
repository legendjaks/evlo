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

/**
 * Created by sathy on 27/6/15.
 */
public class DatabaseProvider extends ContentProvider {

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/income";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/income";
    private static final int INCOMES = 10;
    private static final int INCOME_ID = 20;
    private static final String AUTHORITY = "com.sathy.evlo.provider";
    private static final String BASE_PATH = "income";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, INCOMES);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", INCOME_ID);
    }

    private Database evlo;
    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {

        evlo = new Database(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // Set the table
        queryBuilder.setTables(Income.TableName);

        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case INCOMES:
                break;
            case INCOME_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(Income.Id + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if (db == null)
            db = evlo.getWritableDatabase();

        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
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
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
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
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int uriType = uriMatcher.match(uri);
        if (db == null)
            db = evlo.getWritableDatabase();

        int rowsUpdated = 0;
        switch (uriType) {
            case INCOMES:
                return 0;
            case INCOME_ID:
                String id = uri.getLastPathSegment();
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
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
