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



}
