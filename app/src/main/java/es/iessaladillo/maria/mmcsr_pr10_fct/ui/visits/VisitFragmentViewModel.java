package es.iessaladillo.maria.mmcsr_pr10_fct.ui.visits;

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

class VisitFragmentViewModel extends ViewModel {
    private final Application application;
    private final Repository repository;
    private List<Student> studentsAvailable;
    private final LiveData<List<Visit>> visits;
    private final LiveData<List<Student>> students;
    private final MutableLiveData<Visit> insertTrigger = new MutableLiveData<>();
    private final LiveData<Resource<Long>> insertResult;
    private LiveData<Visit> visitLiveData;
    private Visit visit;
    private final MutableLiveData<Visit> deleteTrigger = new MutableLiveData<>();
    private final LiveData<Resource<Integer>> deletionResult;
    private final MediatorLiveData<Event<String>> successMessage = new MediatorLiveData<>();
    private final MediatorLiveData<Event<String>> errorMessage = new MediatorLiveData<>();

    VisitFragmentViewModel(Application application, Repository repository) {
        this.application = application;
        this.repository = repository;
        visits = repository.queryVisits();
        students = repository.queryStudents();
        insertResult = Transformations.switchMap(insertTrigger, repository::insertVisit);
        deletionResult = Transformations.switchMap(deleteTrigger, repository::deleteVisit);
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

    LiveData<List<Visit>> getVisits() {
        return visits;
    }

    LiveData<Event<String>> getSuccessMessage() {
        return successMessage;
    }

    LiveData<Event<String>> getErrorMessage() {
        return errorMessage;
    }

    void deleteStudent(Visit visit) {
        deleteTrigger.setValue(visit);
    }

    LiveData<Visit> queryVisitByStudentId(long studentId) {
        if (visitLiveData == null) {
            visitLiveData = repository.queryVisitByIdStudent(studentId);
        }
        return visitLiveData;
    }

    LiveData<List<Student>> getStudents() {
        return students;
    }

    List<Student> getStudentsAvailable() {
        return studentsAvailable;
    }

    void setStudentsAvailable(List<Student> studentsAvailable) {
        this.studentsAvailable = studentsAvailable;
    }

    void insertVisit(Visit visit) {
        insertTrigger.setValue(visit);
    }

    public Visit getVisit() {
        return visit;
    }

    public void setVisit(Visit visit) {
        this.visit = visit;
    }
}
