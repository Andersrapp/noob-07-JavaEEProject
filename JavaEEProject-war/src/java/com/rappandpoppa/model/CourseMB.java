package com.rappandpoppa.model;

import com.rappandpoppa.beans.AttendancelistFacadeLocal;
import com.rappandpoppa.beans.CourseFacadeLocal;
import com.rappandpoppa.beans.StudentFacadeLocal;
import com.rappandpoppa.beans.TeacherFacadeLocal;
import com.rappandpoppa.entities.Attendancelist;
import com.rappandpoppa.entities.Course;
import com.rappandpoppa.entities.Student;
import com.rappandpoppa.entities.Teacher;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Anders
 */
@ManagedBean
@RequestScoped
public class CourseMB {

    private Integer id;
    private String courseName;
    private String level;
    private String language;
    private int maxNumberOfStudents;
    private Teacher mainTeacher;
    private Integer studentToBeAddedId;
    private List<Student> courseStudents = new ArrayList<>();
    private List<Attendancelist> newAttendanceLists = new ArrayList<>();
    private List<Attendancelist> completeAttendanceLists = new ArrayList<>();
    private List<Date> lectureDates = new ArrayList<>();
    private Date startDate;
    private Integer numberOfWeeks;
    private List<String> daysOfTheWeek;

    private Integer teacherId;

    @EJB
    CourseFacadeLocal courseFacade;
    @EJB
    TeacherFacadeLocal teacherFacade;
    @EJB
    StudentFacadeLocal studentFacade;
    @EJB
    AttendancelistFacadeLocal attendanceListFacade;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String generateCourseCode() {
        char[] letters = this.courseName.toCharArray();
        StringBuilder sb1 = new StringBuilder();
        for (char letter : letters) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(letter);
            if (sb2.toString().matches("^[A-Z0-9]+$")) {
                sb1.append(sb2.toString());
            }
        }
        sb1.append("-15");
        return sb1.toString();
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getMaxNumberOfStudents() {
        return maxNumberOfStudents;
    }

    public void setMaxNumberOfStudents(int maxNumberOfStudents) {
        this.maxNumberOfStudents = maxNumberOfStudents;
    }

    public Teacher getMainTeacher() {
        return mainTeacher;
    }

    public void setMainTeacher(Teacher mainTeacher) {
        this.mainTeacher = mainTeacher;
    }

    public Integer getStudentToBeAddedId() {
        return studentToBeAddedId;
    }

    public void setStudentToBeAddedId(Integer studentToBeAddedId) {
        this.studentToBeAddedId = studentToBeAddedId;
    }

    public List<Student> getCourseStudents() {
        if (id != null) {
            return courseStudents = courseFacade.find(id).getStudentList();
        } else {
            return null;
        }
    }

    public void setCourseStudents(List<Student> courseStudents) {
        this.courseStudents = courseStudents;
    }

    public List<Attendancelist> getNewAttendanceLists() {
        return newAttendanceLists;
    }

    public void setNewAttendanceLists(List<Attendancelist> newAttendanceLists) {
        this.newAttendanceLists = newAttendanceLists;
    }

    public List<Attendancelist> getCompleteAttendanceLists() {
        return completeAttendanceLists;
    }

    public void setCompleteAttendanceLists(List<Attendancelist> completeAttendanceLists) {
        this.completeAttendanceLists = completeAttendanceLists;
    }

    public void addLectureDate(Date lectureDate) {
        lectureDates.add(lectureDate);
    }

    public void removeLectureDate(Date lectureDate) {
        lectureDates.remove(lectureDate);
    }

    public List<Date> getLectureDates() {
        return lectureDates;
    }

