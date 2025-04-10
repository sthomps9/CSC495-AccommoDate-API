package me.sthomps9.AccommoDate.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import me.sthomps9.AccommoDate.dao.ExamDAO;
import me.sthomps9.AccommoDate.dao.MeetingDAO;
import me.sthomps9.AccommoDate.dao.UserDAO;
import me.sthomps9.AccommoDate.model.*;
import me.sthomps9.AccommoDate.repository.CourseRepository;
import me.sthomps9.AccommoDate.repository.ExamRepository;
import me.sthomps9.AccommoDate.repository.MeetingRepository;
import me.sthomps9.AccommoDate.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/meeting")
public class MeetingViewController {

    private final MeetingDAO meetingDAO;
    private final UserDAO userDAO;

    public MeetingViewController(MeetingRepository meetDAO,
                              UserRepository userDao) {
        this.meetingDAO = meetDAO.getDao();
        this.userDAO = userDao.getDao();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getforadmin/{id}")
    public String getAdminMeetings(@PathVariable("id") String id) throws JsonProcessingException {
        List<Meeting> meets = meetingDAO.findByAdminID(id);
        List<FullMeeting> fullmeets = meets.stream().map(meeting -> {
            User user = userDAO.findByID(meeting.getUserid()).get();
            User admin = userDAO.findByID(meeting.getAdminid()).get();
            return new FullMeeting(user, admin, meeting);
        }).toList();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(fullmeets);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getforuser/{id}")
    public String getUserMeetings(@PathVariable("id") String id) throws JsonProcessingException {
        List<Meeting> meets = meetingDAO.findByStudentID(id);
        List<FullMeeting> fullmeets = meets.stream().map(meeting -> {
            User user = userDAO.findByID(meeting.getUserid()).get();
            User admin = userDAO.findByID(meeting.getAdminid()).get();
            return new FullMeeting(user, admin, meeting);
        }).toList();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(fullmeets);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getforadmin/{id}/{date}")
    public String getAdminMeetingsByDate(@PathVariable("id") String id,
                                         @PathVariable("date") String date) throws JsonProcessingException {
        List<Meeting> meets = meetingDAO.findByAdminID(id, Date.valueOf(date));
        List<FullMeeting> fullmeets = meets.stream().map(meeting -> {
            User user = userDAO.findByID(meeting.getUserid()).get();
            User admin = userDAO.findByID(meeting.getAdminid()).get();
            return new FullMeeting(user, admin, meeting);
        }).toList();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(fullmeets);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getforuser/{id}/{date}")
    public String getUserMeetingsByDate(@PathVariable("id") String id,
                                        @PathVariable("date") String date) throws JsonProcessingException {
        List<Meeting> meets = meetingDAO.findByStudentID(id, Date.valueOf(date));
        List<FullMeeting> fullmeets = meets.stream().map(meeting -> {
            User user = userDAO.findByID(meeting.getUserid()).get();
            User admin = userDAO.findByID(meeting.getAdminid()).get();
            return new FullMeeting(user, admin, meeting);
        }).toList();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(fullmeets);
    }

}
