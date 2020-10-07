package com.dianaszczepankowska.AllInOneCalendar.android.coworkers;

public class User {

    private String usersPhotoUrl;
    private String userName;
    private String userEmail;

    public User(String usersPhoto, String userName, String userEmail) {
        this.usersPhotoUrl = usersPhoto;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public String getUsersPhotoUrl() {
        return usersPhotoUrl;
    }

    public void setUsersPhotoUrl(String usersPhotoUrl) {
        this.usersPhotoUrl = usersPhotoUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
