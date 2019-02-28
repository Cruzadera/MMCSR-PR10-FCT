package es.iessaladillo.maria.mmcsr_pr10_fct.ui.add_company;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.Repository;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Company;

public class AddCompanyFragmentViewModel extends ViewModel {
    private final Repository repository;
    private LiveData<List<Company>> companies;
    private Company company;

    AddCompanyFragmentViewModel(Repository repository) {
        this.repository = repository;
        companies = repository.queryAllCompany();
    }

    LiveData<List<Company>> getCompanies(){
        return companies;
    }

    void insertCompany(Company company){
        repository.insertCompany(company);
    }

    public void updateCompany(Company company){
        repository.updateCompany(company);
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    LiveData<Company> queryCompany(long idCompany){
        return repository.queryCompany(idCompany);
    }
}
