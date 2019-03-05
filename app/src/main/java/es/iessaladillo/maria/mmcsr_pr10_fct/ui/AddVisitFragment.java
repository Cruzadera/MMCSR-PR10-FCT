package es.iessaladillo.maria.mmcsr_pr10_fct.ui;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.iessaladillo.maria.mmcsr_pr10_fct.R;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.RepositoryImpl;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.AppDatabase;
import es.iessaladillo.maria.mmcsr_pr10_fct.databinding.AddVisitFragmentBinding;

public class AddVisitFragment extends Fragment {

    private AddVisitViewModel viewModel;
    private AddVisitFragmentBinding b;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.add_visit_fragment, container, false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppDatabase appDatabase = AppDatabase.getInstance(requireContext().getApplicationContext());
        viewModel = ViewModelProviders.of(this, new AddVisitViewModelFactory(requireActivity().getApplication(),
                new RepositoryImpl(appDatabase.companyDao(), appDatabase.studentDao(), appDatabase.visitDao()))).get(AddVisitViewModel.class);
    }

    private void setupViews() {
        setupDatePicker();
    }

    private void setupDatePicker() {
//        final Calendar calendario = Calendar.getInstance();
//        int yy = calendario.get(Calendar.YEAR);
//        int mm = calendario.get(Calendar.MONTH);
//        int dd = calendario.get(Calendar.DAY_OF_MONTH);
//
//
//        DatePickerDialog datePicker = new DatePickerDialog(requireActivity(), new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//
//                String fecha = String.valueOf(year) +"-"+String.valueOf(monthOfYear)
//                        +"-"+String.valueOf(dayOfMonth);
//                inifecha.setText(fecha);
//
//            }
//        }, yy, mm, dd);
//
//        datePicker.show();
    }

}
