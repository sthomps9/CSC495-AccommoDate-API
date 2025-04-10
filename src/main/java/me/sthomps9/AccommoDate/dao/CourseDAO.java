package me.sthomps9.AccommoDate.dao;

import me.sthomps9.AccommoDate.model.Course;
import me.sthomps9.AccommoDate.model.Exam;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

public interface CourseDAO {

    @SqlQuery("SELECT * FROM courses WHERE crn = :crn ORDER BY courseid")
    @RegisterBeanMapper(Course.class)
    Course findByCRN(@Bind("crn") int crn);

    @SqlQuery("SELECT * FROM courses ORDER BY courseid")
    @RegisterBeanMapper(Course.class)
    List<Course> getAll();

}
