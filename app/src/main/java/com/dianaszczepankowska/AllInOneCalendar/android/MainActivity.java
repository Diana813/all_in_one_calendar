package com.dianaszczepankowska.AllInOneCalendar.android;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dianaszczepankowska.AllInOneCalendar.android.adapters.BottomLayoutShiftsAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.adapters.CustomExpandableListAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.adapters.ShiftListAdapter;
import com.dianaszczepankowska.AllInOneCalendar.android.utils.BottomLayoutsUtils;
import com.dianaszczepankowska.AllInOneCalendar.android.calendar.CalendarFragment;
import com.dianaszczepankowska.AllInOneCalendar.android.coworkers.CoworkerFragment;
import com.dianaszczepankowska.AllInOneCalendar.android.events.cyclicalEvents.CyclicalEvents;
import com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView.BackgroundActivityExpandedDayView;
import com.dianaszczepankowska.AllInOneCalendar.android.events.frequentActivities.FrequentActivities;
import com.dianaszczepankowska.AllInOneCalendar.android.forGirls.ForGirlsFragment;
import com.dianaszczepankowska.AllInOneCalendar.android.personalGrowth.BackgroundActivity;
import com.dianaszczepankowska.AllInOneCalendar.android.shifts.ShiftsFragment;
import com.dianaszczepankowska.AllInOneCalendar.android.widget.events.UpdateWidget;
import com.dianaszczepankowska.AllInOneCalendar.android.widget.shifts.WidgetUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.dianaszczepankowska.AllInOneCalendar.android.calendar.CalendarFrame.shiftsSheetBehavior;
import static com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView.BackgroundActivityExpandedDayView.currentDate;
import static com.dianaszczepankowska.AllInOneCalendar.android.events.expandedDayView.BackgroundActivityExpandedDayView.shiftsSheetBehaviorExpandedDayView;


public class MainActivity extends AppCompatActivity {

    public CoordinatorLayout mDrawer;
    private ExpandableListView expandableListView;
    private CustomExpandableListAdapter expandableListAdapter;
    private static double screenHeight;
    private BottomSheetBehavior sheetBehavior;
    public static Menu menu;
    private static final int MY_CAL_WRITE_REQ = 1;
    private static final int MY_CAL_REQ = 2;
    public Toolbar toolbar;
    private LinearLayout shifts_bottom_sheet;
    private RecyclerView shifts_recycler_view;
    @SuppressLint("StaticFieldLeak")
    public static LinearLayout shifts_bottom_sheet_expanded_day_view;
    public static RecyclerView shifts_recycler_view_expanded_day_view;
    @SuppressLint("StaticFieldLeak")
    public static TextView date;
    private TextView emptyShiftsTable;
    @SuppressLint("StaticFieldLeak")
    public static Button confirm;
    @SuppressLint("StaticFieldLeak")
    public static Button confirmButton;


