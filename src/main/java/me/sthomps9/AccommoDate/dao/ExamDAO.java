package me.sthomps9.AccommoDate.dao;

import me.sthomps9.AccommoDate.model.Exam;
import me.sthomps9.AccommoDate.model.Meeting;
import me.sthomps9.AccommoDate.model.User;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExamDAO {

    @SqlQuery("SELECT * FROM exams WHERE examdate >= :start and examdate <= :end and studentid = :id ORDER BY examtime")
    @RegisterBeanMapper(Exam.class)
    List<Exam> getBetweenDates(@Bind("start") LocalDate start, @Bind("end") LocalDate end, @Bind("id") String id);

    @SqlQuery("SELECT * FROM exams WHERE studentid = :studentid")
    @RegisterBeanMapper(Exam.class)
    List<Exam> findByStudentID(@Bind("studentid") String studentid);

    @SqlQuery("SELECT * FROM exams WHERE examrequested = true and examconfirmed = false ORDER BY examdate")
    @RegisterBeanMapper(Exam.class)
    List<Exam> getPending();

    @SqlQuery("SELECT * FROM exams WHERE examrequested = false and examconfirmed = false ORDER BY examdate")
    @RegisterBeanMapper(Exam.class)
    List<Exam> getNew();

    @SqlQuery("SELECT * FROM exams WHERE examid = :examid")
    @RegisterBeanMapper(Exam.class)
    Optional<Exam> findByID(@Bind("examid") String examid);

    @SqlQuery("SELECT * FROM exams WHERE examdate = :date ORDER BY examtime")
    @RegisterBeanMapper(Exam.class)
    List<Exam> getByDate(@Bind("date") LocalDate date);

    @SqlQuery("SELECT * FROM exams WHERE examdate = :date and examcomplete = false ORDER BY examtime")
    @RegisterBeanMapper(Exam.class)
    List<Exam> getForBoard(@Bind("date") LocalDate date);

    @SqlUpdate("insert into exams values (:examid, :crn, :examdate, :examtime, :studentid, :examlocation, :examconfirmed, :examcomplete, :examonline, :examduration, :note, :examrequested, :studentnotified)")
    void addExam(
            @Bind("examid") String examid,
            @Bind("crn") int crn,
            @Bind("examdate") LocalDate examdate,
            @Bind("examtime") Time examtime,
            @Bind("studentid") String studentid,
            @Bind("examlocation") String examlocation,
            @Bind("examconfirmed") boolean examconfirmed,
            @Bind("examcomplete") boolean examcomplete,
            @Bind("examonline") boolean examonline,
            @Bind("examduration") int examduration,
            @Bind("note") String note,
            @Bind("examrequested") boolean examrequested,
            @Bind("studentnotified") boolean studentnotified);

    @SqlUpdate("update exams set " +
            "crn = :crn, " +
            "examdate = :examdate, " +
            "examtime = :examtime, " +
            "studentid = :studentid, " +
            "examlocation = :examlocation, " +
            "examconfirmed = :examconfirmed, " +
            "examcomplete = :examcomplete, " +
            "examonline = :examonline, " +
            "examduration = :examduration, " +
            "note = :note, " +
            "examrequested = :examrequested, " +
            "studentnotified = :studentnotified " +
            "where :examid = examid")
    void updateExam(
            @Bind("examid") String examid,
            @Bind("crn") int crn,
            @Bind("examdate") LocalDate examdate,
            @Bind("examtime") Time examtime,
            @Bind("studentid") String studentid,
            @Bind("examlocation") String examlocation,
            @Bind("examconfirmed") boolean examconfirmed,
            @Bind("examcomplete") boolean examcomplete,
            @Bind("examonline") boolean examonline,
            @Bind("examduration") int examduration,
            @Bind("note") String note,
            @Bind("examrequested") boolean examrequested,
            @Bind("studentnotified") boolean studentnotified);

    @SqlQuery("SELECT * FROM exams WHERE studentid = :studentid and examdate >= :date ORDER BY examdate")
    @RegisterBeanMapper(Exam.class)
    List<Exam> findUpcomingByStudentID(@Bind("studentid") String studentid, @Bind("date") LocalDate date);

    @SqlQuery("SELECT * FROM exams WHERE studentid = :studentid and examdate <= :date ORDER BY examdate DESC")
    @RegisterBeanMapper(Exam.class)
    List<Exam> findPastByStudentID(@Bind("studentid") String studentid, @Bind("date") LocalDate date);

    @SqlQuery("SELECT * FROM exams order by examdate asc")
    @RegisterBeanMapper(Exam.class)
    List<Exam> getAll();

    @SqlQuery("SELECT * FROM exams WHERE examdate = :notifydate and studentnotified = false ORDER BY examtime")
    @RegisterBeanMapper(Exam.class)
    List<Exam> getToNotify(@Bind("notifydate") LocalDate notifydate);

    @SqlUpdate("DELETE FROM exams WHERE examid = :examid")
    void deleteExam(@Bind("examid") String id);

    @SqlUpdate("DELETE FROM exams WHERE studentid = :userid")
    void purgeExams(@Bind("userid") String id);
}
