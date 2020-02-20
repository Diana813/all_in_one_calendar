package com.example.android.flowercalendar;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.android.flowercalendar.Calendar.CalendarFragment;
import com.example.android.flowercalendar.Coworkers.CoworkerFragment;
import com.example.android.flowercalendar.Events.CyclicalEvents;
import com.example.android.flowercalendar.Events.FrequentActivities;
import com.example.android.flowercalendar.Events.OneTimeEvents;
import com.example.android.flowercalendar.ForGirls.ForGirlsFragment;
import com.example.android.flowercalendar.PersonalGrowth.BackgroundActivity;
import com.example.android.flowercalendar.PersonalGrowth.LifeAims;
import com.example.android.flowercalendar.Shifts.ShiftsFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity {

    public DrawerLayout mDrawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private ExpandableListView expandableListView;
    private CustomExpandableListAdapter expandableListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.flContent, new CalendarFragment());
        tx.commit();


        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        mDrawer = findViewById(R.id.drawer_layout);
        addListData();
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        showMyAppOnTheShareListOfApps();

    }

    private void showMyAppOnTheShareListOfApps() {

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if ("android.intent.action.SEND".equals(action) && "text/".equals(type)) {
            Log.println(Log.ASSERT, "shareablTextExtra", Objects.requireNonNull(intent.getStringExtra("android.intent.extra.TEXT")));
        } else if (("android.intent.action.SEND".equals(action) && "image/jpeg".equals(type))) {
            Uri receivedUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);

            if (receivedUri != null) {
                Fragment lifeAims = new LifeAims();
                Bundle args = new Bundle();
                args.putParcelable("image", receivedUri);
                lifeAims.setArguments(args);
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.flContent, lifeAims);
                tx.commit();
            }


        }

    }


    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            mDrawer.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public static LinkedHashMap<String, List<String>> getData() {

        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<String, List<String>>();

        List<String> calendar = new ArrayList<String>();

        List<String> forGirls = new ArrayList<String>();

        List<String> shifts = new ArrayList<String>();

        List<String> coworkers = new ArrayList<String>();

        List<String> personalGrowth = new ArrayList<String>();

        List<String> events = new ArrayList<String>();
        events.add("Cyclical Events");
        events.add("Frequent activities");

        expandableListDetail.put("Calendar", calendar);
        expandableListDetail.put("Events", events);
        expandableListDetail.put("For girls", forGirls);
        expandableListDetail.put("Shifts", shifts);
        expandableListDetail.put("Coworkers", coworkers);
        expandableListDetail.put("Personal growth", personalGrowth);


        return expandableListDetail;
    }

    private void addListData() {

        HashMap<String, List<String>> expandableListDetail = getData();
        List<String> expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, (LinkedHashMap<String, List<String>>) expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                Fragment fragment = null;
                Class fragmentClass = null;

                if (expandableListAdapter.getChildrenCount(groupPosition) > 0) {
                    return false;
                }

                if (id == 0) {
                    fragmentClass = CalendarFragment.class;

                }

                if (id == 2) {
                    fragmentClass = ForGirlsFragment.class;
                }

                if (id == 3) {
                    fragmentClass = ShiftsFragment.class;
                }
                if (id == 4) {
                    fragmentClass = CoworkerFragment.class;
                }
                if (id == 5) {
                    fragmentClass = BackgroundActivity.class;

                }

                try {
                    assert fragmentClass != null;
                    fragment = (Fragment) fragmentClass.newInstance();


                } catch (Exception e) {
                    e.printStackTrace();
                }


                FragmentManager fragmentManager = getSupportFragmentManager();
                assert fragment != null;
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();

                mDrawer.closeDrawers();

                return true;
            }
        });


        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                Fragment fragment = null;
                Class fragmentClass;
                if (id == 1) {
                    fragmentClass = FrequentActivities.class;
                } else if (id == 0) {
                    fragmentClass = CyclicalEvents.class;
                } else {
                    fragmentClass = CalendarFragment.class;
                }

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                assert fragment != null;
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack("tag").commit();

                mDrawer.closeDrawers();
                return true;
            }
        });
    }


}