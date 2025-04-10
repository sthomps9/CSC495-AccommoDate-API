package me.sthomps9.AccommoDate.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nimbusds.jose.shaded.gson.Gson;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
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
        List<Exam> exams = examDAO.getByDate(Date.valueOf(date));

        List<FullExam> fullexams = exams.stream().map(exam -> {
            User user = userDAO.findByID(exam.getStudentid()).get();
            Course course = courseDAO.findByCRN(exam.getCrn());
            return new FullExam(user, exam, course);
        }).toList();

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

}
