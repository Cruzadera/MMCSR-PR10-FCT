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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import es.iessaladillo.maria.mmcsr_pr10_fct.R;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.RepositoryImpl;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.AppDatabase;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.CompanyDao;
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Company;
import es.iessaladillo.maria.mmcsr_pr10_fct.databinding.FragmentAddCompanyBinding;
import es.iessaladillo.maria.mmcsr_pr10_fct.utils.Field;
import es.iessaladillo.maria.mmcsr_pr10_fct.utils.KeyboardUtils;
import es.iessaladillo.maria.mmcsr_pr10_fct.utils.SnackbarUtils;
import es.iessaladillo.maria.mmcsr_pr10_fct.utils.TextWatcherUtils;
import es.iessaladillo.maria.mmcsr_pr10_fct.utils.ValidationUtils;

public class AddCompanyFragment extends Fragment {

    private AddCompanyFragmentViewModel viewModel;
    private FragmentAddCompanyBinding b;
    private NavController navController;
    private int companyId;

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
        navController = NavHostFragment.findNavController(this);
        CompanyDao companyDao = AppDatabase.getInstance(getContext()).companyDao();
        RepositoryImpl repository =  new RepositoryImpl(companyDao);
        viewModel = ViewModelProviders.of(this, new AddCompanyFragmentViewModelFactory(repository)).get(AddCompanyFragmentViewModel.class);
        //Get company id
        Objects.requireNonNull(getArguments());
        companyId = getArguments().getInt("companyId");
        if(companyId == 0){
            viewModel.setCompany(new Company());
        }
        setupViews();
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
        if (item.getItemId() == R.menu.add_company_fragment) {
            save();
            return true;
        }
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
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
                && checkField(b.tilCIFnumbers, b.txtNumbersCif, Field.COMMON);
        return isValid;
    }

    private void save() {
        if (isValidForm()) {
            KeyboardUtils.hideSoftKeyboard(requireActivity());
            SnackbarUtils.snackbar(b.tilCIFLetter, getString(R.string.main_saved_succesfully));
            getDataCompany();
            viewModel.insertCompany(viewModel.getCompany());
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

    private void getDataCompany() {
        viewModel.getCompany().setName(b.txtName.getText().toString());
        viewModel.getCompany().setEmail(b.txtEmail.getText().toString());
        viewModel.getCompany().setPhone(b.txtPhone.getText().toString());
        viewModel.getCompany().setAddress(b.txtAddress.getText().toString());
        viewModel.getCompany().setCIF(String.format("%s%s", b.txtCIFLetter.getText().toString(), b.txtNumbersCif.getText().toString()));
        if(TextUtils.isEmpty(b.txtUrlLogo.getText().toString())){
            viewModel.getCompany().setUrlLogo("http://i.imgur.com/DvpvklR.png");
        }else{
            viewModel.getCompany().setUrlLogo(b.txtUrlLogo.getText().toString());
        }
    }
}
