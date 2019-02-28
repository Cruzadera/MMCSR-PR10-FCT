package es.iessaladillo.maria.mmcsr_pr10_fct.ui.companies;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.Repository;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Company;

public class CompanyFragmentViewModel extends ViewModel {

    private final Repository repository;
    private LiveData<List<Company>> companies;

    CompanyFragmentViewModel(Repository repository) {
        this.repository = repository;
        companies = repository.queryAllCompany();
    }

    LiveData<List<Company>> getCompanies(){
        return companies;
    }

    public void insertCompany(Company company){
        repository.insertCompany(company);
    }

    public void updateCompany(Company company){
        repository.updateCompany(company);
    }

    void deleteCompany(Company item) {
        repository.deleteCompany(item);
    }
}
