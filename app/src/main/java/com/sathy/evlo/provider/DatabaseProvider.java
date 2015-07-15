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
import android.util.Log;

import com.sathy.evlo.data.Database;
import com.sathy.evlo.data.Expense;
import com.sathy.evlo.data.Income;
import com.sathy.evlo.data.Source;
import com.sathy.evlo.data.Tag;

/**
 * Created by sathy on 27/6/15.
 */
public class DatabaseProvider extends ContentProvider {

  private static final int INCOMES = 10;
  private static final int INCOME_ID = 11;
  private static final int SOURCES = 20;
  private static final int SOURCE_ID = 21;
  private static final int TAGS = 30;
  private static final int TAG_ID = 31;
  private static final int EXPENSES = 40;
  private static final int EXPENSE_ID = 41;

  private static final String AUTHORITY = "com.sathy.evlo.provider";
  private static final String INCOME = "income";
  public static final Uri INCOME_URI = Uri.parse("content://" + AUTHORITY
      + "/" + INCOME);
  public static final String INCOME_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
      + "/" + INCOME;
  private static final String SOURCE = "source";
  public static final Uri SOURCE_URI = Uri.parse("content://" + AUTHORITY
      + "/" + SOURCE);
  public static final String SOURCE_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
      + "/" + SOURCE;
  private static final String TAG = "tag";
  public static final Uri TAG_URI = Uri.parse("content://" + AUTHORITY
      + "/" + TAG);
  public static final String TAG_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
      + "/" + TAG;
  private static final String EXPENSE = "expense";
  public static final Uri EXPENSE_URI = Uri.parse("content://" + AUTHORITY
      + "/" + EXPENSE);
  public static final String EXPENSE_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
      + "/" + EXPENSE;

  private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

  static {
    uriMatcher.addURI(AUTHORITY, INCOME, INCOMES);
    uriMatcher.addURI(AUTHORITY, INCOME + "/#", INCOME_ID);
    uriMatcher.addURI(AUTHORITY, SOURCE, SOURCES);
    uriMatcher.addURI(AUTHORITY, SOURCE + "/#", SOURCE_ID);
    uriMatcher.addURI(AUTHORITY, TAG, TAGS);
    uriMatcher.addURI(AUTHORITY, TAG + "/#", TAG_ID);
    uriMatcher.addURI(AUTHORITY, EXPENSE, EXPENSES);
    uriMatcher.addURI(AUTHORITY, EXPENSE + "/#", EXPENSE_ID);
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
    String text = null;
    switch (uriType) {
      case INCOMES:
        queryBuilder.setTables(Income.TableName);
        sortOrder = Income.IncomeDate + " DESC, " + Income.Id + " DESC ";

        if (TextUtils.isEmpty(selection))
          limit = " 10 ";

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
      case TAGS:
        queryBuilder.setTables(Tag.TableName);
        sortOrder = " name ASC ";
        break;
      case TAG_ID:
        queryBuilder.setTables(Tag.TableName);
        queryBuilder.appendWhere(Tag.Id + "="
            + uri.getLastPathSegment());
        break;
      case EXPENSES:

        text = "SELECT e._id, e.expense_date, e.source_id, e.tag_id, e.amount, e.notes, s.name as source, t.name as tag";
        text = text + ", (Select total(amount) From Expense where tag_id = e.tag_id) as total ";
        text = text + " From expense e INNER JOIN source s ON e.source_id = s._id INNER JOIN tag t ON e.tag_id = t._id ";
        text = text + " Order By t.name ASC, e.expense_date DESC LIMIT 20";
        break;
      case EXPENSE_ID:
        queryBuilder.setTables(Expense.TableName);
        queryBuilder.appendWhere(Expense.Id + "="
            + uri.getLastPathSegment());
        break;
      default:
        throw new IllegalArgumentException("Unknown URI: " + uri);
    }

    if (db == null)
      db = evlo.getWritableDatabase();

    Cursor cursor = null;
    if (text != null) {
      cursor = db.rawQuery(text, null);
    } else {
      cursor = queryBuilder.query(db, projection, selection,
          selectionArgs, null, null, sortOrder, limit);
    }

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
      case TAGS:
        id = db.insert(Tag.TableName, null, values);
        break;
      case EXPENSES:
        id = db.insert(Expense.TableName, null, values);
        break;
      default:
        throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    // doesn't matter. returned value is not used
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
        rowsUpdated = db.update(Income.TableName,
            values,
            Income.Id + "=" + id,
            null);
        break;

      case SOURCE_ID:
        id = uri.getLastPathSegment();
        rowsUpdated = db.update(Source.TableName,
            values,
            Source.Id + "=" + id,
            null);
        break;
      case TAG_ID:
        id = uri.getLastPathSegment();
        rowsUpdated = db.update(Tag.TableName,
            values,
            Tag.Id + "=" + id,
            null);
        break;
      case EXPENSE_ID:
        id = uri.getLastPathSegment();
        rowsUpdated = db.update(Expense.TableName,
            values,
            Expense.Id + "=" + id,
            null);
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
      case TAGS:
        for (String id : selectionArgs) {
          db.delete(Tag.TableName, Tag.Id + "=" + id,
              null);
          rowsDeleted++;
        }
        break;
      case EXPENSES:
        for (String id : selectionArgs) {
          db.delete(Expense.TableName, Expense.Id + "=" + id,
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
