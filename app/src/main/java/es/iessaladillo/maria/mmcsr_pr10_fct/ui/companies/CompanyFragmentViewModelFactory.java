package es.iessaladillo.maria.mmcsr_pr10_fct.ui.companies;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.Repository;

public class CompanyFragmentViewModelFactory implements ViewModelProvider.Factory {

    private final Repository repository;

    CompanyFragmentViewModelFactory(Repository repository) {
        this.repository = repository;
    }
    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CompanyFragmentViewModel(repository);
    }
}
