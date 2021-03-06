package es.iessaladillo.maria.mmcsr_pr10_fct.ui.add_company;

import android.app.Application;

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

class AddCompanyFragmentViewModel extends ViewModel {
    private final Application application;
    private final Repository repository;
    private LiveData<Company> companyLiveData;
    private final MutableLiveData<Company> insertTrigger = new MutableLiveData<>();
    private final MutableLiveData<Company> updateTrigger = new MutableLiveData<>();
    private final MediatorLiveData<Event<String>> successMessage = new MediatorLiveData<>();
    private final MediatorLiveData<Event<String>> errorMessage = new MediatorLiveData<>();
    private final LiveData<Resource<Long>> insertResult;
    private final LiveData<Resource<Integer>> updateResult;

    AddCompanyFragmentViewModel(Application application, Repository repository) {
        this.application = application;
        this.repository = repository;
        insertResult = Transformations.switchMap(insertTrigger, repository::insertCompany);
        updateResult = Transformations.switchMap(updateTrigger, repository::updateCompany);
        setupSuccessMessage();
        setupErrorMessage();
    }

    private void setupSuccessMessage() {
        successMessage.addSource(insertResult, resource -> {
            if (resource.hasSuccess() && resource.getData() >= 0) {
                successMessage.setValue(new Event<>(application.getString(R.string.company_inserted_successfully)));
            }
        });
        successMessage.addSource(updateResult, resource -> {
            if (resource.hasSuccess() && resource.getData() > 0) {
                successMessage.setValue(new Event<>(application.getString(R.string.company_updated_successfully)));
            }
        });
    }

    private void setupErrorMessage() {
        errorMessage.addSource(insertResult, resource -> {
            if (resource.hasError()) {
                errorMessage.setValue(new Event<>(resource.getException().getMessage()));
            } else if (resource.hasSuccess() && resource.getData() <= 0) {
                errorMessage.setValue(new Event<>(application.getString(R.string.company_error_inserting)));
            }
        });
        errorMessage.addSource(updateResult, resource -> {
            if (resource.hasError()) {
                errorMessage.setValue(new Event<>(resource.getException().getMessage()));
            } else if (resource.hasSuccess() && resource.getData() <= 0) {
                errorMessage.setValue(new Event<>(application.getString(R.string.company_error_updating)));
            }
        });
    }

    LiveData<Company> getCompany(long companyId) {
        if (companyLiveData == null) {
            companyLiveData = repository.queryCompany(companyId);
        }
        return companyLiveData;
    }

    LiveData<Event<String>> getSuccessMessage() {
        return successMessage;
    }

    LiveData<Event<String>> getErrorMessage() {
        return errorMessage;
    }

    void insertCompany(Company company) {
        insertTrigger.setValue(company);
    }

    void updateCompany(Company company) {
        updateTrigger.setValue(company);
    }
}
