package me.sthomps9.AccommoDate.model;

public class StudentCourse {
    User user;
    Course course;
    public StudentCourse(User user, Course course) {
        this.user = user;
        this.course = course;
    }

    public void setUser(User user) { this.user = user; }
    public void setCourse(Course course) { this.course = course; }
    public Course getCourse() { return course; }
    public User getUser() { return user; }
}
