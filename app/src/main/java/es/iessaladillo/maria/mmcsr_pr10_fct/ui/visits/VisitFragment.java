package es.iessaladillo.maria.mmcsr_pr10_fct.ui.visits;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.iessaladillo.maria.mmcsr_pr10_fct.R;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.RepositoryImpl;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.AppDatabase;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Visit;
import es.iessaladillo.maria.mmcsr_pr10_fct.databinding.FragmentVisitBinding;
import es.iessaladillo.maria.mmcsr_pr10_fct.ui.students.StudentFragmentAdapter;

public class VisitFragment extends Fragment {

    private VisitFragmentViewModel viewModel;
    private FragmentVisitBinding b;
    private NavController navController;
    private VisitFragmentAdapter listAdapter;
    private long studentId;

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
        observeVisits();
        observeStudents();
        observeVisitsByStudentId();
    }

    private void observeStudents() {
        viewModel.getStudents().observe(this, students -> {
            viewModel.setStudentsAvailable(students);
        });
    }

    private void observeVisitsByStudentId() {
        if (viewModel.getStudentsAvailable() != null) {
            for(int i = 0; i<viewModel.getStudentsAvailable().size(); i++){
                studentId = viewModel.getStudentsAvailable().get(i).getIdStudent();
                LiveData<Visit> visita = viewModel.queryVisitByStudentId(viewModel.getStudentsAvailable().get(i).getIdStudent());
            }
        }
    }

    private void getVisitData(Visit visit) {
        visit.setIdVisit(studentId);
        visit.setStartTime("12:00");
        visit.setDate("2019-12-09");
        visit.setEndTime("14:00");
        visit.setIdStu(studentId);
        visit.setNameStudent("Pepe");
        visit.setObservations("asfdhjadsfj");
        viewModel.setVisit(visit);
        viewModel.insertVisit(visit);
    }

    private void observeVisits() {
        viewModel.getVisits().observe(this, visits -> {
            listAdapter.submitList(visits);
            b.lblEmptyVisits.setVisibility(visits.size() == 0 ? View.VISIBLE : View.INVISIBLE);
        });
    }

    private void setupRecyclerView() {
        listAdapter = new VisitFragmentAdapter();
        //listAdapter.setOnEditableListener((position) -> navigateToAddStudent((int) listAdapter.getItemId(position)));
        b.lstVisits.setHasFixedSize(true);
        b.lstVisits.setLayoutManager(new GridLayoutManager(getActivity(),
                getResources().getInteger(R.integer.company_lstCompanies_columns)));
        b.lstVisits.addItemDecoration(new DividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL));
        b.lstVisits.setItemAnimator(new DefaultItemAnimator());
        b.lstVisits.setAdapter(listAdapter);
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
                        viewModel.deleteStudent(listAdapter.getItem(viewHolder.getAdapterPosition()));
                    }
                });
        // Se enlaza con el RecyclerView.
        itemTouchHelper.attachToRecyclerView(b.lstVisits);
    }

    private void setupViews(){
        b.lblEmptyVisits.setOnClickListener(l->navigateToAddVisitFragment(R.integer.defaultValorAdd));
        setupToolbar();
        setupRecyclerView();
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
