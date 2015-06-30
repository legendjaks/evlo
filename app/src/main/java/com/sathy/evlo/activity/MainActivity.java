package com.sathy.evlo.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.sathy.evlo.fragment.ExpensesFragment;
import com.sathy.evlo.fragment.HomeFragment;
import com.sathy.evlo.fragment.IncomesFragment;
import com.sathy.evlo.fragment.SourcesFragment;
import com.sathy.evlo.fragment.TagsFragment;

public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getSimpleName();

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigation;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigation = (NavigationView) findViewById(R.id.navigation_view);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerToggle = new DrawerToggle();
        // Set the drawer_items toggle as the DrawerListener
        drawerLayout.setDrawerListener(drawerToggle);

        navigation.setNavigationItemSelectedListener(new SelectedListener());

        showFragment(R.id.nav_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_search) {
            Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    private void showFragment(int id) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (id) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            case R.id.nav_expenses:
                fragment = new ExpensesFragment();
                title = getString(R.string.title_expenses);
                break;
            case R.id.nav_incomes:
                fragment = new IncomesFragment();
                title = getString(R.string.title_incomes);
                break;
            case R.id.nav_tags:
                fragment = new TagsFragment();
                title = getString(R.string.title_tags);
                break;
            case R.id.nav_sources:
                fragment = new SourcesFragment();
                title = getString(R.string.title_sources);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.body, fragment);
            fragmentTransaction.commit();
        }
        getSupportActionBar().setTitle(title);
    }

    private class SelectedListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {

            drawerLayout.closeDrawer(navigation);
            showFragment(menuItem.getItemId());
            menuItem.setChecked(true);
            return true;
        }
    }

    private class DrawerToggle extends ActionBarDrawerToggle {

        public DrawerToggle() {
            super(MainActivity.this, drawerLayout,
                    toolbar, R.string.drawer_open, R.string.drawer_close);
        }

        /**
         * Called when a drawer_items has settled in a completely closed state.
         */
        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
            invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
        }

        /**
         * Called when a drawer_items has settled in a completely open state.
         */
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            super.onDrawerSlide(drawerView, slideOffset);
            toolbar.setAlpha(1 - slideOffset / 2);
        }
    }
}
