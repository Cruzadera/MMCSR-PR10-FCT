package es.iessaladillo.maria.mmcsr_pr10_fct.ui.add_students;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Objects;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import es.iessaladillo.maria.mmcsr_pr10_fct.R;
import es.iessaladillo.maria.mmcsr_pr10_fct.base.EventObserver;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.RepositoryImpl;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.AppDatabase;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Student;
import es.iessaladillo.maria.mmcsr_pr10_fct.databinding.FragmentAddStudentBinding;
import es.iessaladillo.maria.mmcsr_pr10_fct.utils.Field;
import es.iessaladillo.maria.mmcsr_pr10_fct.utils.KeyboardUtils;
import es.iessaladillo.maria.mmcsr_pr10_fct.utils.SnackbarUtils;
import es.iessaladillo.maria.mmcsr_pr10_fct.utils.TextWatcherUtils;
import es.iessaladillo.maria.mmcsr_pr10_fct.utils.ValidationUtils;

public class AddStudentFragment extends Fragment {

    private AddStudentViewModel viewModel;
    private FragmentAddStudentBinding b;
    private NavController navController;
    private int studentId;
    private boolean edit;

    public static AddStudentFragment newInstance() {
        return new AddStudentFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_add_student, container, false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppDatabase appDatabase = AppDatabase.getInstance(requireContext().getApplicationContext());
        viewModel = ViewModelProviders.of(this, new AddStudentFragmentViewModelFactory(requireActivity().getApplication(),
                new RepositoryImpl(appDatabase.companyDao(), appDatabase.studentDao(), appDatabase.visitDao()))).get(AddStudentViewModel.class);
        Objects.requireNonNull(getArguments());
        studentId = getArguments().getInt("studentId");
        edit = getArguments().getBoolean("edit");
        navController = NavHostFragment.findNavController(this);
        if(edit){
            viewModel.getStudent(studentId).observe(getViewLifecycleOwner(), this::dataEditStudent);
        }
        setupViews();
        observeSuccessMessage();
        observeErrorMessage();
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
        Snackbar.make(b.tilTutorName, message, Snackbar.LENGTH_SHORT).addCallback(
                new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        requireActivity().onBackPressed();
                    }
                }).show();
    }

    private void showMessage(String message) {
        SnackbarUtils.snackbar(b.tilTutorName, message);
    }

    private void dataEditStudent(Student student) {
        int positionSpinnerGrade = b.spinnerGrade.getSelectedItemPosition();
        int positionSpinnerCompany = b.spinnerCompanies.getSelectedItemPosition();

        b.txtNameStudent.setText(student.getName());
        b.txtPhoneStudent.setText(student.getPhone());
        b.spinnerGrade.setSelection(positionSpinnerGrade);
        b.txtEmailStudent.setText(student.getEmail());
        b.spinnerCompanies.setSelection(positionSpinnerCompany);
        b.txtTutorPhone.setText(student.getLaborTutorPhone());
        b.txtTutorName.setText(student.getLaborTutorName());
        b.txtSchedule01.setText(student.getSchedule().substring(0, 5));
        b.txtSchedule02.setText(student.getSchedule().substring(7));
    }

    private void setupViews() {
        setupToolbar();
        setupSpinner();

        TextWatcherUtils.setAfterTextChangeListener(b.txtNameStudent, s -> checkField(b.tilNameStudent, b.txtNameStudent, Field.COMMON));
        TextWatcherUtils.setAfterTextChangeListener(b.txtPhoneStudent, s -> checkField(b.tilPhoneStudent, b.txtPhoneStudent, Field.PHONENUMBER));
        TextWatcherUtils.setAfterTextChangeListener(b.txtEmailStudent, s -> checkField(b.tilEmailStudent, b.txtEmailStudent, Field.EMAIL));
        TextWatcherUtils.setAfterTextChangeListener(b.txtTutorName, s -> checkField(b.tilTutorName, b.txtTutorName, Field.COMMON));
        TextWatcherUtils.setAfterTextChangeListener(b.txtTutorPhone, s -> checkField(b.tilTutorPhone, b.txtTutorPhone, Field.PHONENUMBER));
        TextWatcherUtils.setAfterTextChangeListener(b.txtSchedule01, s -> checkField(b.tilSchedule1, b.txtSchedule01, Field.HOUR));
        TextWatcherUtils.setAfterTextChangeListener(b.txtSchedule02, s -> checkField(b.tilSchedule2, b.txtSchedule02, Field.HOUR));
        b.txtSchedule02.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                save();
                return true;
            }
            return false;
        });
    }

    private void setupSpinner() {
        viewModel.getNamesCompaniesLiveData().observe(this, this::getSpinnerData);
    }

    private void getSpinnerData(List<String> stringList) {
        if (stringList.size() > 0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    stringList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            b.spinnerCompanies.setAdapter(adapter);
        }else{
            SnackbarUtils.snackbar(b.tilTutorName, getString(R.string.msg_no_companies));
            navController.navigate(R.id.studentFragment);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private Student getDataStudent() {
        Student student = new Student();
        student.setIdStudent(studentId);
        student.setName(b.txtNameStudent.getText().toString());
        student.setPhone(b.txtPhoneStudent.getText().toString());
        student.setEmail(b.txtEmailStudent.getText().toString());
        student.setGrade(b.spinnerGrade.getSelectedItem().toString());
        student.setNameCompany(b.spinnerCompanies.getSelectedItem().toString());
        student.setLaborTutorName(b.txtTutorName.getText().toString());
        student.setLaborTutorPhone(b.txtTutorPhone.getText().toString());
        student.setSchedule(String.format("%s||%s", b.txtSchedule01.getText().toString(), b.txtSchedule02.getText().toString()));
        return student;
    }

    private void setupToolbar() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(b.toolbar);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_company_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Conflicts navigation lib with de validation form
        if(item.getItemId() == R.id.mnuSave){
            save();
        }else{
            requireActivity().onBackPressed();
        }
        return true;
    }

    private void save() {
        if (isValidForm()) {
            KeyboardUtils.hideSoftKeyboard(requireActivity());
            Student student = getDataStudent();
            if (!edit) {
                viewModel.insertStudent(student);
            }else{
                viewModel.updateStudent(student);
            }
            navController.navigate(R.id.studentFragment);
        } else {
            KeyboardUtils.hideSoftKeyboard(requireActivity());
            checkField(b.tilNameStudent, b.txtNameStudent, Field.COMMON);
            checkField(b.tilPhoneStudent, b.txtPhoneStudent, Field.PHONENUMBER);
            checkField(b.tilTutorName, b.txtTutorName, Field.COMMON);
            checkField(b.tilEmailStudent, b.txtEmailStudent, Field.EMAIL);
            checkField(b.tilTutorPhone, b.txtTutorPhone, Field.PHONENUMBER);
            checkField(b.tilSchedule1, b.txtSchedule01, Field.HOUR);
            checkField(b.tilSchedule2, b.txtSchedule02, Field.HOUR);
        }

    }

    private boolean isValidForm() {
        return checkField(b.tilNameStudent, b.txtNameStudent, Field.COMMON) &&
                checkField(b.tilPhoneStudent, b.txtPhoneStudent, Field.PHONENUMBER) &&
                checkField(b.tilEmailStudent, b.txtEmailStudent, Field.EMAIL) &&
                checkField(b.tilTutorName, b.txtTutorName, Field.COMMON) &&
                checkField(b.tilTutorPhone, b.txtTutorPhone, Field.PHONENUMBER) &&
                checkField(b.tilSchedule1, b.txtSchedule01, Field.HOUR) &&
                checkField(b.tilSchedule2, b.txtSchedule02, Field.HOUR);
    }

    @SuppressLint("NewApi")
    private boolean checkField(TextInputLayout label, TextInputEditText txt, Field field){
        boolean isValid;
        if (field == Field.EMAIL && !ValidationUtils.isValidEmail(txt.getText().toString())) {
            isValid = false;

        } else if (field == Field.PHONENUMBER && !ValidationUtils.isValidSpanishPhoneNumber(txt.getText().toString())) {
            isValid = false;

        } else if (field == Field.HOUR && !ValidationUtils.isValidHour(txt.getText().toString())) {
            isValid = false;

        } else if (field == Field.COMMON && !ValidationUtils.isEmptyText(txt.getText().toString())) {
            isValid = false;

        } else {
            label.setEnabled(true);
            isValid = true;
        }
        return isValid;
    }

}
