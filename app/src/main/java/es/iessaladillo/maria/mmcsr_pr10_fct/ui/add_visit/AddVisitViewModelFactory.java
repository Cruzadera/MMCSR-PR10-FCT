package es.iessaladillo.maria.mmcsr_pr10_fct.ui.add_visit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.Repository;

public class AddVisitViewModelFactory implements ViewModelProvider.Factory{
    private final Application application;
    private final Repository repository;

    AddVisitViewModelFactory(@NonNull Application application, @NonNull Repository repository) {
        this.application = application;
        this.repository = repository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AddVisitViewModel.class)) {
            return (T) new AddVisitViewModel(application, repository);
        } else {
            throw new IllegalArgumentException("Wrong viewModel class");
        }
    }
}
