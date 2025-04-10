package me.sthomps9.AccommoDate.model;

public class FullExam {
    User user;
    Exam exam;
    Course course;
    public FullExam(User user, Exam exam, Course course) {
        this.user = user;
        this.exam = exam;
        this.course = course;
    }

    public void setUser(User user) { this.user = user; }
    public void setCourse(Course course) { this.course = course; }
    public void setExam(Exam exam) { this.exam = exam; }
    public Exam getExam() { return exam; }
    public Course getCourse() { return course; }
    public User getUser() { return user; }
}