    public void setLectureDates(List<Date> lectureDates) {
        this.lectureDates = lectureDates;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer getNumberOfWeeks() {
        return numberOfWeeks;
    }

    public void setNumberOfWeeks(Integer numberOfWeeks) {
        this.numberOfWeeks = numberOfWeeks;
    }

    public List<String> getDaysOfTheWeek() {
        return daysOfTheWeek;
    }

    public void setDaysOfTheWeek(List<String> daysOfTheWeek) {
        this.daysOfTheWeek = daysOfTheWeek;
    }
    
    public void addCourse() {
        Course createdCourse = new Course();
        String courseCode = generateCourseCode();
        int numberOfAttendanceLists = daysOfTheWeek.size() * numberOfWeeks;
        createdCourse.setCourseName(this.courseName);
        createdCourse.setCourseCode(courseCode);
        createdCourse.setCourseLanguage(this.language);
        createdCourse.setCourseLevel(this.level);
        createdCourse.setMaxNumberOfStudents(this.maxNumberOfStudents);
        this.mainTeacher = teacherFacade.find(teacherId);
        createdCourse.setTeacher(this.mainTeacher);
        for (int i = 0; i < numberOfAttendanceLists; ++i) {
            Attendancelist attendanceList = new Attendancelist();
            newAttendanceLists.add(attendanceList);
        }
        for (String day : daysOfTheWeek) {
            Calendar cal = Calendar.getInstance();
            boolean firstAttendanceList = true;
            Date attendanceDate = startDate;
            for (int i = 0; i < numberOfWeeks; ++i) {
                Attendancelist attendanceList = newAttendanceLists.get(0);
                switch (day) {
                    case "Mon":
                        if (firstAttendanceList) {
                            attendanceList.setAttendanceDate(startDate);
                            attendanceList.setCourse(createdCourse);
                            completeAttendanceLists.add(attendanceList);
                            newAttendanceLists.remove(attendanceList);
                            firstAttendanceList = false;
                        } else {
                            cal.setTime(attendanceDate);
                            cal.add(Calendar.DATE, 7);
                            attendanceDate = cal.getTime();
                            attendanceList.setAttendanceDate(attendanceDate);
                            attendanceList.setCourse(createdCourse);
                            completeAttendanceLists.add(attendanceList);
                            newAttendanceLists.remove(attendanceList);
                        }
                        break;
                    case "Tue":
                        if (firstAttendanceList) {
                            cal.setTime(startDate);
                            cal.add(Calendar.DATE, 1);
                            attendanceDate = cal.getTime();
                            attendanceList.setAttendanceDate(attendanceDate);
                            attendanceList.setCourse(createdCourse);
                            completeAttendanceLists.add(attendanceList);
                            newAttendanceLists.remove(attendanceList);
                            firstAttendanceList = false;
                        } else {
                            cal.setTime(attendanceDate);
                            cal.add(Calendar.DATE, 7);
                            attendanceDate = cal.getTime();
                            attendanceList.setAttendanceDate(attendanceDate);
                            attendanceList.setCourse(createdCourse);
                            completeAttendanceLists.add(attendanceList);
                            newAttendanceLists.remove(attendanceList);
                        }
                        break;
                    case "Wed":
                        if (firstAttendanceList) {
                            cal.setTime(startDate);
                            cal.add(Calendar.DATE, 2);
                            attendanceDate = cal.getTime();
                            attendanceList.setAttendanceDate(attendanceDate);
                            attendanceList.setCourse(createdCourse);
                            completeAttendanceLists.add(attendanceList);
                            newAttendanceLists.remove(attendanceList);
                            firstAttendanceList = false;
                        } else {
                            cal.setTime(attendanceDate);
                            cal.add(Calendar.DATE, 7);
                            attendanceDate = cal.getTime();
                            attendanceList.setAttendanceDate(attendanceDate);
                            attendanceList.setCourse(createdCourse);
                            completeAttendanceLists.add(attendanceList);
                            newAttendanceLists.remove(attendanceList);
                        }
                        break;
                    case "Thu":
                        if (firstAttendanceList) {
                            cal.setTime(startDate);
                            cal.add(Calendar.DATE, 3);
                            attendanceDate = cal.getTime();
                            attendanceList.setAttendanceDate(attendanceDate);
                            attendanceList.setCourse(createdCourse);
                            completeAttendanceLists.add(attendanceList);
                            newAttendanceLists.remove(attendanceList);
                            firstAttendanceList = false;
                        } else {
                            cal.setTime(attendanceDate);
                            cal.add(Calendar.DATE, 7);
                            attendanceDate = cal.getTime();
                            attendanceList.setAttendanceDate(attendanceDate);
                            attendanceList.setCourse(createdCourse);
                            completeAttendanceLists.add(attendanceList);
                            newAttendanceLists.remove(attendanceList);
                        }
                        break;
                    case "Fri":
                        if (firstAttendanceList) {
                            cal.setTime(startDate);
                            cal.add(Calendar.DATE, 4);
                            attendanceDate = cal.getTime();
                            attendanceList.setAttendanceDate(attendanceDate);
                            attendanceList.setCourse(createdCourse);
                            completeAttendanceLists.add(attendanceList);
                            newAttendanceLists.remove(attendanceList);
                            firstAttendanceList = false;
                        } else {
                            cal.setTime(attendanceDate);
                            cal.add(Calendar.DATE, 7);
                            attendanceDate = cal.getTime();
                            attendanceList.setAttendanceDate(attendanceDate);
                            attendanceList.setCourse(createdCourse);
                            completeAttendanceLists.add(attendanceList);
                            newAttendanceLists.remove(attendanceList);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        createdCourse.setAttendancelistList(completeAttendanceLists);
        courseFacade.create(createdCourse);
    }

    public List<Course> viewAllCourses() {
        return this.courseFacade.findAll();
    }

    public List<String> getAllCourseNames() {
        List<Course> coursesFound = courseFacade.findAll();
        List<String> courseNames = new ArrayList<>();
        for (Course course : coursesFound) {
            courseNames.add(course.getCourseName());
        }
        return courseNames;
    }

    public void addStudents() {
        Course courseToBeEdited = courseFacade.find(id);
        this.maxNumberOfStudents = courseToBeEdited.getMaxNumberOfStudents();
        if (courseStudents.size() < maxNumberOfStudents) {
            courseStudents.add(studentFacade.find(studentToBeAddedId));
            courseToBeEdited.setStudentList(courseStudents);
            courseFacade.edit(courseToBeEdited);
        }
    }
}
