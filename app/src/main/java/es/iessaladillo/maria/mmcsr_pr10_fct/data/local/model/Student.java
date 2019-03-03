package es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model;

import java.util.Objects;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "student")
public class Student {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="idStudent")
    private long idStudent;
    @ColumnInfo(name="nameStudent")
    private String name;
    @ColumnInfo(name="phoneStudent")
    private String phone;
    @ColumnInfo(name="emailStudent")
    private String email;
    @ColumnInfo(name="grade")
    private String grade;
    @Embedded(prefix = "_company")
    private Company company;
    @ColumnInfo(name="laborTutorName")
    private String laborTutorName;
    @ColumnInfo(name="laborTutorPhone")
    private String laborTutorPhone;
    @ColumnInfo(name="schedule")
    private String schedule;

    public Student(long idStudent, String name, String phone, String email, String grade, Company company, String laborTutorName, String laborTutorPhone, String schedule) {
        this.idStudent = idStudent;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.grade = grade;
        this.company = company;
        this.laborTutorName = laborTutorName;
        this.laborTutorPhone = laborTutorPhone;
        this.schedule = schedule;
    }

    @Ignore
    public Student() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getLaborTutorName() {
        return laborTutorName;
    }

    public void setLaborTutorName(String laborTutorName) {
        this.laborTutorName = laborTutorName;
    }

    public String getLaborTutorPhone() {
        return laborTutorPhone;
    }

    public void setLaborTutorPhone(String laborTutorPhone) {
        this.laborTutorPhone = laborTutorPhone;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public long getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(long idStudent) {
        this.idStudent = idStudent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return idStudent == student.idStudent &&
                Objects.equals(name, student.name) &&
                Objects.equals(phone, student.phone) &&
                Objects.equals(email, student.email) &&
                Objects.equals(grade, student.grade) &&
                Objects.equals(company, student.company) &&
                Objects.equals(laborTutorName, student.laborTutorName) &&
                Objects.equals(laborTutorPhone, student.laborTutorPhone) &&
                Objects.equals(schedule, student.schedule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idStudent, name, phone, email, grade, company, laborTutorName, laborTutorPhone, schedule);
    }
}
