package es.iessaladillo.maria.mmcsr_pr10_fct.ui.companies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.iessaladillo.maria.mmcsr_pr10_fct.R;
import es.iessaladillo.maria.mmcsr_pr10_fct.base.EventObserver;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.RepositoryImpl;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.AppDatabase;
import es.iessaladillo.maria.mmcsr_pr10_fct.databinding.FragmentCompanyBinding;
import es.iessaladillo.maria.mmcsr_pr10_fct.utils.SnackbarUtils;

public class CompanyFragment extends Fragment {

    private CompanyFragmentViewModel viewModel;
    private FragmentCompanyBinding b;
    private CompanyFragmentAdapter listAdapter;
    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_company, container, false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppDatabase appDatabase = AppDatabase.getInstance(requireContext().getApplicationContext());
        viewModel = ViewModelProviders.of(this, new CompanyFragmentViewModelFactory(requireActivity().getApplication(),
                new RepositoryImpl(appDatabase.companyDao(), appDatabase.studentDao(), appDatabase.visitDao()))).get(CompanyFragmentViewModel.class);
        navController = NavHostFragment.findNavController(this);
        setupViews();
        observeCompanies();
        observeSuccessMessage();
        observeErrorMessage();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.company_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void observeCompanies() {
        viewModel.getCompanies().observe(this, companies -> {
            listAdapter.submitList(companies);
            b.lblEmptyCompany.setVisibility(companies.size() == 0 ? View.VISIBLE : View.INVISIBLE);
        });
    }

    private void observeSuccessMessage() {
        viewModel.getSuccessMessage().observe(getViewLifecycleOwner(),
                new EventObserver<>(this::showMessage));
    }

    private void observeErrorMessage() {
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(),
                new EventObserver<>(this::showMessage));
    }

    private void showMessage(String message) {
        SnackbarUtils.snackbar(b.lblEmptyCompany, message);
    }

    private void setupViews() {
        b.lblEmptyCompany.setOnClickListener(view -> navigateToAddCompany(getResources().getInteger(R.integer.defaultValorAdd)));
        setupToolbar();
        setupRecyclerView();
    }

    private void setupToolbar() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(b.toolbar);
    }

    private void navigateToAddCompany(int currentCompanyId) {
        Bundle arguments = new Bundle();
        if (currentCompanyId != 0) {
            arguments.putBoolean("edit", true);
        }
        arguments.putInt("companyId", currentCompanyId);
        navController.navigate(R.id.action_companyFragment_to_addCompanyFragment, arguments);
    }


    private void setupRecyclerView() {
        listAdapter = new CompanyFragmentAdapter();
        listAdapter.setOnEditableListener((position) -> navigateToAddCompany((int) listAdapter.getItemId(position)));
        b.lstCompany.setHasFixedSize(true);
        b.lstCompany.setLayoutManager(new GridLayoutManager(getActivity(),
                getResources().getInteger(R.integer.lst_columns)));
        b.lstCompany.addItemDecoration(new DividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL));
        b.lstCompany.setItemAnimator(new DefaultItemAnimator());
        b.lstCompany.setAdapter(listAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(
                        0,
                        ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        // Se elimina el elemento.
                        viewModel.deleteCompany(listAdapter.getItem(viewHolder.getAdapterPosition()));
                    }
                });
        // Se enlaza con el RecyclerView.
        itemTouchHelper.attachToRecyclerView(b.lstCompany);
    }


}
