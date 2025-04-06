package me.sthomps9.AccommoDate.dao;

import me.sthomps9.AccommoDate.model.Exam;
import me.sthomps9.AccommoDate.model.User;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;
import java.util.Optional;

public interface ExamDAO {

    @SqlQuery("SELECT * FROM exams WHERE studentid = :studentid")
    @RegisterBeanMapper(Exam.class)
    List<Exam> findByStudentID(@Bind("studentid") String studentid);

}
