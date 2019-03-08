package es.iessaladillo.maria.mmcsr_pr10_fct.ui.add_visit;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Objects;

import es.iessaladillo.maria.mmcsr_pr10_fct.R;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.RepositoryImpl;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.AppDatabase;
import es.iessaladillo.maria.mmcsr_pr10_fct.databinding.AddVisitFragmentBinding;
import es.iessaladillo.maria.mmcsr_pr10_fct.utils.Field;
import es.iessaladillo.maria.mmcsr_pr10_fct.utils.TextWatcherUtils;
import es.iessaladillo.maria.mmcsr_pr10_fct.utils.ValidationUtils;

public class AddVisitFragment extends Fragment {

    private AddVisitViewModel viewModel;
    private AddVisitFragmentBinding b;
    private long visitId;
    private boolean edit;

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
        Objects.requireNonNull(getArguments());
        visitId = getArguments().getInt("visitId");
        edit = getArguments().getBoolean("edit");
        setupViews();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Conflicts navigation lib with de validation form
        if (item.getItemId() == R.id.mnuSave) {
            save();
        } else {
            requireActivity().onBackPressed();
        }
        return true;
    }

    private void save() {

    }

    private void setupViews() {
        b.tilDate.requestFocus();
        b.txtDate.setOnClickListener(l->setupDatePicker());
        b.txtStartTime.setOnClickListener(l -> setupTimePicker(b.txtStartTime));
        b.txtEndTime.setOnClickListener(l -> setupTimePicker(b.txtEndTime));
        TextWatcherUtils.setAfterTextChangeListener(b.txtDate, s -> checkField(b.tilDate, b.txtDate, Field.COMMON));
        TextWatcherUtils.setAfterTextChangeListener(b.txtEndTime, s -> checkField(b.tilEndTime, b.txtEndTime, Field.COMMON));
        TextWatcherUtils.setAfterTextChangeListener(b.txtStartTime, s -> checkField(b.tilStartTime, b.txtStartTime, Field.COMMON));
        TextWatcherUtils.setAfterTextChangeListener(b.txtObservations, s -> checkField(b.tilObservations, b.txtObservations, Field.COMMON));
    }

    private boolean checkField(TextInputLayout til, TextInputEditText txt, Field field) {
        boolean isValid;
        if(field == Field.COMMON && !ValidationUtils.isEmptyText(txt.getText().toString())) {
            txt.setError(getString(R.string.main_invalid_data));
            isValid = false;
        }else{
            til.setEnabled(true);
            isValid = true;
        }
        return isValid;
    }


    private void setupDatePicker() {
        Calendar calendario = Calendar.getInstance();
        int yy = calendario.get(Calendar.YEAR);
        int mm = calendario.get(Calendar.MONTH);
        int dd = calendario.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePicker = new DatePickerDialog(requireActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                String date = String.valueOf(dayOfMonth) +"/"+String.valueOf(monthOfYear)
                        +"/"+ String.valueOf(year);
                b.txtDate.setText(date);

            }
        }, yy, mm, dd);

        datePicker.show();
    }

    private void setupTimePicker(TextInputEditText txt){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                txt.setText(String.format ("%02d:%02d", selectedHour, selectedMinute));
            }
        }, hour, minute, true);
        mTimePicker.show();
    }

}

