package me.sthomps9.AccommoDate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import me.sthomps9.AccommoDate.dao.ExamDAO;
import me.sthomps9.AccommoDate.dao.MeetingDAO;

import java.sql.Date;
import java.sql.Time;
import java.util.UUID;

public class Meeting {

    String meetingid;

    @JsonFormat(pattern = "yyyy-MM-dd")
    Date meetdate;
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

    public Date getMeetdate() {
        return meetdate;
    }

    public void setMeetdate(Date meetdate) {
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