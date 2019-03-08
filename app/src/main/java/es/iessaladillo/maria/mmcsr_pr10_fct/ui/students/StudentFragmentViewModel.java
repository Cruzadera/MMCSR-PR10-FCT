package es.iessaladillo.maria.mmcsr_pr10_fct.ui.students;

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

class StudentFragmentViewModel extends ViewModel {
    private boolean areCompanies;
    private final Application application;
    private final LiveData<List<Student>> students;
    private final LiveData<List<String>> namesCompaniesLiveData;
    private final MutableLiveData<Student> deleteTrigger = new MutableLiveData<>();
    private final LiveData<Resource<Integer>> deletionResult;
    private final MediatorLiveData<Event<String>> successMessage = new MediatorLiveData<>();
    private final MediatorLiveData<Event<String>> errorMessage = new MediatorLiveData<>();

    StudentFragmentViewModel(Application application, Repository repository) {
        this.application = application;
        namesCompaniesLiveData = repository.queryAllCompanyNames();
        students = repository.queryStudents();
        deletionResult = Transformations.switchMap(deleteTrigger, repository::deleteStudent);
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

    LiveData<List<Student>> getStudents() {
        return students;
    }

    LiveData<Event<String>> getSuccessMessage() {
        return successMessage;
    }

    LiveData<Event<String>> getErrorMessage() {
        return errorMessage;
    }

    void deleteStudent(Student student) {
        deleteTrigger.setValue(student);
    }

    LiveData<List<String>> getNamesCompaniesLiveData() {
        return namesCompaniesLiveData;
    }

    public boolean isAreCompanies() {
        return areCompanies;
    }

    public void setAreCompanies(boolean areCompanies) {
        this.areCompanies = areCompanies;
    }
}
