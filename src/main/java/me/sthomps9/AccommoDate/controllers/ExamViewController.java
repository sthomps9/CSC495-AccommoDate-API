package me.sthomps9.AccommoDate.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nimbusds.jose.shaded.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import me.sthomps9.AccommoDate.dao.CourseDAO;
import me.sthomps9.AccommoDate.dao.ExamDAO;
import me.sthomps9.AccommoDate.dao.UserDAO;
import me.sthomps9.AccommoDate.jwt.JwtUtil;
import me.sthomps9.AccommoDate.mail.MailHandler;
import me.sthomps9.AccommoDate.model.*;
import me.sthomps9.AccommoDate.repository.CourseRepository;
import me.sthomps9.AccommoDate.repository.ExamRepository;
import me.sthomps9.AccommoDate.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exam")
public class ExamViewController {

    private final ExamDAO examDAO;
    private final UserDAO userDAO;
    private final CourseDAO courseDAO;
    private final JwtUtil jwtUtil;

    public ExamViewController(ExamRepository examDao,
                              UserRepository userDao,
                              CourseRepository courseDao,
                              JwtUtil jwtUtil) {
        this.examDAO = examDao.getDao();
        this.userDAO = userDao.getDao();
        this.courseDAO = courseDao.getDao();
        this.jwtUtil = jwtUtil;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/getbyid/{id}")
    public ResponseEntity<String> getExamsByStudentID(HttpServletRequest request, @PathVariable("id") String id) throws JsonProcessingException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String jwt = authHeader.substring(7);
        String authID = jwtUtil.extractID(jwt);
        String role = jwtUtil.extractRole(jwt);
        if (role.equals("ROLE_USER") && !authID.equals(id)) {
            return ResponseEntity.status(403).build();
        }

        List<Exam> exams = examDAO.findByStudentID(id);
        List<FullExam> fullexams = exams.stream().map(exam -> {
            User user = userDAO.findByID(exam.getStudentid()).get();
            Course course = courseDAO.findByCRN(exam.getCrn());
            return new FullExam(user, exam, course);
        }).toList();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ResponseEntity.ok(ow.writeValueAsString(fullexams));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Exam> deleteExam(@PathVariable("id") String id) throws JsonProcessingException {
        Exam exam = examDAO.findByID(id).orElse(null);
        if (exam == null) return ResponseEntity.notFound().build();
        examDAO.deleteExam(id);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/getbyid/past/{id}/{date}/{time}")
    public ResponseEntity<String> getPastExamsByStudentID(HttpServletRequest request, @PathVariable("date") String date, @PathVariable("id") String id, @PathVariable("time") String time) throws JsonProcessingException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String jwt = authHeader.substring(7);
        String authID = jwtUtil.extractID(jwt);
        String role = jwtUtil.extractRole(jwt);
        if (role.equals("ROLE_USER") && !authID.equals(id)) {
            return ResponseEntity.status(403).build();
        }

        String[] parts1 = date.split("-");
        int year1 = Integer.parseInt(parts1[0]);
        int month1 = Integer.parseInt(parts1[1]);
        int day1 = Integer.parseInt(parts1[2]);

        Time currentT = Time.valueOf(time);
        LocalDate currentD = LocalDate.of(year1, month1, day1);

        List<Exam> exams = examDAO.findPastByStudentID(id, currentD);
        List<Exam> past = new ArrayList<>();
        for (Exam x : exams) {
            if (x.getExamdate().isBefore(currentD)) past.add(x);
            else if (x.getExamdate().isEqual(currentD) && x.getExamtime().before(currentT)) past.add(x);
        }

        List<FullExam> fullexams = past.stream().map(exam -> {
            User user = userDAO.findByID(exam.getStudentid()).get();
            Course course = courseDAO.findByCRN(exam.getCrn());
            return new FullExam(user, exam, course);
        }).toList();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ResponseEntity.ok(ow.writeValueAsString(fullexams));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/getbyid/upcoming/{id}/{date}/{time}")
    public ResponseEntity<String> getUpcomingExamsByStudentID(HttpServletRequest request, @PathVariable("date") String date, @PathVariable("id") String id, @PathVariable("time") String time) throws JsonProcessingException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String jwt = authHeader.substring(7);
        String authID = jwtUtil.extractID(jwt);
        String role = jwtUtil.extractRole(jwt);
        if (role.equals("ROLE_USER") && !authID.equals(id)) {
            return ResponseEntity.status(403).build();
        }

        String[] parts1 = date.split("-");
        int year1 = Integer.parseInt(parts1[0]);
        int month1 = Integer.parseInt(parts1[1]);
        int day1 = Integer.parseInt(parts1[2]);

        Time currentT = Time.valueOf(time);
        LocalDate currentD = LocalDate.of(year1, month1, day1);

        List<Exam> exams = examDAO.findUpcomingByStudentID(id, currentD);
        List<Exam> past = new ArrayList<>();
        for (Exam x : exams) {
            if (x.getExamdate().isAfter(currentD)) past.add(x);
            else if (x.getExamdate().isEqual(currentD) && x.getExamtime().after(currentT)) past.add(x);
        }

        List<FullExam> fullexams = past.stream().map(exam -> {
            User user = userDAO.findByID(exam.getStudentid()).get();
            Course course = courseDAO.findByCRN(exam.getCrn());
            return new FullExam(user, exam, course);
        }).toList();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ResponseEntity.ok(ow.writeValueAsString(fullexams));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getbydate/{date}")
    public String getExamsByDate(@PathVariable("date") String date) throws JsonProcessingException {
        String[] parts = date.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);
        List<Exam> exams = examDAO.getByDate(LocalDate.of(year, month, day));

        List<FullExam> fullexams = exams.stream().map(exam -> {
            User user = userDAO.findByID(exam.getStudentid()).get();
            Course course = courseDAO.findByCRN(exam.getCrn());
            return new FullExam(user, exam, course);
        }).toList();

        System.out.println("Returning exams by date (" + date + "): " + fullexams.size());

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(fullexams);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getforboard/{date}")
    public String getExamsForBoard(@PathVariable("date") String date) throws JsonProcessingException {
        String[] parts = date.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);
        List<Exam> exams = examDAO.getForBoard(LocalDate.of(year, month, day));

        List<FullExam> fullexams = exams.stream().map(exam -> {
            User user = userDAO.findByID(exam.getStudentid()).get();
            Course course = courseDAO.findByCRN(exam.getCrn());
            return new FullExam(user, exam, course);
        }).toList();

        System.out.println("Returning exams by date (" + date + "): " + fullexams.size());

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(fullexams);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/getbetween/{id}")
    public ResponseEntity<String> getExamsBetween(@PathVariable("id") String id,
                                  @RequestParam String start,
                                  @RequestParam String end,
                                  HttpServletRequest request) throws JsonProcessingException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String jwt = authHeader.substring(7);
        String authID = jwtUtil.extractID(jwt);
        String role = jwtUtil.extractRole(jwt);
        if (role.equals("ROLE_USER") && !authID.equals(id)) {
            return ResponseEntity.status(403).build();
        }

        String[] parts1 = start.split("-");
        int year1 = Integer.parseInt(parts1[0]);
        int month1 = Integer.parseInt(parts1[1]);
        int day1 = Integer.parseInt(parts1[2]);

        String[] parts2 = end.split("-");
        int year2 = Integer.parseInt(parts2[0]);
        int month2 = Integer.parseInt(parts2[1]);
        int day2 = Integer.parseInt(parts2[2]);

        List<Exam> exams = examDAO.getBetweenDates(LocalDate.of(year1, month1, day1), LocalDate.of(year2, month2, day2), id);

        List<FullExam> fullexams = exams.stream().map(exam -> {
            User user = userDAO.findByID(exam.getStudentid()).get();
            Course course = courseDAO.findByCRN(exam.getCrn());
            return new FullExam(user, exam, course);
        }).toList();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ResponseEntity.ok(ow.writeValueAsString(fullexams));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getall")
    public String getAllExams() throws JsonProcessingException {
        List<Exam> exams = examDAO.getAll();
        List<FullExam> fullexams = exams.stream().map(exam -> {
            User user = userDAO.findByID(exam.getStudentid()).get();
            Course course = courseDAO.findByCRN(exam.getCrn());
            return new FullExam(user, exam, course);
        }).toList();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(fullexams);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/add")
    public ResponseEntity<Exam> addNewExam(@RequestBody Exam exam, HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String jwt = authHeader.substring(7);
        String authID = jwtUtil.extractID(jwt);
        String role = jwtUtil.extractRole(jwt);
        if (role.equals("ROLE_USER") && !authID.equals(exam.getStudentid())) {
            return ResponseEntity.status(403).build();
        }

        exam.setExamid(UUID.randomUUID().toString());
        exam.writeExam(examDAO);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<Exam> updateExam(@RequestBody Exam exam) throws JsonProcessingException {
        exam.updateExam(examDAO);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/request/{examid}")
    public ResponseEntity<Exam> requestExam(@PathVariable("examid") String examid) throws JsonProcessingException {
        Exam requesting = examDAO.findByID(examid).orElse(null);

        if (requesting == null) return ResponseEntity.badRequest().build();
        Course course = courseDAO.findByCRN(requesting.getCrn());

        MailHandler.requestExam(course, requesting);
        requesting.setExamrequested(true);
        requesting.updateExam(examDAO);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/getpending")
    public String getPendingExams() throws JsonProcessingException {
        List<Exam> pending = examDAO.getPending();

        List<FullExam> fullexams = pending.stream().map(exam -> {
            User user = userDAO.findByID(exam.getStudentid()).get();
            Course course = courseDAO.findByCRN(exam.getCrn());
            return new FullExam(user, exam, course);
        }).toList();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(fullexams);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/getnew")
    public String getNewExams() throws JsonProcessingException {
        List<Exam> newExams = examDAO.getNew();

        List<FullExam> fullexams = newExams.stream().map(exam -> {
            User user = userDAO.findByID(exam.getStudentid()).get();
            Course course = courseDAO.findByCRN(exam.getCrn());
            return new FullExam(user, exam, course);
        }).toList();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(fullexams);
    }

}
