package es.iessaladillo.maria.mmcsr_pr10_fct.data;

import java.util.List;

import androidx.lifecycle.LiveData;
import es.iessaladillo.maria.mmcsr_pr10_fct.base.Resource;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Company;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Student;

public interface Repository {
    //Company
    LiveData<List<Company>> queryAllCompany();
    LiveData<Resource<Long>> insertCompany(Company company);
    LiveData<Resource<Integer>> deleteCompany(Company company);
    LiveData<Company> queryCompany(long companyId);
    LiveData<Resource<Integer>> updateCompany(Company company);
    LiveData<List<String>> queryAllCompanyNames();
    //Student
    LiveData<List<Student>> queryStudents();
    LiveData<Resource<Long>> insertStudent(Student student);
    LiveData<Resource<Integer>> deleteStudent(Student student);
    LiveData<Student> queryStudent(long studentId);
    LiveData<Resource<Integer>> updateStudent(Student student);
    LiveData<String> queryCompanyStudent(long studentId);
    //Visit
}
