package com.sathy.evlo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sathy on 26/6/15.
 */
public class NavDrawer extends Fragment {

    private static String TAG = NavDrawer.class.getSimpleName();

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View containerView;
    private static String[] titles = null;
    private static int[] icons = null;

    public NavDrawer() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // drawer labels
        //titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
        //icons = new int[]{R.drawable.ic_home, R.drawable.ic_expense, R.drawable.ic_income, R.drawable.ic_tag};
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.navigation_drawer, container, false);
        return layout;
    }


}
