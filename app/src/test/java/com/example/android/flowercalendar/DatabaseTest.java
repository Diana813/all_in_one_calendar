package com.example.android.flowercalendar;

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
