package es.iessaladillo.maria.mmcsr_pr10_fct.data;

import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
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
    public void insertCompany(Company company) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> companyDao.insertCompany(company));
    }

    @Override
    public void deleteCompany(Company company) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> companyDao.deleteCompany(company));
    }

    @Override
    public LiveData<Company> queryCompany(long companyId) {
        return companyDao.queryCompany(companyId);
    }

    @Override
    public void updateCompany(Company company) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> companyDao.updateCompany(company));
    }
}
