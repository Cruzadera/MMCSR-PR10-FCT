package es.iessaladillo.maria.mmcsr_pr10_fct.data;

import java.util.List;

import androidx.lifecycle.LiveData;
import es.iessaladillo.maria.mmcsr_pr10_fct.base.Resource;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Company;

public interface Repository {
    LiveData<List<Company>> queryAllCompany();
    LiveData<Resource<Long>> insertCompany(Company company);
    LiveData<Resource<Integer>> deleteCompany(Company company);
    LiveData<Company> queryCompany(long companyId);
    LiveData<Resource<Integer>> updateCompany(Company company);
}
