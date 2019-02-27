package es.iessaladillo.maria.mmcsr_pr10_fct.ui.add_company;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.Repository;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Company;

public class AddCompanyFragmentViewModel extends ViewModel {
    private final Repository repository;
    private LiveData<List<Company>> companies;

    AddCompanyFragmentViewModel(Repository repository) {
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
}
