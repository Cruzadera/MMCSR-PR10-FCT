package es.iessaladillo.maria.mmcsr_pr10_fct.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.Repository;

public class AddCompanyFragmentViewModelFactory implements ViewModelProvider.Factory{
    private final Application application;
    private final Repository repository;

    AddCompanyFragmentViewModelFactory(@NonNull Application application, @NonNull Repository repository) {
        this.application = application;
        this.repository = repository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AddCompanyFragmentViewModel.class)) {
            return (T) new AddCompanyFragmentViewModel(application, repository);
        } else {
            throw new IllegalArgumentException("Wrong viewModel class");
        }
    }

}
