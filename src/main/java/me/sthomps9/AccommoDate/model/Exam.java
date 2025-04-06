package me.sthomps9.AccommoDate.model;

import java.sql.Date;
import java.sql.Time;
import java.time.format.TextStyle;
import java.util.UUID;

public class Exam {

    String examid;
    int crn;
    Date examdate;
    Time examtime;
    String studentid;
    String examlocation;
    boolean examconfirmed;
    boolean examcomplete;
    boolean examonline;
    int examduration;


    public int getCRN() { return crn; }

}