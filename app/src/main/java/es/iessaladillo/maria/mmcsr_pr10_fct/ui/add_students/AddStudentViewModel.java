package es.iessaladillo.maria.mmcsr_pr10_fct.ui.add_students;

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
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Student;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Visit;

class AddStudentViewModel extends ViewModel {
    private final Application application;
    private final Repository repository;
    private LiveData<Student> studentLiveData;
    private LiveData<String> companyLiveDataByName;
    private LiveData<List<String>> namesCompaniesLiveData;
    private final MutableLiveData<Visit> insertTriggerVisit = new MutableLiveData<>();
    private final MutableLiveData<Student> insertTrigger = new MutableLiveData<>();
    private final MutableLiveData<Student> updateTrigger = new MutableLiveData<>();
    private final MediatorLiveData<Event<String>> successMessage = new MediatorLiveData<>();
    private final MediatorLiveData<Event<String>> errorMessage = new MediatorLiveData<>();
    private final LiveData<Resource<Long>> insertResultVisit;
    private final LiveData<Resource<Long>> insertResult;
    private final LiveData<Resource<Integer>> updateResult;

    AddStudentViewModel(Application application, Repository repository) {
        this.application = application;
        this.repository = repository;
        namesCompaniesLiveData = repository.queryAllCompanyNames();
        insertResultVisit = Transformations.switchMap(insertTriggerVisit, repository::insertVisit);
        insertResult = Transformations.switchMap(insertTrigger, repository::insertStudent);
        updateResult = Transformations.switchMap(updateTrigger, repository::updateStudent);
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

    LiveData<Student> getStudent(long studentId) {
        if (studentLiveData == null) {
            studentLiveData = repository.queryStudent(studentId);
        }
        return studentLiveData;
    }

    LiveData<String> queryCompanyByName(String companyName){
        if(companyLiveDataByName == null){
            companyLiveDataByName = repository.queryAllCompanyByName(companyName);
        }

        return companyLiveDataByName;
    }


    LiveData<Event<String>> getSuccessMessage() {
        return successMessage;
    }

    LiveData<Event<String>> getErrorMessage() {
        return errorMessage;
    }

    void insertStudent(Student student) {
        insertTrigger.setValue(student);
    }

    void insertVisit(Visit visit){
        insertTriggerVisit.setValue(visit);
    }

    void updateStudent(Student student) {
        updateTrigger.setValue(student);
    }

    LiveData<List<String>> getNamesCompaniesLiveData() {
        return namesCompaniesLiveData;
    }
}
