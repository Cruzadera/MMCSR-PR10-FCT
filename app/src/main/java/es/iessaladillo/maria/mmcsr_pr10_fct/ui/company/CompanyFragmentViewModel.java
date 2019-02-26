package es.iessaladillo.maria.mmcsr_pr10_fct.ui.company;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.Repository;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Company;

public class CompanyFragmentViewModel extends ViewModel {

    private final Repository repository;
    private LiveData<Company> company;

    public CompanyFragmentViewModel(Repository repository) {
        this.repository = repository;
    }

    public LiveData<Company> getCompany(){
        return company;
    }

    public void insertCompany(Company company){
        repository.insertCompany(company);
    }

    public void updateCompany(Company company){
        repository.updateCompany(company);
    }
}
