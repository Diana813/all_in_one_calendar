package com.example.android.flowercalendar.mainActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.calendar.CalendarFragment;
import com.example.android.flowercalendar.coworkers.CoworkerFragment;
import com.example.android.flowercalendar.events.CyclicalEvents.CyclicalEvents;
import com.example.android.flowercalendar.events.FrequentActivities.FrequentActivities;
import com.example.android.flowercalendar.forGirls.ForGirlsFragment;
import com.example.android.flowercalendar.personalGrowth.BackgroundActivity;
import com.example.android.flowercalendar.personalGrowth.LifeAims;
import com.example.android.flowercalendar.shifts.ShiftsFragment;
import com.example.android.flowercalendar.widget.WidgetUtils;

import java.util.ArrayList;
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
    private static double screenHeight;

    @Override
    public void onPause() {
        super.onPause();
        WidgetUtils.updateWidget(this);
        WidgetUtils.updateWidgetAtMidnight(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.flContent, new CalendarFragment());
        tx.commit();


        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        expandableListView = findViewById(R.id.expandableListView);
        mDrawer = findViewById(R.id.activity_expanded_day_view);
        addListData();
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        showMyAppOnTheShareListOfApps();
        setUsableScreenDimensions();


    }

    private void showMyAppOnTheShareListOfApps() {

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if ("android.intent.action.SEND".equals(action) && "text/".equals(type)) {
            Log.println(Log.ASSERT, "shareablTextExtra", Objects.requireNonNull(intent.getStringExtra("android.intent.extra.TEXT")));
        } else if (("android.intent.action.SEND".equals(action) && "image/jpeg".equals(type))) {
            Uri receivedUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);

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
        DrawerLayout drawer = findViewById(R.id.activity_expanded_day_view);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public static LinkedHashMap<String, List<String>> getData() {

        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<>();

        List<String> calendar = new ArrayList<>();

        List<String> forGirls = new ArrayList<>();

        List<String> shifts = new ArrayList<>();

        List<String> coworkers = new ArrayList<>();

        List<String> personalGrowth = new ArrayList<>();

        List<String> events = new ArrayList<>();
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

    public static List<Integer> getIconData() {

        List<Integer> iconList = new ArrayList<>();

        iconList.add(R.drawable.baseline_date_range_black_48);
        iconList.add(R.drawable.baseline_today_black_48);
        iconList.add(R.drawable.baseline_filter_vintage_black_48);
        iconList.add(R.drawable.baseline_business_center_black_48);
        iconList.add(R.drawable.baseline_perm_contact_calendar_black_48);
        iconList.add(R.drawable.baseline_sentiment_satisfied_alt_black_48);

        return iconList;
    }


    private void addListData() {

        LinkedHashMap<String, List<String>> expandableListDetail = getData();
        List<String> expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail, getIconData());
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(this::onGroupClick);


        expandableListView.setOnChildClickListener(this::onChildClick);
    }

    private void setUsableScreenDimensions() {

        DisplayMetrics dm = getResources().getDisplayMetrics();
        float screen_w = dm.widthPixels;
        float screen_h = dm.heightPixels;

        int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            screen_h -= getResources().getDimensionPixelSize(resId);
        }
        TypedValue typedValue = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
            screen_h -= getResources().getDimensionPixelSize(typedValue.resourceId);
        }
        screenHeight = screen_h;
    }

    public static double getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(double screenHeight) {
        MainActivity.screenHeight = screenHeight;
    }


    private boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
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

    private boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
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
}