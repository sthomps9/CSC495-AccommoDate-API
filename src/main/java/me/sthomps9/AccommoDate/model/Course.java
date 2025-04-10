package me.sthomps9.AccommoDate.model;

import java.sql.Time;

public class Course {
    int crn;
    String courseid;
    String sectionnum;
    String meetdays;
    String instructor;
    String instructoremail;
    String coursename;
    Time meettime;
    int meetduration;

    public int getCRN() { return crn; }
    public String getCourseid() { return courseid; }
    public String getMeetdays() { return meetdays; }
    public String getSectionnum() { return sectionnum; }
    public String getInstructor() { return instructor; }
    public String getInstructoremail() { return instructoremail; }
    public String getCoursename() { return coursename; }
    public Time getMeettime() { return meettime; }
    public int getMeetduration() { return meetduration; }

    public void setCRN(int crn) { this.crn = crn; }
    public void setCourseid(String courseid) { this.courseid = courseid; }
    public void setMeetdays(String meetdays) { this.meetdays = meetdays; }
    public void setInstructoremail(String instructoremail) { this.instructoremail = instructoremail; }
    public void setCoursename(String coursename) { this.coursename = coursename; }
    public void setInstructor(String instructor) { this.instructor = instructor; }
    public void setSectionnum(String sectionnum) { this.sectionnum = sectionnum; }
    public void setMeettime(Time meettime) { this.meettime = meettime; }
    public void setMeetduration(int meetduration) { this.meetduration = meetduration; }

}
