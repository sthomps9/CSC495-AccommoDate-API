package me.sthomps9.AccommoDate.dao;

import me.sthomps9.AccommoDate.model.Meeting;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

public interface MeetingDAO {

    @SqlQuery("SELECT * FROM meetings WHERE userid = :studentid")
    @RegisterBeanMapper(Meeting.class)
    List<Meeting> findByStudentID(@Bind("studentid") String studentid);

    @SqlQuery("SELECT * FROM meetings WHERE adminid = :adminid")
    @RegisterBeanMapper(Meeting.class)
    List<Meeting> findByAdminID(@Bind("adminid") String adminid);

    @SqlQuery("SELECT * FROM meetings WHERE userid = :studentid and meetdate >= :date")
    @RegisterBeanMapper(Meeting.class)
    List<Meeting> findUpcomingByStudentID(@Bind("studentid") String studentid, @Bind("date") LocalDate date);

    @SqlQuery("SELECT * FROM meetings WHERE adminid = :adminid and meetdate >= :date")
    @RegisterBeanMapper(Meeting.class)
    List<Meeting> findUpcomingByAdminID(@Bind("adminid") String adminid, @Bind("date") LocalDate date);

    @SqlQuery("SELECT * FROM meetings WHERE userid = :studentid and meetdate <= :date")
    @RegisterBeanMapper(Meeting.class)
    List<Meeting> findPriorByStudentID(@Bind("studentid") String studentid, @Bind("date") LocalDate date);

    @SqlUpdate("DELETE FROM meetings WHERE userid = :userid")
    void purgeMeetingsUser(@Bind("userid") String id);

    @SqlQuery("SELECT * FROM meetings WHERE adminid = :adminid and meetdate <= :date")
    @RegisterBeanMapper(Meeting.class)
    List<Meeting> findPriorByAdminID(@Bind("adminid") String adminid, @Bind("date") LocalDate date);

    @SqlUpdate("insert into meetings values (:meetingid, :userid, :adminid, :meettime, :meetdate, :virtual)")
    void addExam(
            @Bind("meetingid") String meetingid,
            @Bind("userid") String userid,
            @Bind("adminid") String adminid,
            @Bind("meettime") Time meettime,
            @Bind("meetdate") LocalDate meetdate,
            @Bind("virtual") boolean virtual);


}
