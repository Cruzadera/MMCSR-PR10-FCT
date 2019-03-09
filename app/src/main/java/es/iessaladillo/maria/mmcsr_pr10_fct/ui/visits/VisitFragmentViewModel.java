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
    private LiveData<Student> studentLiveData;
    private boolean studentsAvailable;
    private List<Visit> visitsList;
    private final LiveData<List<Visit>> visits;
    private final LiveData<List<Student>> students;
    private final MutableLiveData<Visit> insertTrigger = new MutableLiveData<>();
    private final LiveData<Resource<Long>> insertResult;
    private LiveData<Visit> visitLiveData;
    private Visit visit;
    private int days;
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
        successMessage.addSource(insertResult, resource -> {
            if (resource.hasSuccess() && resource.getData() >= 0) {
                successMessage.setValue(new Event<>(application.getString(R.string.company_inserted_successfully)));
            }
        });
    }

    private void setupErrorMessage() {
        errorMessage.addSource(deletionResult, resource -> {
            if (resource.hasError()) {
                errorMessage.setValue(new Event<>(application.getString(R.string.list_error_deleting)));
            }
        });
        errorMessage.addSource(insertResult, resource -> {
            if (resource.hasError()) {
                errorMessage.setValue(new Event<>(resource.getException().getMessage()));
            } else if (resource.hasSuccess() && resource.getData() <= 0) {
                errorMessage.setValue(new Event<>(application.getString(R.string.company_error_inserting)));
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

    void deleteVisit(Visit visit) {
        deleteTrigger.setValue(visit);
    }

    LiveData<Visit> queryVisitByStudentId(String nameStudent) {
        if (visitLiveData == null) {
            visitLiveData = repository.queryVisitByIdStudent(nameStudent);
        }
        return visitLiveData;
    }

    LiveData<Student> queryStudentByName(String nameStudent){
        if(studentLiveData == null){
            studentLiveData = repository.queryStudentByName(nameStudent);
        }
        return studentLiveData;
    }

    LiveData<List<Student>> getStudents() {
        return students;
    }

    boolean getStudentsAvailable() {
        return studentsAvailable;
    }

    void setStudentsAvailable(boolean studentsAvailable) {
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

    public List<Visit> getVisitsList() {
        return visitsList;
    }

    void setVisitsList(List<Visit> visitsList) {
        this.visitsList = visitsList;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}
