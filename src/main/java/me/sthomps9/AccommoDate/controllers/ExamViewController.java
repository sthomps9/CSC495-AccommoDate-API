package me.sthomps9.AccommoDate.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nimbusds.jose.shaded.gson.Gson;
import me.sthomps9.AccommoDate.dao.ExamDAO;
import me.sthomps9.AccommoDate.model.Exam;
import me.sthomps9.AccommoDate.repository.ExamRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/exam")
public class ExamViewController {

    private final ExamDAO examDAO;

    public ExamViewController(ExamRepository examDao) {
        this.examDAO = examDao.getDao();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getbyid")
    public String getExamsByStudentID(@RequestParam(name="id") String id) throws JsonProcessingException {
        List<Exam> exams = examDAO.findByStudentID(id);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(exams);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getbydate")
    public String getExamsByDate(@RequestParam(name="date", required=false, defaultValue="2025-02-12") String date) throws JsonProcessingException {
        List<Exam> exams = examDAO.getByDate(Date.valueOf(date));
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(exams);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getall")
    public String getAllExams() throws JsonProcessingException {
        List<Exam> exams = examDAO.getAll();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(exams);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/addnewexam")
    public void addNewExam(@RequestBody Exam exam) {
        exam.writeExam(examDAO);
    }

}
