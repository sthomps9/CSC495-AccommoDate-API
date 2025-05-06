package me.sthomps9.AccommoDate.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import me.sthomps9.AccommoDate.dao.CourseDAO;
import me.sthomps9.AccommoDate.dao.ExamDAO;
import me.sthomps9.AccommoDate.dao.UserDAO;
import me.sthomps9.AccommoDate.model.Course;
import me.sthomps9.AccommoDate.model.FullExam;
import me.sthomps9.AccommoDate.model.StudentCourse;
import me.sthomps9.AccommoDate.model.User;
import me.sthomps9.AccommoDate.repository.CourseRepository;
import me.sthomps9.AccommoDate.repository.ExamRepository;
import me.sthomps9.AccommoDate.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/course")
public class CourseViewController {

    private final ExamDAO examDAO;
    private final UserDAO userDAO;
    private final CourseDAO courseDAO;

    public CourseViewController(ExamRepository examDao,
                              UserRepository userDao,
                              CourseRepository courseDao) {
        this.examDAO = examDao.getDao();
        this.userDAO = userDao.getDao();
        this.courseDAO = courseDao.getDao();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/get")
    public String getAllCourses() throws JsonProcessingException {
        List<Course> courses = courseDAO.getAll();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(courses);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/getbyid/{id}")
    public String getCoursesByID(@PathVariable("id") String id) throws JsonProcessingException {
        User searching = userDAO.findByID(id).orElse(null);
        List<Course> courses = new ArrayList<>();
        if (searching != null) {
            List<Integer> crns = userDAO.findCRNByID(id);
            for (Integer i : crns) {
                Course c = courseDAO.findByCRN(i);
                if (c != null) courses.add(c);
            }
        }
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(courses);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/getfullbyid/{id}")
    public String getFullCoursesByID(@PathVariable("id") String id) throws JsonProcessingException {
        User searching = userDAO.findByID(id).orElse(null);
        List<StudentCourse> courses = new ArrayList<>();
        if (searching != null) {
            List<Integer> crns = userDAO.findCRNByID(id);
            for (Integer i : crns) {
                Course c = courseDAO.findByCRN(i);
                if (c != null) courses.add(new StudentCourse(searching, c));
            }
        }
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(courses);
    }
}
