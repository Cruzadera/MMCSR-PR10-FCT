package es.iessaladillo.maria.mmcsr_pr10_fct.ui.add_company;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.Repository;

public class AddCompanyFragmentViewModelFactory implements ViewModelProvider.Factory{
    private final Repository repository;

    public AddCompanyFragmentViewModelFactory(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddCompanyFragmentViewModel(repository);
    }
}
