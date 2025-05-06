package me.sthomps9.AccommoDate.dao;

import me.sthomps9.AccommoDate.model.User;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface UserDAO {

    @SqlQuery("SELECT * FROM users WHERE email = :email")
    @RegisterBeanMapper(User.class)
    Optional<User> findByEmail(@Bind("email") String email);

    @SqlQuery("SELECT * FROM users WHERE studentid = :studentid")
    @RegisterBeanMapper(User.class)
    Optional<User> findByID(@Bind("studentid") String studentid);

    @SqlQuery("SELECT crn FROM studentcourses WHERE studentid = :studentid")
//    @RegisterBeanMapper(User.class)
    List<Integer> findCRNByID(@Bind("studentid") String studentid);

    @SqlUpdate("insert into studentcourses VALUES (:studentid, :crn)")
//    @RegisterBeanMapper(User.class)
    void addCourseRelation(@Bind("studentid") String studentid, @Bind("crn") int crn);

    @SqlQuery("SELECT * FROM users WHERE studentid LIKE 'ADMIN%'")
    @RegisterBeanMapper(User.class)
    List<User> getAdmins();

    @SqlQuery("SELECT * FROM users WHERE role = 'ROLE_USER' ORDER BY studentid")
    @RegisterBeanMapper(User.class)
    List<User> getStudents();

    @SqlUpdate("UPDATE users SET pwd = :newpwd WHERE studentid = :userid")
    void updatePWD(@Bind("newpwd") String newpwd, @Bind("userid") String userid);

    @SqlUpdate("UPDATE users SET preferredname = :prefname WHERE studentid = :userid")
    void updatePrefName(@Bind("prefname") String prefname, @Bind("userid") String userid);

    @SqlUpdate("DELETE FROM users WHERE studentid = :studentid")
    void deleteUser(@Bind("studentid") String id);

    @SqlUpdate("UPDATE users SET " +
            "fullname = :fullname, " +
            "email = :email, " +
            "timeextension = :timeextension, " +
            "wordprocessor = :wordprocessor, " +
            "scribe = :scribe, " +
            "reader = :reader" +
            " WHERE studentid = :studentid")
    void update(
            @Bind("studentid") String studentid,
            @Bind("fullname") String fullname,
            @Bind("email") String email,
            @Bind("timeextension") double timeextension,
            @Bind("wordprocessor") boolean wordprocessor,
            @Bind("scribe") boolean scribe,
            @Bind("reader") boolean reader);

    @SqlUpdate("insert into users values (" +
            ":studentid, " +
            ":fullname, " +
            ":email, " +
            ":preferredname, " +
            ":pwd, " +
            ":role, " +
            ":title, " +
            ":timeextension, " +
            ":scribe, " +
            ":reader, " +
            ":wordprocessor)")
    void add(
            @Bind("studentid") String studentid,
            @Bind("fullname") String fullname,
            @Bind("preferredname") String preferredname,
            @Bind("email") String email,
            @Bind("role") String role,
            @Bind("title") String title,
            @Bind("pwd") String pwd,
            @Bind("timeextension") double timeextension,
            @Bind("wordprocessor") boolean wordprocessor,
            @Bind("scribe") boolean scribe,
            @Bind("reader") boolean reader);

    @SqlUpdate("DELETE FROM studentcourses WHERE studentid = :studentid")
    void deleteCourseRelations(@Bind("studentid") String id);
}
