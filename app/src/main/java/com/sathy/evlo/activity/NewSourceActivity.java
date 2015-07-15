package com.sathy.evlo.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.sathy.evlo.data.Source;
import com.sathy.evlo.provider.DatabaseProvider;

/**
 * Created by sathy on 24/6/15.
 */
public class NewSourceActivity extends AppCompatActivity {

  private Toolbar toolbar;
  private EditText name;

  private Uri uri;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.new_source);

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    name = (EditText) findViewById(R.id.name);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      setTitle(R.string.edit_source);
      uri = extras.getParcelable(DatabaseProvider.SOURCE_ITEM_TYPE);
      populate();
    } else
      setTitle(R.string.new_source);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.save, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    if (id == R.id.action_save) {
      if (save())
        this.finish();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void populate() {
    if (uri == null) {
      return;
    }

    Cursor cursor = getContentResolver().query(uri, Source.Columns, null, null,
        null);
    if (cursor != null) {
      cursor.moveToFirst();
      name.setText(cursor.getString(cursor
          .getColumnIndexOrThrow(Source.Name)));
      // always close the cursor
      cursor.close();
    }
  }

  private boolean save() {

    if (name.getText().toString().trim().length() == 0)
      return false;

    ContentValues values = new ContentValues();
    values.put(Source.Name, name.getText().toString().trim().toUpperCase());

    if (uri == null) {
      uri = getContentResolver().insert(DatabaseProvider.SOURCE_URI, values);
    } else {
      getContentResolver().update(uri, values, null, null);
    }

    return true;
  }
}
