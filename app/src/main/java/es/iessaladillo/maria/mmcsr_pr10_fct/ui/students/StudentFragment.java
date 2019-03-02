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
import es.iessaladillo.maria.mmcsr_pr10_fct.R;
import es.iessaladillo.maria.mmcsr_pr10_fct.databinding.FragmentStudentBinding;

public class StudentFragment extends Fragment {

    private StudentFragmentViewModel viewModel;
    private FragmentStudentBinding b;
    private NavController navController;


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
//        viewModel = ViewModelProviders.of(this).get(StudentFragmentViewModel.class);
        navController = NavHostFragment.findNavController(this);
        setupViews();
    }

    private void setupViews() {

        setupToolbar();
    }

    private void setupToolbar() {
        b.lblEmptyStudents.setOnClickListener(l -> navController.navigate(R.id.action_studentFragment_to_addStudentFragment));
        ((AppCompatActivity) requireActivity()).setSupportActionBar(b.toolbar);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.student_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
