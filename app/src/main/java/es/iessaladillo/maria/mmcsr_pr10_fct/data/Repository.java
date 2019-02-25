package es.iessaladillo.maria.mmcsr_pr10_fct.data;

import java.util.List;

import androidx.lifecycle.LiveData;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Company;

public interface Repository {
    LiveData<List<Company>> queryAllCompany();
    void insertCompany(Company company);
    void deleteCompany(Company company);
    LiveData<Company> queryCompany(long companyId);
    void updateCompany(Company company);
}
