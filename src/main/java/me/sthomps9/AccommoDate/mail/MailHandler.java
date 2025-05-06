package me.sthomps9.AccommoDate.mail;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import me.sthomps9.AccommoDate.model.Course;
import me.sthomps9.AccommoDate.model.Exam;
import me.sthomps9.AccommoDate.model.User;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class MailHandler {

    private static final String username = "accommodate.app@gmail.com";
    private static final String password = "wwog barj ccor thyz";

    private static Session getSession() {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        return Session.getInstance(prop,
                new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

    }

    public static void notifyStudent(Course course, Exam exam, User student) {
        try {

            Message message = new MimeMessage(getSession());
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
//                    InternetAddress.parse(student.getEmail() + "@oswego.edu")
                    InternetAddress.parse("sthomps9" + "@oswego.edu")
            );
            message.setSubject("Exam for " + course.getCourseid());
            message.setText("Good morning,"
                    + "\n\nWe have you scheduled to take your " + course.getCourseid() + " exam in our office on " + getFormattedDate(exam.getExamdate())
                    + " at " + getFormattedTime(exam.getExamtime()) + ". Please bring your student ID and any materials you may need for the exam. "
                    + "If you have any questions please email our office at access@oswego.edu. "
                    + "\n\nThank you," +
                    "\nAccessibility Resources");


            Transport.send(message);

            System.out.println("Notified " + student.getFullname() + " for " + course.getCourseid());

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    public static String getFormattedDate(LocalDate date) {
        return date.getDayOfWeek().name() + ", " + date.getMonth().name() + " " + date.getDayOfMonth();
    }

    public static String getFormattedTime(Time time) {
        return time.toLocalTime().format(DateTimeFormatter.ofPattern("h:mma")).toLowerCase();
    }

    public static void requestExam(Course course, Exam exam) {
        try {

            Message message = new MimeMessage(getSession());
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("sthomps9" + "@oswego.edu")
            );
            message.setSubject("OAR Exam Request: " + course.getCourseid());
            message.setText("Professor " + course.getInstructor() + ","
                    + "\n\n A student has requested to take an exam for your course " + course.getCourseid() + ": " + course.getCoursename() + " (" + course.getSectionnum() + "). "
                    + "They have requested to take the exam on " + getFormattedDate(exam.getExamdate()) + " at " + getFormattedTime(exam.getExamtime()) + ". "
                    + "Please either send us the exam, or deliver it to our office in 155 Marano Campus Center, along with any instructions or resources the student may need. "
                    + "\n\nThank you, \nAccessibility Resources");


            Transport.send(message);

            System.out.println("Requested exam from " + course.getInstructor() + " for " + course.getCourseid());

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

}
