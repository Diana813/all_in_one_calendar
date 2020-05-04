package com.example.android.flowercalendar.Events.ExpandedDayView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.flowercalendar.AppUtils;
import com.example.android.flowercalendar.DoneOnEditorActionListener;
import com.example.android.flowercalendar.R;
import com.example.android.flowercalendar.database.CalendarDatabase;
import com.example.android.flowercalendar.database.Event;
import com.example.android.flowercalendar.database.EventsDao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import static com.example.android.flowercalendar.Calendar.CalendarFragment.pickedDay;

public class ExpandableListHoursAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private LinkedHashMap<String, List<String>> expandableListDetail;
    private AppUtils appUtils = new AppUtils();
    private DailyScheduleEvents dailyScheduleEvents = new DailyScheduleEvents();
    public static String scheduleGroup;
    public static String scheduleItem;
    private int index = -1;
    private int indexListPosition = -1;
    private int indexExpandedListPosition = -1;


    ExpandableListHoursAdapter(Context context, List<String> expandableListTitle,
                               LinkedHashMap<String, List<String>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return Objects.requireNonNull(this.expandableListDetail.get(this.expandableListTitle.get(listPosition)))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @SuppressLint({"InflateParams", "ClickableViewAccessibility"})
    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = Objects.requireNonNull(layoutInflater).inflate(R.layout.single_hours_item, null);
        }

        EditText editText = convertView.findViewById(R.id.editText);

        editText.setOnEditorActionListener(new DoneOnEditorActionListener());
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        editText.setRawInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        editText.setOnTouchListener((v, event) -> {
            indexListPosition = listPosition;
            indexExpandedListPosition = expandedListPosition;
            return false;
        });

        editText.setOnLongClickListener(v -> {
            EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();
            eventsDao.deleteBySchedule(pickedDay, expandedListText);
            editText.setText("");
            return false;
        });



        TextView expandedListTextView = convertView
                .findViewById(R.id.hour);

        expandedListTextView.setText(expandedListText);

        if (indexListPosition != -1 && indexExpandedListPosition != -1) {
            scheduleItem = (String) getChild(indexListPosition, indexExpandedListPosition);
        }

        EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();
        Event event = eventsDao.findBySchedule(pickedDay, expandedListText);
        if (event != null) {
            if (event.getSchedule().equals(expandedListText)) {
                editText.setText(event.getEvent_name());
            }
        } else {
            editText.setText("");
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return Objects.requireNonNull(this.expandableListDetail.get(this.expandableListTitle.get(listPosition)))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @SuppressLint({"InflateParams", "ClickableViewAccessibility"})
    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = Objects.requireNonNull(layoutInflater).inflate(R.layout.single_hours_group, null);
        }


        ImageView imageView = convertView.findViewById(R.id.downArrow);
        TextView listTitleTextView = convertView
                .findViewById(R.id.hour);
        EditText editText = convertView.findViewById(R.id.editText);

        editText.setOnEditorActionListener(new DoneOnEditorActionListener());
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        editText.setRawInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        editText.setFocusable(false);
        editText.setOnTouchListener((v, event) -> {
            editText.setFocusableInTouchMode(true);
            index = listPosition;
            return false;
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                editText.setFocusable(false);
                editText.setFocusableInTouchMode(false);
            }
        });

        editText.setOnLongClickListener(v -> {
            EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();
            eventsDao.deleteBySchedule(pickedDay, listTitle);
            editText.setText("");
            return false;
        });

        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        if (getChildrenCount(listPosition) > 0) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
        }

        if (index != -1) {
            scheduleGroup = (String) getGroup(index);
        }
        EventsDao eventsDao = CalendarDatabase.getDatabase(context).eventsDao();
        Event event = eventsDao.findBySchedule(pickedDay, listTitle);
        if (event != null) {
            if (event.getSchedule().equals(listTitle)) {
                editText.setText(event.getEvent_name());
            }
        } else {
            editText.setText("");
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

}

