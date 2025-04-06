package me.sthomps9.AccommoDate.model;

public class User {
    String studentid;
    String fullname;
    String preferredname;
    String title;
    public String email;
    public String pwd;
    public String role;

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

}


