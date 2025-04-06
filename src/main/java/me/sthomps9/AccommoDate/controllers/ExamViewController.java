package me.sthomps9.AccommoDate.controllers;

import me.sthomps9.AccommoDate.dao.ExamDAO;
import me.sthomps9.AccommoDate.model.Exam;
import me.sthomps9.AccommoDate.repository.ExamRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/exam")
public class ExamViewController {

    private final ExamDAO examDAO;

    public ExamViewController(ExamRepository examDao) {
        this.examDAO = examDao.getDao();
    }

    @GetMapping("/getbyid")
    public String getExams(@RequestParam(name="id", required=false, defaultValue="-1") String id) {
        List<Exam> exams = examDAO.findByStudentID(id);
        for (Exam x : exams) {
            System.out.println(x.getCRN());
        }
        return ""+exams.size();
    }

}
