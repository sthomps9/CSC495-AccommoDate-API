package me.sthomps9.AccommoDate.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import me.sthomps9.AccommoDate.dao.MeetingDAO;
import me.sthomps9.AccommoDate.dao.UserDAO;
import me.sthomps9.AccommoDate.model.*;
import me.sthomps9.AccommoDate.repository.MeetingRepository;
import me.sthomps9.AccommoDate.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
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
    @GetMapping("/get/admin/upcoming/{id}/{date}/{time}")
    public String getUpcomingAdminMeetings(@PathVariable("id") String id,
                                           @PathVariable("date") String date,
                                           @PathVariable("time") String time) throws JsonProcessingException {

        String[] parts1 = date.split("-");
        int year1 = Integer.parseInt(parts1[0]);
        int month1 = Integer.parseInt(parts1[1]);
        int day1 = Integer.parseInt(parts1[2]);

        Time currentT = Time.valueOf(time);
        LocalDate currentD = LocalDate.of(year1, month1, day1);

        List<Meeting> meets = meetingDAO.findUpcomingByAdminID(id, currentD);

        List<Meeting> upcoming = new ArrayList<>();
        for (Meeting m : meets) {
            if (m.getMeetdate().isAfter(currentD)) upcoming.add(m);
            else if (m.getMeetdate().isEqual(currentD) && m.getMeettime().after(currentT)) upcoming.add(m);
        }

        List<FullMeeting> fullmeets = upcoming.stream().map(meeting -> {
            User user = userDAO.findByID(meeting.getUserid()).get();
            User admin = userDAO.findByID(meeting.getAdminid()).get();
            return new FullMeeting(user, admin, meeting);
        }).toList();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(fullmeets);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get/user/upcoming/{id}/{date}/{time}")
    public String getUpcomingUserMeetings(@PathVariable("id") String id,
                                          @PathVariable("date") String date,
                                          @PathVariable("time") String time) throws JsonProcessingException {

        String[] parts1 = date.split("-");
        int year1 = Integer.parseInt(parts1[0]);
        int month1 = Integer.parseInt(parts1[1]);
        int day1 = Integer.parseInt(parts1[2]);

        Time currentT = Time.valueOf(time);
        LocalDate currentD = LocalDate.of(year1, month1, day1);

        List<Meeting> meets = meetingDAO.findUpcomingByStudentID(id, currentD);

        List<Meeting> upcoming = new ArrayList<>();
        for (Meeting m : meets) {
            if (m.getMeetdate().isAfter(currentD)) upcoming.add(m);
            else if (m.getMeetdate().isEqual(currentD) && m.getMeettime().after(currentT)) upcoming.add(m);
        }

        List<FullMeeting> fullmeets = upcoming.stream().map(meeting -> {
            User user = userDAO.findByID(meeting.getUserid()).get();
            User admin = userDAO.findByID(meeting.getAdminid()).get();
            return new FullMeeting(user, admin, meeting);
        }).toList();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(fullmeets);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get/admin/past/{id}/{date}/{time}")
    public String getPastAdminMeetings(@PathVariable("id") String id,
                                           @PathVariable("date") String date,
                                           @PathVariable("time") String time) throws JsonProcessingException {

        String[] parts1 = date.split("-");
        int year1 = Integer.parseInt(parts1[0]);
        int month1 = Integer.parseInt(parts1[1]);
        int day1 = Integer.parseInt(parts1[2]);

        Time currentT = Time.valueOf(time);
        LocalDate currentD = LocalDate.of(year1, month1, day1);

        List<Meeting> meets = meetingDAO.findPriorByAdminID(id, currentD, currentT);

        List<FullMeeting> fullmeets = meets.stream().map(meeting -> {
            User user = userDAO.findByID(meeting.getUserid()).get();
            User admin = userDAO.findByID(meeting.getAdminid()).get();
            return new FullMeeting(user, admin, meeting);
        }).toList();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(fullmeets);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get/user/past/{id}/{date}/{time}")
    public String getPastUserMeetings(@PathVariable("id") String id,
                                          @PathVariable("date") String date,
                                          @PathVariable("time") String time) throws JsonProcessingException {

        String[] parts1 = date.split("-");
        int year1 = Integer.parseInt(parts1[0]);
        int month1 = Integer.parseInt(parts1[1]);
        int day1 = Integer.parseInt(parts1[2]);

        Time currentT = Time.valueOf(time);
        LocalDate currentD = LocalDate.of(year1, month1, day1);

        List<Meeting> meets = meetingDAO.findPriorByStudentID(id, currentD, currentT);

        List<FullMeeting> fullmeets = meets.stream().map(meeting -> {
            User user = userDAO.findByID(meeting.getUserid()).get();
            User admin = userDAO.findByID(meeting.getAdminid()).get();
            return new FullMeeting(user, admin, meeting);
        }).toList();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(fullmeets);
    }

}
