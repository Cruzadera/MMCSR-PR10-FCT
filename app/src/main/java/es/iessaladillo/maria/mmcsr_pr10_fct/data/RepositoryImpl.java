package es.iessaladillo.maria.mmcsr_pr10_fct.data;

import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import es.iessaladillo.maria.mmcsr_pr10_fct.base.Resource;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.CompanyDao;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Company;

public class RepositoryImpl implements Repository {

    private final CompanyDao companyDao;

    public RepositoryImpl(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

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
}
