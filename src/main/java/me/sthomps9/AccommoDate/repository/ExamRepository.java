package me.sthomps9.AccommoDate.repository;

import me.sthomps9.AccommoDate.dao.ExamDAO;
import me.sthomps9.AccommoDate.dao.UserDAO;
import me.sthomps9.AccommoDate.model.Exam;
import me.sthomps9.AccommoDate.model.User;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ExamRepository {

    private final ExamDAO dao;

    public ExamRepository(Jdbi jdbi) {
        this.dao = jdbi.onDemand(ExamDAO.class);
    }

    public List<Exam> findByStudentID(String studentid) {
        return dao.findByStudentID(studentid);
    }

    public ExamDAO getDao() { return dao; }


}
