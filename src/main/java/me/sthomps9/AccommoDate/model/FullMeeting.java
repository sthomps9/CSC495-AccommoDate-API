package me.sthomps9.AccommoDate.model;

public class FullMeeting {
    User user;
    User admin;
    Meeting meeting;
    public FullMeeting(User user, User admin, Meeting meeting) {
        this.user = user;
        this.admin = admin;
        this.meeting = meeting;
    }

    public void setUser(User user) { this.user = user; }
    public void setAdmin(User admin) { this.admin = admin; }
    public void setMeeting(Meeting meeting) { this.meeting = meeting; }
    public Meeting getMeeting() { return meeting; }
    public User getAdmin() { return admin; }
    public User getUser() { return user; }
}
