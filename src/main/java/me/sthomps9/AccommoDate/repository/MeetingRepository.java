package me.sthomps9.AccommoDate.repository;

import me.sthomps9.AccommoDate.dao.ExamDAO;
import me.sthomps9.AccommoDate.dao.MeetingDAO;
import me.sthomps9.AccommoDate.model.Exam;
import me.sthomps9.AccommoDate.model.Meeting;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MeetingRepository {

    private final MeetingDAO dao;

    public MeetingRepository(Jdbi jdbi) {
        this.dao = jdbi.onDemand(MeetingDAO.class);
    }

    public List<Meeting> findByStudentID(String studentid) {
        return dao.findByStudentID(studentid);
    }
    public List<Meeting> findByAdminID(String adminid) {
        return dao.findByAdminID(adminid);
    }

    public MeetingDAO getDao() { return dao; }


}
