package es.iessaladillo.maria.mmcsr_pr10_fct.ui.add_visit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import es.iessaladillo.maria.mmcsr_pr10_fct.R;
import es.iessaladillo.maria.mmcsr_pr10_fct.base.EventObserver;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.RepositoryImpl;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.AppDatabase;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Visit;
import es.iessaladillo.maria.mmcsr_pr10_fct.databinding.AddVisitFragmentBinding;
import es.iessaladillo.maria.mmcsr_pr10_fct.utils.Field;
import es.iessaladillo.maria.mmcsr_pr10_fct.utils.KeyboardUtils;
import es.iessaladillo.maria.mmcsr_pr10_fct.utils.SnackbarUtils;
import es.iessaladillo.maria.mmcsr_pr10_fct.utils.TextWatcherUtils;
import es.iessaladillo.maria.mmcsr_pr10_fct.utils.ValidationUtils;

public class AddVisitFragment extends Fragment {

    private AddVisitViewModel viewModel;
    private AddVisitFragmentBinding b;
    private long visitId;
    private NavController navController;
    private SharedPreferences sharedPreferences;

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
        navController = NavHostFragment.findNavController(this);
        Objects.requireNonNull(getArguments());
        visitId = getArguments().getInt("visitId");
        if(visitId !=0){
            viewModel.getVisit(visitId).observe(getViewLifecycleOwner(), this::dataEditVisit);
        }
        setupViews();
        observeErrorMessage();
        observeSuccessMessage();
    }

    private void dataEditVisit(Visit visit) {
        int positionSpinnerStudent = b.spinnerStudents.getSelectedItemPosition();
        b.txtDate.setText(visit.getDate());
        b.txtStartTime.setText(visit.getStartTime());
        b.spinnerStudents.setSelection(positionSpinnerStudent);
        b.txtEndTime.setText(visit.getEndTime());
        b.txtObservations.setText(visit.getObservations());
    }

    private void observeSuccessMessage() {
        viewModel.getSuccessMessage().observe(getViewLifecycleOwner(),
                new EventObserver<>(this::showMessageAndFinish));
    }

    private void observeErrorMessage() {
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(),
                new EventObserver<>(this::showMessage));
    }

    private void showMessageAndFinish(String message) {
        Snackbar.make(b.tilDate, message, Snackbar.LENGTH_SHORT).addCallback(
                new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        requireActivity().onBackPressed();
                    }
                }).show();
    }

    private void showMessage(String message) {
        SnackbarUtils.snackbar(b.tilDate, message);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save_menu, menu);
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
        if (isValidForm()) {
            KeyboardUtils.hideSoftKeyboard(requireActivity());
            Visit visit = getDataVisit();
            if(visitId == 0){
                viewModel.insertVisit(visit);
            }else{
                viewModel.updateVisit(visit);
            }
            navController.navigate(R.id.visitFragment);
        } else {
            KeyboardUtils.hideSoftKeyboard(requireActivity());
            checkField(b.tilDate, b.txtDate, Field.COMMON);
            checkField(b.tilStartTime, b.txtStartTime, Field.COMMON);
            checkField(b.tilEndTime, b.txtEndTime, Field.COMMON);
            checkField(b.tilObservations, b.txtObservations, Field.EMAIL);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private Visit getDataVisit() {
        Visit visit = new Visit();
        visit.setIdVisit(visitId);
        visit.setDate(b.txtDate.getText().toString());
        visit.setStartTime(b.txtStartTime.getText().toString());
        visit.setEndTime(b.txtEndTime.getText().toString());
        visit.setObservations(b.txtObservations.getText().toString());
        visit.setNameStudent(b.spinnerStudents.getSelectedItem().toString());
        visit.setNextVisit("");
        return visit;
    }

    private boolean isValidForm() {
        return checkField(b.tilDate, b.txtDate, Field.COMMON) &&
                checkField(b.tilStartTime, b.txtStartTime, Field.COMMON) &&
                checkField(b.tilObservations, b.txtObservations, Field.COMMON) &&
                checkField(b.tilEndTime, b.txtEndTime, Field.COMMON);
    }

    private void setupViews() {
        setupToolbar();
        setupSpinner();
        b.tilDate.requestFocus();
        b.txtDate.setOnClickListener(l->setupDatePicker());
        b.txtStartTime.setOnClickListener(l -> setupTimePicker(b.txtStartTime));
        b.txtEndTime.setOnClickListener(l -> setupTimePicker(b.txtEndTime));
        TextWatcherUtils.setAfterTextChangeListener(b.txtDate, s -> checkField(b.tilDate, b.txtDate, Field.COMMON));
        TextWatcherUtils.setAfterTextChangeListener(b.txtEndTime, s -> checkField(b.tilEndTime, b.txtEndTime, Field.COMMON));
        TextWatcherUtils.setAfterTextChangeListener(b.txtStartTime, s -> checkField(b.tilStartTime, b.txtStartTime, Field.COMMON));
        TextWatcherUtils.setAfterTextChangeListener(b.txtObservations, s -> checkField(b.tilObservations, b.txtObservations, Field.COMMON));
    }

    private void setupToolbar() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(b.toolbar);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayShowHomeEnabled(true);
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
        int mm = calendario.get(Calendar.MONTH + 1);
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

    private void setupSpinner() {
        viewModel.getNamesStudentsLiveData().observe(this, this::getSpinnerData);
    }

    private void getSpinnerData(List<String> stringList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                stringList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        b.spinnerStudents.setAdapter(adapter);

    }
}

