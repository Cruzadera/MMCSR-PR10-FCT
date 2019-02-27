package es.iessaladillo.maria.mmcsr_pr10_fct.ui.companies;

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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import es.iessaladillo.maria.mmcsr_pr10_fct.R;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.RepositoryImpl;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.AppDatabase;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.CompanyDao;
import es.iessaladillo.maria.mmcsr_pr10_fct.databinding.FragmentCompanyBinding;

public class CompanyFragment extends Fragment {

    private CompanyFragmentViewModel viewModel;
    private FragmentCompanyBinding b;
    private CompanyFragmentAdapter listAdapter;
    private NavController navController;

    public static CompanyFragment newInstance() {
        return new CompanyFragment();
    }

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
        CompanyDao companyDao = AppDatabase.getInstance(getContext()).companyDao();
        RepositoryImpl repository =  new RepositoryImpl(companyDao);
        viewModel = ViewModelProviders.of(this, new CompanyFragmentViewModelFactory(repository)).get(CompanyFragmentViewModel.class);
        navController = NavHostFragment.findNavController(this);
        setupViews();
        observeCompanies();
    }

    private void observeCompanies() {
        viewModel.getCompanies().observe(this, companies -> {
            listAdapter.submitList(companies);
            b.lblEmptyCompany.setVisibility(companies.size() == 0 ? View.VISIBLE : View.INVISIBLE);
        });
    }

    private void setupViews() {
        b.fabAddCompany.setOnClickListener(view -> navigateToAddCompany());
        b.lblEmptyCompany.setOnClickListener(view -> navigateToAddCompany());
        setupToolbar();
        setupRecyclerView();
    }

    private void setupToolbar() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(b.toolbar);
    }

    private void navigateToAddCompany() {
        navController.navigate(R.id.action_companyFragment_to_addCompanyFragment);
    }


    private void setupRecyclerView() {
        listAdapter = new CompanyFragmentAdapter();
//        listAdapter.setOnDeleteListener(position -> deleteUser(listAdapter.getItem(position)));
//        listAdapter.setOnEditableListener((v, position) -> onUserEditListener.onUserEdit(listAdapter.getItem(position)));
        b.lstCompanies.setHasFixedSize(true);
        b.lstCompanies.setLayoutManager(new GridLayoutManager(getActivity(),
                getResources().getInteger(R.integer.company_lstCompanies_columns)));
        b.lstCompanies.addItemDecoration(new DividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL));
        b.lstCompanies.setItemAnimator(new DefaultItemAnimator());
        b.lstCompanies.setAdapter(listAdapter);
    }



}
