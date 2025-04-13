package me.sthomps9.AccommoDate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import me.sthomps9.AccommoDate.dao.ExamDAO;

import me.sthomps9.AccommoDate.mapping.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.UUID;

public class Exam {

    String examid;
    int crn;

    //    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    LocalDate examdate;
    Time examtime;
    String studentid;
    String examlocation;
    boolean examconfirmed;
    boolean examcomplete;
    boolean examonline;
    int examduration;
    String note;

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
                examduration,
                note);
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

    public LocalDate getExamdate() {
        return examdate;
    }

    public void setExamdate(LocalDate examdate) {
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void updateExam(ExamDAO dao) {
        System.out.println(examdate);
        dao.updateExam(
                examid,
                crn,
                examdate,
                examtime,
                studentid,
                examlocation,
                examconfirmed,
                examcomplete,
                examonline,
                examduration,
                note);
    }



}