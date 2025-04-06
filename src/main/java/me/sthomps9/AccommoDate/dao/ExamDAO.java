package me.sthomps9.AccommoDate.dao;

import me.sthomps9.AccommoDate.model.Exam;
import me.sthomps9.AccommoDate.model.User;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

public interface ExamDAO {

    @SqlQuery("SELECT * FROM exams WHERE studentid = :studentid")
    @RegisterBeanMapper(Exam.class)
    List<Exam> findByStudentID(@Bind("studentid") String studentid);

    @SqlQuery("SELECT * FROM exams WHERE examdate = :date")
    @RegisterBeanMapper(Exam.class)
    List<Exam> getByDate(@Bind("date") Date date);

    @SqlUpdate("insert into exams values (:examid, :crn, :examdate, :examtime, :studentid, :examlocation, :examconfirmed, :examcomplete, :examonline, :examduration)")
    void addExam(
            @Bind("examid") String examid,
            @Bind("crn") int crn,
            @Bind("examdate") Date examdate,
            @Bind("examtime") Time examtime,
            @Bind("studentid") String studentid,
            @Bind("examlocation") String examlocation,
            @Bind("examconfirmed") boolean examconfirmed,
            @Bind("examcomplete") boolean examcomplete,
            @Bind("examonline") boolean examonline,
            @Bind("examduration") int examduration);


    @SqlQuery("SELECT * FROM exams order by examdate asc")
    @RegisterBeanMapper(Exam.class)
    List<Exam> getAll();
}
