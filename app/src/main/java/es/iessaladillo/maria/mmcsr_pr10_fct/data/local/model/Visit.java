package es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "visit",
        foreignKeys = @ForeignKey(entity = Student.class,
                parentColumns = "nameStudent",
                childColumns = "nameStu"))
public class Visit {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="idVisit")
    private long idVisit;
    @ColumnInfo(name = "date")
    private String date;
    @ColumnInfo(name = "startTime")
    private String startTime;
    @ColumnInfo(name = "endTime")
    private String endTime;
    @ColumnInfo(name = "observations")
    private String observations;
    @ColumnInfo(name = "nameStu")
    private String nameStudent;
    @ColumnInfo(name = "nextVisit")
    private String nextVisit;

    public Visit(long idVisit, String date, String startTime, String endTime, String observations, String nameStudent, String nextVisit) {
        this.idVisit = idVisit;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.observations = observations;
        this.nameStudent = nameStudent;
        this.nextVisit = nextVisit;
    }

    @Ignore
    public Visit() {

    }


    public long getIdVisit() {
        return idVisit;
    }

    public void setIdVisit(long idVisit) {
        this.idVisit = idVisit;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getNameStudent() {
        return nameStudent;
    }

    public void setNameStudent(String nameStudent) {
        this.nameStudent = nameStudent;
    }

    public String getNextVisit() {
        return nextVisit;
    }

    public void setNextVisit(String nextVisit) {
        this.nextVisit = nextVisit;
    }
}
