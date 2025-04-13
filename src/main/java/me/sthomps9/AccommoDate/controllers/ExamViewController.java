package me.sthomps9.AccommoDate.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.websocket.server.PathParam;
import me.sthomps9.AccommoDate.dao.CourseDAO;
import me.sthomps9.AccommoDate.dao.ExamDAO;
import me.sthomps9.AccommoDate.dao.UserDAO;
import me.sthomps9.AccommoDate.model.Course;
import me.sthomps9.AccommoDate.model.Exam;
import me.sthomps9.AccommoDate.model.FullExam;
import me.sthomps9.AccommoDate.model.User;
import me.sthomps9.AccommoDate.repository.CourseRepository;
import me.sthomps9.AccommoDate.repository.ExamRepository;
import me.sthomps9.AccommoDate.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exam")
public class ExamViewController {

    private final ExamDAO examDAO;
    private final UserDAO userDAO;
    private final CourseDAO courseDAO;

    public ExamViewController(ExamRepository examDao,
                              UserRepository userDao,
                              CourseRepository courseDao) {
        this.examDAO = examDao.getDao();
        this.userDAO = userDao.getDao();
        this.courseDAO = courseDao.getDao();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/getbyid/{id}")
    public String getExamsByStudentID(@PathVariable("id") String id) throws JsonProcessingException {
        List<Exam> exams = examDAO.findByStudentID(id);
        List<FullExam> fullexams = exams.stream().map(exam -> {
            User user = userDAO.findByID(exam.getStudentid()).get();
            Course course = courseDAO.findByCRN(exam.getCrn());
            return new FullExam(user, exam, course);
        }).toList();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(fullexams);
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

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/getbetween/{id}")
    public String getExamsBetween(@PathVariable("id") String id,
                                  @RequestParam String start,
                                  @RequestParam String end) throws JsonProcessingException {
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

        System.out.println("Returning exams between dates (" + start + ", " + end + "): " + fullexams.size());

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(fullexams);
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
    @PostMapping("/addnewexam")
    public void addNewExam(@RequestBody Exam exam) {
        exam.writeExam(examDAO);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<Exam> updateExam(@RequestBody Exam exam) throws JsonProcessingException {
        exam.updateExam(examDAO);
        System.out.println(new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(exam));
        return ResponseEntity.ok(exam);
    }

}