    @Override
    public void onPause() {
        super.onPause();
        WidgetUtils.updateWidget(this);
        WidgetUtils.updateWidgetAtMidnight(this);
        UpdateWidget.updateWidget(this);
        UpdateWidget.updateWidgetAtMidnight(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.flContent, new CalendarFragment());
        tx.addToBackStack("tag");
        tx.commit();


        //no background for status bar
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        toolbar = findViewById(R.id.toolbar1);

        //menu three dots color
        Objects.requireNonNull(toolbar.getOverflowIcon()).setColorFilter(getColor(R.color.darkGreyZilla), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);

        expandableListView = findViewById(R.id.expandableListView);
        mDrawer = findViewById(R.id.coordinator);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        menu = bottomNavigationView.getMenu();
        setBottomNavigation(bottomNavigationView);
        addListData();

        LinearLayout bottom_sheet = findViewById(R.id.bottom_sheet_menu);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        showMyAppOnTheShareListOfApps();
        setUsableScreenDimensions();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, MY_CAL_REQ);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_CAL_WRITE_REQ);
        }

        shifts_bottom_sheet = findViewById(R.id.shiftsSettings);
        shifts_recycler_view = findViewById(R.id.shifts_list_recycler);

        shifts_bottom_sheet_expanded_day_view = findViewById(R.id.shiftsSettingsExpandedDayView);
        shifts_recycler_view_expanded_day_view = findViewById(R.id.shifts_list_recycler_expanded_day_view);
        shiftsSheetBehaviorExpandedDayView = BottomSheetBehavior.from(shifts_bottom_sheet_expanded_day_view);
        shiftsSheetBehaviorExpandedDayView.setHideable(true);
        shiftsSheetBehaviorExpandedDayView.setState(BottomSheetBehavior.STATE_HIDDEN);

        date = findViewById(R.id.date);

        emptyShiftsTable = findViewById(R.id.emptyShifts);

        setBottomSheetsBehaviorShifts();
        setBottomLayoutShiftsAdapter();
        FrameLayout buttonLayout = findViewById(R.id.buttonLayout);
        buttonLayout.setPadding(0, 10, 1, 10);
        confirm = findViewById(R.id.confirmButton);
        confirm.setText(R.string.close);
        confirmButton = findViewById(R.id.confirmButtonShifts);

    }


    private void showMyAppOnTheShareListOfApps() {

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if ("android.intent.action.SEND".equals(action) && "image/jpeg".equals(type)) {
            Uri receivedUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);

            if (receivedUri != null) {
                Fragment backgroundActivity = new BackgroundActivity();
                Bundle args = new Bundle();
                args.putParcelable("image", receivedUri);
                backgroundActivity.setArguments(args);
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.flContent, backgroundActivity);
                tx.commit();
            }


        } else if ("android.intent.action.SEND".equals(action) && "text/plain".equals(type)) {

            Fragment backgroundActivity = new BackgroundActivity();
            Bundle args = new Bundle();
            args.putString("message", getString(R.string.imageMessage));
            backgroundActivity.setArguments(args);
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.flContent, backgroundActivity);
            tx.commit();
        }
    }


    @Override
    public void onBackPressed() {
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            super.onBackPressed();
        }
    }


    public LinkedHashMap<String, List<String>> getData() {

        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<>();


        List<String> forGirls = new ArrayList<>();

        List<String> shifts = new ArrayList<>();

        List<String> coworkers = new ArrayList<>();

        List<String> aboutApp = new ArrayList<>();

        List<String> logOut = new ArrayList<>();

        List<String> events = new ArrayList<>();
        events.add(getString(R.string.CyclicalEvents));
        events.add(getString(R.string.FrequentActivities));

        expandableListDetail.put(getString(R.string.OneTimeEvents), events);
        expandableListDetail.put(getString(R.string.ForGirls), forGirls);
        expandableListDetail.put(getString(R.string.Shifts), shifts);
        expandableListDetail.put(getString(R.string.Coworkers), coworkers);
        expandableListDetail.put(getString(R.string.about_app), aboutApp);
        expandableListDetail.put(getString(R.string.logOutButton), logOut);


        return expandableListDetail;
    }

    public static List<Integer> getIconData() {

        List<Integer> iconList = new ArrayList<>();

        iconList.add(R.drawable.baseline_today_black_48);
        iconList.add(R.drawable.baseline_filter_vintage_black_48);
        iconList.add(R.drawable.baseline_business_center_black_48);
        iconList.add(R.drawable.baseline_perm_contact_calendar_black_48);
        iconList.add(R.drawable.baseline_info_black_48);
        iconList.add(R.drawable.baseline_login_black_48);


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


        if (id == 1) {
            fragmentClass = ForGirlsFragment.class;
            menu.findItem(R.id.events).setIcon(R.drawable.baseline_filter_vintage_black_48).setChecked(true);
        }

        if (id == 2) {
            fragmentClass = ShiftsFragment.class;
            menu.findItem(R.id.events).setIcon(R.drawable.baseline_business_center_black_48).setChecked(true);
        }
        if (id == 3) {
            fragmentClass = CoworkerFragment.class;
            menu.findItem(R.id.events).setIcon(R.drawable.baseline_perm_contact_calendar_black_48).setChecked(true);

        }
        if (id == 4) {
            //fragmentClass = BackgroundActivity.class;

            menu.findItem(R.id.events).setIcon(R.drawable.baseline_info_black_48).setChecked(true);
        }

        if (id == 5) {
            launchLoginActivity();
            menu.findItem(R.id.events).setIcon(R.drawable.baseline_login_black_48).setChecked(true);
        }


        try {
            assert fragmentClass != null;
            fragment = (Fragment) fragmentClass.newInstance();


        } catch (Exception e) {
            e.printStackTrace();
        }


        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragment == null) {
            return false;
        }
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack("tag").commit();
        BottomLayoutsUtils.checkBottomLayoutState(sheetBehavior, null, getApplicationContext());
        BottomLayoutsUtils.bottomSheetListener(sheetBehavior, v);
        addListData();
        return true;
    }

    private boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                 int childPosition, long id) {
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
        BottomLayoutsUtils.checkBottomLayoutState(sheetBehavior, null, getApplicationContext());
        BottomLayoutsUtils.bottomSheetListener(sheetBehavior, v);
        return true;
    }

    private void setBottomNavigation(BottomNavigationView bottomNavigationView) {

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            Class fragmentClass;
            switch (item.getItemId()) {
                case R.id.home:
                    fragmentClass = CalendarFragment.class;
                    openClass(fragment, fragmentClass);
                    menu.findItem(R.id.events).setIcon(R.drawable.baseline_today_black_48);
                    break;
                case R.id.events:
                    fragmentClass = BackgroundActivityExpandedDayView.class;
                    openClass(fragment, fragmentClass);
                    break;
                case R.id.aims:
                    fragmentClass = BackgroundActivity.class;
                    openClass(fragment, fragmentClass);
                    break;
                case R.id.menu:
                    BottomLayoutsUtils.checkBottomLayoutState(sheetBehavior, null, getApplicationContext());
                    BottomLayoutsUtils.bottomSheetListener(sheetBehavior, item.getActionView());
                    break;
            }

            return true;
        });
    }

    private void openClass(Fragment fragment, Class fragmentClass) {

        try {
            assert fragmentClass != null;
            fragment = (Fragment) fragmentClass.newInstance();


        } catch (Exception e) {
            e.printStackTrace();

        }


        FragmentManager fragmentManager = getSupportFragmentManager();
        assert fragment != null;
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack("tag").commit();

    }

    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void setBottomSheetsBehaviorShifts() {
        shiftsSheetBehavior = BottomSheetBehavior.from(shifts_bottom_sheet);
        shiftsSheetBehavior.setHideable(true);
        shiftsSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }


    public void setBottomLayoutShiftsAdapter() {
        BottomLayoutShiftsAdapter shiftsAdapter = new BottomLayoutShiftsAdapter(getBaseContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false);
        shifts_recycler_view.setLayoutManager(layoutManager);
        shifts_recycler_view.setAdapter(shiftsAdapter);
        BottomLayoutsUtils.initShiftsData(this, shiftsAdapter, emptyShiftsTable);
    }


}
