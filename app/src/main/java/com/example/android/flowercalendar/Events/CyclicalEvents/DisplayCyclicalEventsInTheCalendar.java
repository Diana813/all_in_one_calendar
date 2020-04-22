package com.example.android.flowercalendar.Events.CyclicalEvents;

import java.time.LocalDate;

class DisplayCyclicalEventsInTheCalendar {

    void displaycyclicalEvents(LocalDate startDate) {

        CyclicalEventsDetails cyclicalEventsDetails = new CyclicalEventsDetails();

        if (startDate.getMonthValue() == LocalDate.now().getMonthValue()) {
            cyclicalEventsDetails.saveEvent(String.valueOf(startDate));
        }
    }

}
