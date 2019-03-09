package es.iessaladillo.maria.mmcsr_pr10_fct.ui.add_company;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

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
import android.view.inputmethod.EditorInfo;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import es.iessaladillo.maria.mmcsr_pr10_fct.R;
import es.iessaladillo.maria.mmcsr_pr10_fct.base.EventObserver;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.RepositoryImpl;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.AppDatabase;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Company;
import es.iessaladillo.maria.mmcsr_pr10_fct.databinding.FragmentAddCompanyBinding;
import es.iessaladillo.maria.mmcsr_pr10_fct.ui.companies.CompanyFragmentViewModel;
import es.iessaladillo.maria.mmcsr_pr10_fct.utils.Field;
import es.iessaladillo.maria.mmcsr_pr10_fct.utils.KeyboardUtils;
import es.iessaladillo.maria.mmcsr_pr10_fct.utils.SnackbarUtils;
import es.iessaladillo.maria.mmcsr_pr10_fct.utils.TextWatcherUtils;
import es.iessaladillo.maria.mmcsr_pr10_fct.utils.ValidationUtils;

public class AddCompanyFragment extends Fragment {

    private AddCompanyFragmentViewModel viewModel;
    private CompanyFragmentViewModel viewModelCompany;
    private FragmentAddCompanyBinding b;
    private NavController navController;
    private long companyId;
    private boolean edit;

