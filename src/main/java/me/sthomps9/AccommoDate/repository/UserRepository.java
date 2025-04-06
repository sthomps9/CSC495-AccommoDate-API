package me.sthomps9.AccommoDate.repository;

import me.sthomps9.AccommoDate.dao.UserDAO;
import me.sthomps9.AccommoDate.model.User;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepository {

    private final UserDAO dao;

    public UserRepository(Jdbi jdbi) {
        this.dao = jdbi.onDemand(UserDAO.class);
    }

    public User findByID(String studentid) {
        Optional<User> user = dao.findByID(studentid);
        return user.orElse(null);
    }

    public User findByEmail(String email) {
        Optional<User> user = dao.findByEmail(email);
        return user.orElse(null);
    }

    public UserDAO getDao() {
        return dao;
    }

}
