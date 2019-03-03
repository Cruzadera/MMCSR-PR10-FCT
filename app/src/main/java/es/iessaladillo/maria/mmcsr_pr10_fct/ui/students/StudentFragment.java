package es.iessaladillo.maria.mmcsr_pr10_fct.ui.students;

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
import es.iessaladillo.maria.mmcsr_pr10_fct.databinding.FragmentStudentBinding;
import es.iessaladillo.maria.mmcsr_pr10_fct.ui.companies.CompanyFragmentAdapter;
import es.iessaladillo.maria.mmcsr_pr10_fct.utils.SnackbarUtils;

public class StudentFragment extends Fragment {

    private StudentFragmentViewModel viewModel;
    private FragmentStudentBinding b;
    private NavController navController;
    private StudentFragmentAdapter listAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_student, container, false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppDatabase appDatabase = AppDatabase.getInstance(requireContext().getApplicationContext());
        viewModel = ViewModelProviders.of(this, new StudentFragmentViewModelFactory(requireActivity().getApplication(),
                new RepositoryImpl(appDatabase.companyDao(), appDatabase.studentDao()))).get(StudentFragmentViewModel.class);
        navController = NavHostFragment.findNavController(this);
        setupViews();
        observeStudents();
        observeErrorMessage();
        observeSuccessMessage();
    }

    private void setupViews() {
        b.lblEmptyStudents.setOnClickListener(l -> navigateToAddStudent(0));
        setupToolbar();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        listAdapter = new StudentFragmentAdapter();
        listAdapter.setOnEditableListener((position) -> navigateToAddStudent((int) listAdapter.getItemId(position)));
        b.lstStudent.setHasFixedSize(true);
        b.lstStudent.setLayoutManager(new GridLayoutManager(getActivity(),
                getResources().getInteger(R.integer.company_lstCompanies_columns)));
        b.lstStudent.addItemDecoration(new DividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL));
        b.lstStudent.setItemAnimator(new DefaultItemAnimator());
        b.lstStudent.setAdapter(listAdapter);
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
        itemTouchHelper.attachToRecyclerView(b.lstStudent);
    }

    private void observeStudents() {
        viewModel.getStudents().observe(this, students -> {
            listAdapter.submitList(students);
            b.lblEmptyStudents.setVisibility(students.size() == 0 ? View.VISIBLE : View.INVISIBLE);
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
        SnackbarUtils.snackbar(b.lblEmptyStudents, message);
    }

    private void navigateToAddStudent(int currentStudentId) {
        Bundle arguments = new Bundle();
        if (currentStudentId != 0) {
            arguments.putBoolean("edit", true);
        }
        arguments.putInt("studentId", currentStudentId);
        navController.navigate(R.id.action_studentFragment_to_addStudentFragment, arguments);
    }

    private void setupToolbar() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(b.toolbar);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.student_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
