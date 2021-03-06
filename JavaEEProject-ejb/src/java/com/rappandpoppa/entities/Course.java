package com.rappandpoppa.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Benjamin
 */
@Entity
@Table(name = "course")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Course.findAll", query = "SELECT c FROM Course c"),
    @NamedQuery(name = "Course.findById", query = "SELECT c FROM Course c WHERE c.id = :id"),
    @NamedQuery(name = "Course.findByCourseName", query = "SELECT c FROM Course c WHERE c.courseName = :courseName"),
    @NamedQuery(name = "Course.findByCourseCode", query = "SELECT c FROM Course c WHERE c.courseCode = :courseCode"),
    @NamedQuery(name = "Course.findByCourseLevel", query = "SELECT c FROM Course c WHERE c.courseLevel = :courseLevel"),
    @NamedQuery(name = "Course.findByCourseLanguage", query = "SELECT c FROM Course c WHERE c.courseLanguage = :courseLanguage"),
    @NamedQuery(name = "Course.findByMaxNumberOfStudents", query = "SELECT c FROM Course c WHERE c.maxNumberOfStudents = :maxNumberOfStudents")})
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 32)
    @Column(name = "courseName")
    private String courseName;
    @Size(max = 16)
    @Column(name = "courseCode")
    private String courseCode;
    @Size(max = 16)
    @Column(name = "courseLevel")
    private String courseLevel;
    @Size(max = 16)
    @Column(name = "courseLanguage")
    private String courseLanguage;
    @Column(name = "maxNumberOfStudents")
    private Integer maxNumberOfStudents;
    @JoinTable(name = "course_student", joinColumns = {
        @JoinColumn(name = "course_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "student_id", referencedColumnName = "id")})
    @ManyToMany
    private List<Student> studentList;
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    @ManyToOne
    private Teacher teacher;
    @OneToMany(mappedBy = "course", cascade = CascadeType.PERSIST)
    private List<Attendancelist> attendancelistList = new ArrayList<>();

    public Course() {
    }

    public Course(Integer id) {
        this.id = id;
    }

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

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseLevel() {
        return courseLevel;
    }

    public void setCourseLevel(String courseLevel) {
        this.courseLevel = courseLevel;
    }

    public String getCourseLanguage() {
        return courseLanguage;
    }

    public void setCourseLanguage(String courseLanguage) {
        this.courseLanguage = courseLanguage;
    }

    public Integer getMaxNumberOfStudents() {
        return maxNumberOfStudents;
    }

    public void setMaxNumberOfStudents(Integer maxNumberOfStudents) {
        this.maxNumberOfStudents = maxNumberOfStudents;
    }

    @XmlTransient
    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @XmlTransient
    public List<Attendancelist> getAttendancelistList() {
        return attendancelistList;
    }

    public void setAttendancelistList(List<Attendancelist> attendancelistList) {
        this.attendancelistList = attendancelistList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Course)) {
            return false;
        }
        Course other = (Course) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.courseName + " " + this.courseCode;
    }
    
}
