package me.sthomps9.AccommoDate.repository;

import me.sthomps9.AccommoDate.dao.CourseDAO;
import me.sthomps9.AccommoDate.dao.UserDAO;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Component;

@Component
public class CourseRepository {
    private final CourseDAO dao;

    public CourseRepository(Jdbi jdbi) {
        this.dao = jdbi.onDemand(CourseDAO.class);
    }

    public CourseDAO getDao() {
        return dao;
    }
}
