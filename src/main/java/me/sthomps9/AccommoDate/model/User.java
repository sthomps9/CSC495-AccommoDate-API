package me.sthomps9.AccommoDate.model;

import me.sthomps9.AccommoDate.dao.UserDAO;

public class User {
    String studentid;
    String fullname;
    String preferredname;
    String title;
    public String email;
    public String pwd;
    public String role;
    double timeextension;
    boolean scribe;
    boolean reader;
    boolean wordprocessor;

    public double getTimeextension() { return timeextension; }
    public void setTimeextension(double timeextension) {
        this.timeextension = timeextension;
    }

    public void setScribe(boolean scribe) { this.scribe = scribe; }
    public void setReader(boolean reader) { this.reader = reader; }
    public void setWordprocessor(boolean wordprocessor) { this.wordprocessor = wordprocessor; }

    public boolean isScribe() { return scribe; }
    public boolean isReader() { return reader; }
    public boolean isWordprocessor() { return wordprocessor; }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPreferredname() {
        return preferredname;
    }

    public void setPreferredname(String preferredname) {
        this.preferredname = preferredname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String permissionlevel) {
        this.role = permissionlevel;
    }

    public void updateUser(UserDAO userDAO) {
        userDAO.update(
                studentid,
                fullname,
                email,
                timeextension,
                wordprocessor,
                scribe,
                reader
        );
    }

    public void addUser(UserDAO userDAO) {
        userDAO.add(
                studentid,
                fullname,
                preferredname,
                email,
                role,
                title,
                pwd,
                timeextension,
                wordprocessor,
                scribe,
                reader
        );
    }
}


