package com.example.android.flowercalendar;

import android.content.Context;

import com.example.android.flowercalendar.database.ShiftsDao;
import com.example.android.flowercalendar.database.ShiftsDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Room;

/*@RunWith(AndroidJUnit4.class)
public class SimpleEntityReadWriteTest {
    private ShiftsDao shiftsDao;
    private ShiftsDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, ShiftsDatabase.class).build();
        shiftsDao = db.getShiftsDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void writeUserAndReadInList() throws Exception {
        User user = TestUtil.createUser(3);
        user.setName("george");
        userDao.insert(user);
        List<User> byName = userDao.findUsersByName("george");
        assertThat(byName.get(0), equalTo(user));
    }
}*/
