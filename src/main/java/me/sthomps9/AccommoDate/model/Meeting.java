package me.sthomps9.AccommoDate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import me.sthomps9.AccommoDate.dao.ExamDAO;
import me.sthomps9.AccommoDate.dao.MeetingDAO;
import me.sthomps9.AccommoDate.mapping.LocalDateDeserializer;
import me.sthomps9.AccommoDate.mapping.LocalDateSerializer;
import me.sthomps9.AccommoDate.mapping.TimeDeserializer;
import me.sthomps9.AccommoDate.mapping.TimeSerializer;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.UUID;

public class Meeting {

    String meetingid;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    LocalDate meetdate;
    @JsonDeserialize(using = TimeDeserializer.class)
    @JsonSerialize(using = TimeSerializer.class)
    Time meettime;
    String userid;
    String adminid;
    boolean virtual;

    public void writeMeeting(MeetingDAO dao) {
        dao.addExam(
                UUID.randomUUID().toString(),
                userid,
                adminid,
                meettime,
                meetdate,
                virtual);
    }



    public String getMeetingid() {
        return meetingid;
    }

    public void setMeetingid(String meetingid) {
        this.meetingid = meetingid;
    }

    public LocalDate getMeetdate() {
        return meetdate;
    }

    public void setMeetdate(LocalDate meetdate) {
        this.meetdate = meetdate;
    }

    public Time getMeettime() {
        return meettime;
    }

    public void setMeettime(Time meettime) {
        this.meettime = meettime;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAdminid() {
        return adminid;
    }

    public void setAdminid(String adminid) {
        this.adminid = adminid;
    }

    public boolean isVirtual() {
        return virtual;
    }

    public void setVirtual(boolean virtual) {
        this.virtual = virtual;
    }


}