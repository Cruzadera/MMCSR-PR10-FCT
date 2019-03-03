package es.iessaladillo.maria.mmcsr_pr10_fct.data;

import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import es.iessaladillo.maria.mmcsr_pr10_fct.base.Resource;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.CompanyDao;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.StudentDao;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Company;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Student;

public class RepositoryImpl implements Repository {

    private final CompanyDao companyDao;
    private final StudentDao studentDao;

    public RepositoryImpl(CompanyDao companyDao, StudentDao studentDao) {
        this.companyDao = companyDao;
        this.studentDao = studentDao;
    }
    //-----------------------------------------COMPANY----------------------------------------------
    @Override
    public LiveData<List<Company>> queryAllCompany() {
        return companyDao.queryAllCompany();
    }

    @Override
    public LiveData<Resource<Long>> insertCompany(Company company) {
        MutableLiveData<Resource<Long>> result = new MutableLiveData<>();
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> {
            result.postValue(Resource.loading());
            try {
                long id = companyDao.insert(company);
                result.postValue(Resource.success(id));
            } catch (Exception e) {
                result.postValue(Resource.error(e));
            }
        });
        return result;
    }

    @Override
    public LiveData<Resource<Integer>> deleteCompany(Company company) {
        MutableLiveData<Resource<Integer>> result = new MutableLiveData<>();
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> {
            result.postValue(Resource.loading());
            try {
                int deleted = companyDao.delete(company);
                result.postValue(Resource.success(deleted));
            } catch (Exception e) {
                result.postValue(Resource.error(e));
            }
        });
        return result;
    }

    @Override
    public LiveData<Company> queryCompany(long companyId) {
        return companyDao.queryCompany(companyId);
    }

    @Override
    public LiveData<Resource<Integer>> updateCompany(Company company) {
        MutableLiveData<Resource<Integer>> result = new MutableLiveData<>();
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> {
            result.postValue(Resource.loading());
            try {
                int updated = companyDao.update(company);
                result.postValue(Resource.success(updated));
            } catch (Exception e) {
                result.postValue(Resource.error(e));
            }
        });
        return result;
    }

    @Override
    public LiveData<List<String>> queryAllCompanyNames() {
        return companyDao.queryAllCompanyNames();
    }


    //---------------------------------------STUDENT------------------------------------------------
    @Override
    public LiveData<List<Student>> queryStudents() {
        return studentDao.queryStudents();
    }

    @Override
    public LiveData<Resource<Long>> insertStudent(Student student) {
        MutableLiveData<Resource<Long>> result = new MutableLiveData<>();
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> {
            result.postValue(Resource.loading());
            try {
                long id = studentDao.insert(student);
                result.postValue(Resource.success(id));
            } catch (Exception e) {
                result.postValue(Resource.error(e));
            }
        });
        return result;
    }

    @Override
    public LiveData<Resource<Integer>> deleteStudent(Student student) {
        MutableLiveData<Resource<Integer>> result = new MutableLiveData<>();
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> {
            result.postValue(Resource.loading());
            try {
                int deleted = studentDao.delete(student);
                result.postValue(Resource.success(deleted));
            } catch (Exception e) {
                result.postValue(Resource.error(e));
            }
        });
        return result;
    }

    @Override
    public LiveData<Student> queryStudent(long studentId) {
        return studentDao.queryStudent(studentId);
    }

    @Override
    public LiveData<Resource<Integer>> updateStudent(Student student) {
        MutableLiveData<Resource<Integer>> result = new MutableLiveData<>();
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> {
            result.postValue(Resource.loading());
            try {
                int updated = studentDao.update(student);
                result.postValue(Resource.success(updated));
            } catch (Exception e) {
                result.postValue(Resource.error(e));
            }
        });
        return result;
    }

    @Override
    public LiveData<Company> queryCompanyStudent(long studentId) {
        return studentDao.getCompanyNameStudent(studentId);
    }

}
