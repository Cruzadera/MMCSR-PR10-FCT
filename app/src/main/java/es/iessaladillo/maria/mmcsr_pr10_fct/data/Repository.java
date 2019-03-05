package es.iessaladillo.maria.mmcsr_pr10_fct.data;

import java.util.List;

import androidx.lifecycle.LiveData;
import es.iessaladillo.maria.mmcsr_pr10_fct.base.Resource;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Company;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Student;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Visit;

public interface Repository {
    //Company
    LiveData<List<Company>> queryAllCompany();

    LiveData<Resource<Long>> insertCompany(Company company);

    LiveData<Resource<Integer>> deleteCompany(Company company);

    LiveData<Company> queryCompany(long companyId);

    LiveData<Resource<Integer>> updateCompany(Company company);

    LiveData<List<String>> queryAllCompanyNames();

    LiveData<String> queryAllCompanyByName(String nameCompany);

    //Student
    LiveData<List<Student>> queryStudents();

    LiveData<Resource<Long>> insertStudent(Student student);

    LiveData<Resource<Integer>> deleteStudent(Student student);

    LiveData<Student> queryStudent(long studentId);

    LiveData<Resource<Integer>> updateStudent(Student student);

    //Visit
    LiveData<Resource<Long>> insertVisit(Visit visit);

    LiveData<Resource<Integer>> deleteVisit(Visit visit);

    LiveData<Resource<Integer>> updateVisit(Visit visit);

    LiveData<List<Visit>> queryVisits();

    LiveData<Visit> queryVisit(long studentId);
}
