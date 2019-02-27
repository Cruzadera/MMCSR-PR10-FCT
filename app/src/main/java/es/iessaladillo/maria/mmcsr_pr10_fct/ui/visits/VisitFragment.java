package es.iessaladillo.maria.mmcsr_pr10_fct.ui.visits;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.iessaladillo.maria.mmcsr_pr10_fct.R;

public class VisitFragment extends Fragment {

    private VisitFragmentViewModel mViewModel;

    public static VisitFragment newInstance() {
        return new VisitFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_visit, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VisitFragmentViewModel.class);
        // TODO: Use the ViewModel
    }

}
