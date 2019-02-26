package es.iessaladillo.maria.mmcsr_pr10_fct.ui.company;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import es.iessaladillo.maria.mmcsr_pr10_fct.R;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.Repository;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Company;
import es.iessaladillo.maria.mmcsr_pr10_fct.databinding.FragmentCompanyBinding;

public class CompanyFragment extends Fragment {

    private CompanyFragmentViewModel viewModel;
    private Repository repository;
    private FragmentCompanyBinding b;

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
        viewModel = ViewModelProviders.of(this, new CompanyFragmentViewModelFactory(repository)).get(CompanyFragmentViewModel.class);
        setupViews();
    }

    private void setupViews() {
//        b.fabAddCompany.setOnClickListener(l -> addCompany(new Company()));
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        CompanyFragmentAdapter listAdapter = new CompanyFragmentAdapter();
//        listAdapter.setOnDeleteListener(position -> deleteUser(listAdapter.getItem(position)));
//        listAdapter.setOnEditableListener((v, position) -> onUserEditListener.onUserEdit(listAdapter.getItem(position)));
        b.lstCompanies.setHasFixedSize(true);
        b.lstCompanies.setLayoutManager(new GridLayoutManager(getActivity(),
                getResources().getInteger(R.integer.company_lstCompanies_columns)));
        b.lstCompanies.setItemAnimator(new DefaultItemAnimator());
        b.lstCompanies.setAdapter(listAdapter);
    }

    private void addCompany(Company company) {
        viewModel.insertCompany(company);
    }




}
