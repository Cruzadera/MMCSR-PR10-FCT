package es.iessaladillo.maria.mmcsr_pr10_fct.ui.companies;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import es.iessaladillo.maria.mmcsr_pr10_fct.R;
import es.iessaladillo.maria.mmcsr_pr10_fct.base.Event;
import es.iessaladillo.maria.mmcsr_pr10_fct.base.Resource;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.Repository;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Company;

public class CompanyFragmentViewModel extends ViewModel {

    private final Application application;
    private final LiveData<List<Company>> companies;
    private final MutableLiveData<Company> deleteTrigger = new MutableLiveData<>();
    private final LiveData<Resource<Integer>> deletionResult;
    private final MediatorLiveData<Event<String>> successMessage = new MediatorLiveData<>();
    private final MediatorLiveData<Event<String>> errorMessage = new MediatorLiveData<>();

    CompanyFragmentViewModel(Application application, Repository repository) {
        this.application = application;
        companies = repository.queryAllCompany();
        deletionResult = Transformations.switchMap(deleteTrigger, repository::deleteCompany);
        setupSuccessMessage();
        setupErrorMessage();
    }

    private void setupSuccessMessage() {
        successMessage.addSource(deletionResult, resource -> {
            if (resource.hasSuccess()) {
                successMessage.setValue(new Event<>(application.getString(R.string.list_deleted_successfully)));
            }
        });
    }

    private void setupErrorMessage() {
        errorMessage.addSource(deletionResult, resource -> {
            if (resource.hasError()) {
                errorMessage.setValue(new Event<>(application.getString(R.string.list_error_deleting)));
            }
        });
    }

    LiveData<List<Company>> getCompanies() {
        return companies;
    }

    LiveData<Event<String>> getSuccessMessage() {
        return successMessage;
    }

    LiveData<Event<String>> getErrorMessage() {
        return errorMessage;
    }

    void deleteCompany(Company company) {
        deleteTrigger.setValue(company);
    }
}
