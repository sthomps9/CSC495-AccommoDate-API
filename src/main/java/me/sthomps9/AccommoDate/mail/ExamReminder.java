package me.sthomps9.AccommoDate.mail;

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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Component
public class ExamReminder {

    private final ExamDAO examDAO;
    private final UserDAO userDAO;
    private final CourseDAO courseDAO;

    public ExamReminder(ExamRepository examDao,
                              UserRepository userDao,
                              CourseRepository courseDao) {
        this.examDAO = examDao.getDao();
        this.userDAO = userDao.getDao();
        this.courseDAO = courseDao.getDao();
    }

    @Scheduled(cron = "0 0 7 * * *")
    public void notifyStudents() {
        List<Exam> tomorrow = examDAO.getByDate(LocalDate.now().plusDays(1));
        List<FullExam> fullexams = tomorrow.stream().map(exam -> {
            User user = userDAO.findByID(exam.getStudentid()).get();
            Course course = courseDAO.findByCRN(exam.getCrn());
            return new FullExam(user, exam, course);
        }).toList();

        for (FullExam exam : fullexams) {
            if (!exam.getExam().isStudentnotified()) {
                MailHandler.notifyStudent(exam.getCourse(), exam.getExam(), exam.getUser());
            }
        }
    }
}
