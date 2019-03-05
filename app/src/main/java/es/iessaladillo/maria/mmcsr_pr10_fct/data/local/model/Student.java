package es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model;

import java.util.Objects;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "student",
        foreignKeys = @ForeignKey(entity = Company.class,
        parentColumns = "name",
        childColumns = "nameCompany"),
        indices=@Index(value="name"))
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
    @ColumnInfo(name="nameCompany")
    private String nameCompany;
    @ColumnInfo(name="laborTutorName")
    private String laborTutorName;
    @ColumnInfo(name="laborTutorPhone")
    private String laborTutorPhone;
    @ColumnInfo(name="schedule")
    private String schedule;

    public Student(long idStudent, String name, String phone, String email, String grade, String nameCompany, String laborTutorName, String laborTutorPhone, String schedule) {
        this.idStudent = idStudent;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.grade = grade;
        this.nameCompany = nameCompany;
        this.laborTutorName = laborTutorName;
        this.laborTutorPhone = laborTutorPhone;
        this.schedule = schedule;
    }

    @Ignore
    public Student() {

    }


    public long getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(long idStudent) {
        this.idStudent = idStudent;
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

    public String getNameCompany() {
        return nameCompany;
    }

    public void setNameCompany(String nameCompany) {
        this.nameCompany = nameCompany;
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
}
