package es.iessaladillo.maria.mmcsr_pr10_fct.ui.students;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.iessaladillo.maria.mmcsr_pr10_fct.R;

public class StudentFragment extends Fragment {

    private StudentFragmentViewModel mViewModel;

    public static StudentFragment newInstance() {
        return new StudentFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(StudentFragmentViewModel.class);
        // TODO: Use the ViewModel
    }

}
