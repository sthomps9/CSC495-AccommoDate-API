package me.sthomps9.AccommoDate.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import me.sthomps9.AccommoDate.dao.MeetingDAO;
import me.sthomps9.AccommoDate.dao.UserDAO;
import me.sthomps9.AccommoDate.model.*;
import me.sthomps9.AccommoDate.repository.MeetingRepository;
import me.sthomps9.AccommoDate.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

        List<Meeting> meets = meetingDAO.findPriorByAdminID(id, currentD);

        List<Meeting> past = new ArrayList<>();
        for (Meeting m : meets) {
            if (m.getMeetdate().isBefore(currentD)) past.add(m);
            else if (m.getMeetdate().isEqual(currentD) && m.getMeettime().before(currentT)) past.add(m);
        }

        List<FullMeeting> fullmeets = past.stream().map(meeting -> {
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

        List<Meeting> meets = meetingDAO.findPriorByStudentID(id, currentD);
        List<Meeting> past = new ArrayList<>();
        for (Meeting m : meets) {
            if (m.getMeetdate().isBefore(currentD)) past.add(m);
            else if (m.getMeetdate().isEqual(currentD) && m.getMeettime().before(currentT)) past.add(m);
        }

        List<FullMeeting> fullmeets = past.stream().map(meeting -> {
            User user = userDAO.findByID(meeting.getUserid()).get();
            User admin = userDAO.findByID(meeting.getAdminid()).get();
            return new FullMeeting(user, admin, meeting);
        }).toList();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(fullmeets);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/admins")
    public String getAdmins() throws JsonProcessingException {

        List<User> admin1 = userDAO.getAdmins();
        List<Admin> admin2 = new ArrayList<>();
        for (User u : admin1) {
            Admin admin = new Admin();
            admin.setPreferredname(u.getPreferredname());
            admin.setFullname(u.getFullname());
            admin.setEmail(u.getEmail());
            admin.setAdminid(u.getStudentid());
            admin.setTitle(u.getTitle());
            admin2.add(admin);
        }

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(admin2);
    }

    @PutMapping("/schedule")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Meeting> scheduleExam(@RequestBody Meeting meeting) {
        meeting.setMeetingid(UUID.randomUUID().toString());
        meeting.writeMeeting(meetingDAO);
        return ResponseEntity.ok().build();
    }

}
