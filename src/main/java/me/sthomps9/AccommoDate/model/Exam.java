package me.sthomps9.AccommoDate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import me.sthomps9.AccommoDate.dao.ExamDAO;

import java.sql.Date;
import java.sql.Time;
import java.time.format.TextStyle;
import java.util.UUID;

public class Exam {

    String examid;
    int crn;

    @JsonFormat(pattern = "yyyy-MM-dd")
    Date examdate;
    Time examtime;
    String studentid;
    String examlocation;
    boolean examconfirmed;
    boolean examcomplete;
    boolean examonline;
    int examduration;

    public void writeExam(ExamDAO dao) {
        dao.addExam(
                UUID.randomUUID().toString(),
                crn,
                examdate,
                examtime,
                studentid,
                examlocation,
                examconfirmed,
                examcomplete,
                examonline,
                examduration);
    }



    public String getExamid() {
        return examid;
    }

    public void setExamid(String examid) {
        this.examid = examid;
    }

    public int getCrn() {
        return crn;
    }

    public void setCrn(int crn) {
        this.crn = crn;
    }

    public Date getExamdate() {
        return examdate;
    }

    public void setExamdate(Date examdate) {
        this.examdate = examdate;
    }

    public Time getExamtime() {
        return examtime;
    }

    public void setExamtime(Time examtime) {
        this.examtime = examtime;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getExamlocation() {
        return examlocation;
    }

    public void setExamlocation(String examlocation) {
        this.examlocation = examlocation;
    }

    public boolean isExamconfirmed() {
        return examconfirmed;
    }

    public void setExamconfirmed(boolean examconfirmed) {
        this.examconfirmed = examconfirmed;
    }

    public boolean isExamcomplete() {
        return examcomplete;
    }

    public void setExamcomplete(boolean examcomplete) {
        this.examcomplete = examcomplete;
    }

    public boolean isExamonline() {
        return examonline;
    }

    public void setExamonline(boolean examonline) {
        this.examonline = examonline;
    }

    public int getExamduration() {
        return examduration;
    }

    public void setExamduration(int examduration) {
        this.examduration = examduration;
    }

}