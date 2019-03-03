package es.iessaladillo.maria.mmcsr_pr10_fct.data.local;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import es.iessaladillo.maria.mmcsr_pr10_fct.base.BaseDao;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Company;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Student;

@Dao
public interface StudentDao extends BaseDao<Student> {
    @Query("SELECT * FROM student")
    LiveData<List<Student>> queryStudents();

    @Query("SELECT * FROM student WHERE idStudent = :studentId")
    LiveData<Student> queryStudent(long studentId);

    @Query("SELECT _companyname FROM student WHERE idStudent = :studentId")
    LiveData<Company> getCompanyNameStudent(long studentId);

    @Query("SELECT _companyname FROM student")
    LiveData<List<String>> getAllCompaniesNameStudents();
}
