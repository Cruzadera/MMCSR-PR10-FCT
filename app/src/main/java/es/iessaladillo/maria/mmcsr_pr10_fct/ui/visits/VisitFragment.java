package es.iessaladillo.maria.mmcsr_pr10_fct.ui.visits;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.PreferenceManager;
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
import es.iessaladillo.maria.mmcsr_pr10_fct.data.local.model.Visit;
import es.iessaladillo.maria.mmcsr_pr10_fct.databinding.FragmentVisitBinding;
import es.iessaladillo.maria.mmcsr_pr10_fct.utils.SnackbarUtils;

public class VisitFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    private VisitFragmentViewModel viewModel;
    private FragmentVisitBinding b;
    private NavController navController;
    private VisitFragmentAdapter listAdapter;
    private int position;
    private SharedPreferences sharedPreferences;

    public static VisitFragment newInstance() {
        return new VisitFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_visit, container, false);
        return b.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //I need to check if there are companies in the database
        if (item.getItemId() == R.id.mnuRefresh) {
            observeStudents();
            observeVisits();
            observeErrorMessage();
            observeSuccessMessage();
        } else {
            requireActivity().onBackPressed();
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        observeStudents();
        observeVisits();
        observeNextVisits();
        observeErrorMessage();
        observeSuccessMessage();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        AppDatabase appDatabase = AppDatabase.getInstance(requireContext().getApplicationContext());
        viewModel = ViewModelProviders.of(this, new VisitFragmentViewModelFactory(requireActivity().getApplication(),
                new RepositoryImpl(appDatabase.companyDao(), appDatabase.studentDao(), appDatabase.visitDao()))).get(VisitFragmentViewModel.class);
        navController = NavHostFragment.findNavController(this);
        setupViews();
        observeStudents();
        observeVisits();
        observeNextVisits();
        observeErrorMessage();
        observeSuccessMessage();
    }


    private void observeStudents() {
        viewModel.getStudents().observe(getViewLifecycleOwner(), students -> {
            for(int i = 0; i<students.size(); i++){
                Visit visit = new Visit(students.get(i).getIdStudent(), "", "", "", "", students.get(i).getName(), "");
                viewModel.insertVisit(visit);
            }
        });
    }

    private void observeStudent(){
        viewModel.getStudents().observe(getViewLifecycleOwner(), students -> {
            if(students.size() == 0){
                viewModel.setStudentsAvailable(false);
            }else{
                viewModel.setStudentsAvailable(true);
            }
        });
    }

    private void observeNextVisits(){
        viewModel.getVisits().observe(this, visits -> {
            for(int i= 0; i< visits.size(); i++){
                if(!visits.get(i).getStartTime().isEmpty()){
                    if (!checkDate(visits.get(i).getNextVisit())) {
                        String date = calculateNextDate(visits.get(i));
                        visits.get(i).setNextVisit(date);
                    }else{
                        visits.get(i).setNextVisit(getString(R.string.msg_as_long_as_possible));
                    }
                }
            }
        });
    }

    private boolean checkDate(String nextVisit) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        String currenDate = format.format(cal.getTime());
        return currenDate.equals(nextVisit);
    }

    @SuppressLint("SimpleDateFormat")
    private String calculateNextDate(Visit visit) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formato = new SimpleDateFormat("d/M/yyyy");
        Date date = null;
        try {
            date = formato.parse(visit.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, getResources().getInteger(R.integer.defaultDays));

        String newDate = formato.format(cal.getTime());
        return newDate;
    }


    private void observeVisits() {
        viewModel.getVisits().observe(this, visits -> {
            viewModel.setVisitsList(visits);
            listAdapter.submitList(visits);
            b.lblEmptyVisits.setVisibility(visits.size() == 0 ? View.VISIBLE : View.INVISIBLE);
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
        SnackbarUtils.snackbar(b.lblEmptyVisits, message);
    }

    private void setupRecyclerView() {
        listAdapter = new VisitFragmentAdapter();
        listAdapter.setOnEditableListener((position) -> navigateToAddVisitFragment((int) listAdapter.getItemId(position)));
        b.lstVisits.setHasFixedSize(true);
        b.lstVisits.setLayoutManager(new GridLayoutManager(getActivity(),
                getResources().getInteger(R.integer.lst_columns)));
        b.lstVisits.addItemDecoration(new DividerItemDecoration(requireActivity(), LinearLayoutManager.VERTICAL));
        b.lstVisits.setItemAnimator(new DefaultItemAnimator());
        b.lstVisits.setAdapter(listAdapter);
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
                        viewModel.deleteVisit(listAdapter.getItem(viewHolder.getAdapterPosition()));
                    }
                });
        // Se enlaza con el RecyclerView.
        itemTouchHelper.attachToRecyclerView(b.lstVisits);
    }

    private void setupViews() {
        b.lblEmptyVisits.setOnClickListener(l -> navigateToAddVisitFragment(getResources().getInteger(R.integer.defaultValorAdd)));
        setupToolbar();
        setupRecyclerView();
    }

    private void navigateToAddVisitFragment(int currentVisitId) {
        if (viewModel.getStudentsAvailable()) {
            Bundle arguments = new Bundle();
            if (currentVisitId != 0) {
                arguments.putBoolean("edit", true);
            }
            arguments.putInt("visitId", currentVisitId);
            navController.navigate(R.id.action_visitFragment_to_addVisitFragment, arguments);
        }else{
            Toast.makeText(requireContext(), getString(R.string.msg_no_students), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupToolbar() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(b.toolbar);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        viewModel.setDays(sharedPreferences.getInt(getString(R.string.prefDays_key),getResources().getInteger(R.integer.defaultDays)));
    }

    @Override
    public void onPause() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
}