    public static AddCompanyFragment newInstance() {
        return new AddCompanyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_add_company, container, false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppDatabase appDatabase = AppDatabase.getInstance(requireContext().getApplicationContext());
        navController = NavHostFragment.findNavController(this);
        viewModel = ViewModelProviders.of(this, new AddCompanyFragmentViewModelFactory(requireActivity().getApplication(),
                        new RepositoryImpl(appDatabase.companyDao(), appDatabase.studentDao(), appDatabase.visitDao()))).get(AddCompanyFragmentViewModel.class);
        //Get company id
        Objects.requireNonNull(getArguments());
        companyId = getArguments().getInt("companyId");
        edit = getArguments().getBoolean("edit");
        setupViews();
        if(edit){
            viewModel.getCompany(companyId).observe(getViewLifecycleOwner(), this::dataEditCompany);
        }
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
        Snackbar.make(b.txtAddress, message, Snackbar.LENGTH_SHORT).addCallback(
                new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        requireActivity().onBackPressed();
                    }
                }).show();
    }

    private void showMessage(String message) {
        SnackbarUtils.snackbar(b.tilCIFLetter, message);
    }


    private void dataEditCompany(Company company) {
        b.txtUrlLogo.setText(company.getUrlLogo());
        b.txtName.setText(company.getName());
        b.txtAddress.setText(company.getAddress());
        b.txtEmail.setText(company.getEmail());
        b.txtPhone.setText(company.getPhone());
        b.txtContactName.setText(company.getNameContactPerson());
        b.txtNumbersCif.setText(company.getCIF().substring(1));
        b.txtCIFLetter.setText(String.format("%c", company.getCIF().charAt(0)));
    }

    private void setupViews() {
        setupToolbar();
        TextWatcherUtils.setAfterTextChangeListener(b.txtUrlLogo, s -> checkField(b.tilUrlLogo, b.txtUrlLogo, Field.URL_LOGO));
        TextWatcherUtils.setAfterTextChangeListener(b.txtName, s -> checkField(b.tilName, b.txtName, Field.COMMON));
        TextWatcherUtils.setAfterTextChangeListener(b.txtAddress, s -> checkField(b.tilAddress, b.txtAddress, Field.COMMON));
        TextWatcherUtils.setAfterTextChangeListener(b.txtPhone, s -> checkField(b.tilPhone, b.txtPhone, Field.PHONENUMBER));
        TextWatcherUtils.setAfterTextChangeListener(b.txtEmail, s -> checkField(b.tilEmail, b.txtEmail, Field.EMAIL));
        TextWatcherUtils.setAfterTextChangeListener(b.txtContactName, s -> checkField(b.tilContactName, b.txtContactName, Field.COMMON));
        //TODO: Hacer la validación del CIF
        TextWatcherUtils.setAfterTextChangeListener(b.txtNumbersCif, s -> checkField(b.tilCIFnumbers, b.txtNumbersCif, Field.COMMON));
        TextWatcherUtils.setAfterTextChangeListener(b.txtCIFLetter, s -> checkField(b.tilCIFLetter, b.txtCIFLetter, Field.COMMON));
        b.txtCIFLetter.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                save();
                return true;
            }
            return false;
        });
    }

    private void changePhoto() {
        Picasso.with(requireContext()).load(b.txtUrlLogo.getText().toString())
                .error(R.mipmap.ic_launcher)
                .resize(200, 200)
                .into(b.imgLogo);
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
        inflater.inflate(R.menu.save_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Conflicts navigation lib with de action in the item menu
        if(item.getItemId() == R.id.mnuSave){
            save();
        }else{
            requireActivity().onBackPressed();
        }
        return true;
    }

    private boolean checkField(TextInputLayout label, TextInputEditText txt, Field field){
        boolean isValid;
        if (field == Field.EMAIL && !ValidationUtils.isValidEmail(txt.getText().toString())) {
            isValid = invalidateField(label, txt, field);

        } else if (field == Field.PHONENUMBER && !ValidationUtils.isValidSpanishPhoneNumber(txt.getText().toString())) {
            isValid = invalidateField(label, txt, field);

        } else if (field == Field.URL_LOGO && !ValidationUtils.isValidUrl(txt.getText().toString())) {
            isValid = invalidateField(label, txt, field);

        } else if (field == Field.COMMON && !ValidationUtils.isEmptyText(txt.getText().toString())) {
            isValid = invalidateField(label, txt, field);

        } else {
            label.setEnabled(true);
            isValid = true;
        }
        return isValid;
    }

    private boolean invalidateField(TextInputLayout label, TextInputEditText txt, Field field) {
        txt.setError(getString(R.string.main_invalid_data));
        return false;
    }

    private boolean isValidForm() {
        boolean isValid;
        isValid = checkField(b.tilName, b.txtName, Field.COMMON) && checkField(b.tilAddress, b.txtAddress, Field.COMMON) && checkField(b.tilPhone, b.txtPhone, Field.PHONENUMBER)
                && checkField(b.tilEmail, b.txtEmail, Field.EMAIL) && checkField(b.tilUrlLogo, b.txtUrlLogo, Field.URL_LOGO) && checkField(b.tilCIFLetter, b.txtCIFLetter, Field.COMMON)
                && checkField(b.tilCIFnumbers, b.txtNumbersCif, Field.COMMON) && checkField(b.tilContactName, b.txtContactName, Field.COMMON);
        return isValid;
    }

    private void save() {
        if (isValidForm()) {
            KeyboardUtils.hideSoftKeyboard(requireActivity());
            SnackbarUtils.snackbar(b.tilCIFLetter, getString(R.string.main_saved_succesfully));
            Company company = getDataCompany();
            if (!edit) {
                viewModel.insertCompany(company);
            }else{
                viewModel.updateCompany(company);
            }
            navController.navigate(R.id.companyFragment);
        } else {
            KeyboardUtils.hideSoftKeyboard(requireActivity());
            SnackbarUtils.snackbar(b.tilCIFLetter, getString(R.string.main_error_saving));
            checkField(b.tilAddress, b.txtAddress, Field.COMMON);
            checkField(b.tilUrlLogo, b.txtUrlLogo, Field.URL_LOGO);
            checkField(b.tilName, b.txtName, Field.COMMON);
            checkField(b.tilEmail, b.txtEmail, Field.EMAIL);
            checkField(b.tilPhone, b.txtPhone, Field.PHONENUMBER);
            checkField(b.tilContactName, b.txtContactName, Field.COMMON);
            checkField(b.tilCIFnumbers, b.txtNumbersCif, Field.COMMON);
            checkField(b.tilCIFLetter, b.txtCIFLetter, Field.COMMON);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private Company getDataCompany() {
        Company company = new Company();
        company.setIdCompany(companyId);
        company.setName(b.txtName.getText().toString());
        company.setEmail(b.txtEmail.getText().toString());
        company.setPhone(b.txtPhone.getText().toString());
        company.setAddress(b.txtAddress.getText().toString());
        company.setCIF(String.format("%s%s", b.txtCIFLetter.getText().toString(), b.txtNumbersCif.getText().toString()));
        company.setNameContactPerson(b.txtContactName.getText().toString());
        if(TextUtils.isEmpty(b.txtUrlLogo.getText().toString())){
            company.setUrlLogo("http://i.imgur.com/DvpvklR.png");
        }else{
            company.setUrlLogo(b.txtUrlLogo.getText().toString());
        }
        return company;
    }
}
