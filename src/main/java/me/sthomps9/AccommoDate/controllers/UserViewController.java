package me.sthomps9.AccommoDate.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import me.sthomps9.AccommoDate.dao.ExamDAO;
import me.sthomps9.AccommoDate.dao.MeetingDAO;
import me.sthomps9.AccommoDate.dao.UserDAO;
import me.sthomps9.AccommoDate.model.Course;
import me.sthomps9.AccommoDate.model.Exam;
import me.sthomps9.AccommoDate.model.StudentCourse;
import me.sthomps9.AccommoDate.model.User;
import me.sthomps9.AccommoDate.repository.ExamRepository;
import me.sthomps9.AccommoDate.repository.MeetingRepository;
import me.sthomps9.AccommoDate.repository.UserRepository;
import me.sthomps9.AccommoDate.request.NewStudentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserViewController {

    private final UserDAO userDAO;
    private final MeetingDAO meetingDAO;
    private final ExamDAO examDAO;
    private final PasswordEncoder passwordEncoder;

    public UserViewController(UserRepository userDao, ExamRepository examDao, MeetingRepository meetingDao, PasswordEncoder passwordEncoder) {
        this.userDAO = userDao.getDao();
        this.examDAO = examDao.getDao();
        this.meetingDAO = meetingDao.getDao();
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<User> addNewStudent(@RequestBody NewStudentRequest request) {

        User inRequest = request.getStudent();
        int[] courses = request.getCourses();

        User find = userDAO.findByID(inRequest.getStudentid()).orElse(null);
        if (find != null) return ResponseEntity.unprocessableEntity().build();


        inRequest.setRole("ROLE_USER");
        inRequest.setPwd(passwordEncoder.encode(inRequest.getStudentid()));
        inRequest.setTitle("Student");

        inRequest.addUser(userDAO);
        for (int crn : courses) {
            userDAO.addCourseRelation(inRequest.getStudentid(), crn);
        }

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<User> updateStudent(@RequestBody User user) throws JsonProcessingException {
        user.updateUser(userDAO);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/get/{id}")
    public String getUserByID(@PathVariable("id") String id) throws JsonProcessingException {
        User searching = userDAO.findByID(id).orElse(null);
        if (searching != null) {
            searching.setPwd(null);
        }
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(searching);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/get/students")
    public String getStudents() throws JsonProcessingException {
        List<User> students = userDAO.getStudents();
        for (User student : students) {
            student.pwd = null;
        }
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(students);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Exam> deleteUser(@PathVariable("id") String id) throws JsonProcessingException {
        User user = userDAO.findByID(id).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();
        userDAO.deleteUser(id);
        userDAO.deleteCourseRelations(id);
        meetingDAO.purgeMeetingsUser(id);
        examDAO.purgeExams(id);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/changepwd")
    public ResponseEntity<User> changePassword(@RequestBody ChangePwdRequest req) throws JsonProcessingException {
        User searching = userDAO.findByID(req.getUserid()).orElse(null);
        if (searching == null) {
            return ResponseEntity.notFound().build();
        }

        userDAO.updatePWD(passwordEncoder.encode(req.newpassword), searching.getStudentid());

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/changeprefname")
    public ResponseEntity<User> changePrefName(@RequestBody ChangePrefNameRequest req) throws JsonProcessingException {
        User searching = userDAO.findByID(req.getUserid()).orElse(null);
        if (searching == null) {
            return ResponseEntity.notFound().build();
        }

        userDAO.updatePrefName(req.preferredname, searching.getStudentid());

        return ResponseEntity.ok().build();
    }

    public static class ChangePwdRequest {
        String userid;
        String newpassword;
        String confirmpwd;
        String getUserid() { return userid; }
        String getNewpassword() { return newpassword; }
        String getConfirmpwd() { return confirmpwd; }
        void setUserid(String userid) { this.userid = userid; }
        void setNewpassword(String newpassword) { this.newpassword = newpassword; }
        void setConfirmpwd(String confirmpwd) { this.confirmpwd = confirmpwd; }
    }

    public static class ChangePrefNameRequest {
        String userid;
        String preferredname;
        String getUserid() { return userid; }
        String getPreferredname() { return preferredname; }
        void setUserid(String userid) { this.userid = userid; }
        void setPreferredname(String preferredname) { this.preferredname = preferredname; }
    }
}
