package es.iessaladillo.maria.mmcsr_pr10_fct.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import es.iessaladillo.maria.mmcsr_pr10_fct.R;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.RepositoryImpl;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.AppDatabase;
import es.iessaladillo.maria.mmcsr_pr10_fct.databinding.FragmentCompanyBinding;

public class VisitFragment extends Fragment {

    private VisitFragmentViewModel viewModel;
    private FragmentCompanyBinding b;
    private NavController navController;

    public static VisitFragment newInstance() {
        return new VisitFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_visit, container, false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppDatabase appDatabase = AppDatabase.getInstance(requireContext().getApplicationContext());
        viewModel = ViewModelProviders.of(this, new VisitFragmentViewModelFactory(requireActivity().getApplication(),
                new RepositoryImpl(appDatabase.companyDao(), appDatabase.studentDao(), appDatabase.visitDao()))).get(VisitFragmentViewModel.class);
        navController = NavHostFragment.findNavController(this);
        setupViews();
    }

    private void setupViews(){
        b.lblEmptyCompany.setOnClickListener(l->navigateToAddVisitFragment(R.integer.defaultValorAdd));
        setupToolbar();
    }

    private void navigateToAddVisitFragment(int currentVisitId) {
        Bundle arguments = new Bundle();
        if (currentVisitId != 0) {
            arguments.putBoolean("edit", true);
        }
        arguments.putInt("visitId", currentVisitId);
        navController.navigate(R.id.action_visitFragment_to_addVisitFragment, arguments);
    }

    private void setupToolbar() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(b.toolbar);
    }
}
