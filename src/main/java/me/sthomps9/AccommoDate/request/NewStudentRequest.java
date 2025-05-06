package me.sthomps9.AccommoDate.request;

import me.sthomps9.AccommoDate.model.User;

public class NewStudentRequest {

    User student;
    int[] courses;

    public User getStudent() { return student; }
    public int[] getCourses() { return courses; }
    public void setStudent(User student) { this.student = student; }
    public void setCourses(int[] courses) { this.courses = courses; }
}
