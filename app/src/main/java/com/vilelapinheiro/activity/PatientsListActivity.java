package com.vilelapinheiro.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import com.vilelapinheiro.PatientListAdapter;
import com.vilelapinheiro.R;
import com.vilelapinheiro.dao.PatientDAO;
import com.vilelapinheiro.model.MedicalPlan;
import com.vilelapinheiro.model.Gender;
import com.vilelapinheiro.model.Patient;

public class PatientsListActivity extends AppCompatActivity {

    public static final String KEY_PATIENT = "patient";
    public static final int CODE_NEW_PATIENT = 101;
    private ListView patientsList;

    private final PatientDAO dao = new PatientDAO();
    private PatientListAdapter adapter;
    private ActionMode actionMode;
    private int selectedPosition = -1;
    private View selectedView;

    private boolean filteredByAgreed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.patients_list);
        setTitle(getString(R.string.patients));


        createHardcodedPatients();

        readFilterSetting();

        configureList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        adapter.clearAndAddAll(dao.findAll(), filteredByAgreed);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_patients_list_top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
/*            case R.id.activity_patients_list_about:
                Intent about = new Intent(this, AboutActivity.class);
                startActivity(about);
                return true;*/
            case R.id.activity_patients_list_new:
                clickedAdd();
                return true;
            case R.id.agreed:
                boolean newState = !item.isChecked();
//                item.setChecked(newState);
                clickedFilterByAgreed(newState);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem agreedMenuItem = menu.findItem(R.id.agreed);
        Log.i("Leonardo", "onPrepareOptionsMenu: " + filteredByAgreed);
        agreedMenuItem.setChecked(filteredByAgreed);
        return true;
    }

    private void clickedFilterByAgreed(boolean newState) {
        Toast.makeText(this,
                "Clicou apenas filtrados: " + newState,
                Toast.LENGTH_SHORT).show();
//        agreedMenuItem.setChecked(true);
        filteredByAgreed = newState;
        saveFilterSetting(newState);
        refreshList();
    }

    private final ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.activity_patients_list_contextual_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.activity_lista_alunos_menu_delete:
                    deletePatient();
                    mode.finish();
                    return true;
                case R.id.activity_lista_alunos_menu_edit:
                    Patient patient = (Patient) adapter.getItem(selectedPosition);
                    callEditor(patient);
                    mode.finish();
                    return true;
                default:
                    return false;

            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (selectedView != null) {
                selectedView.setBackgroundColor(Color.TRANSPARENT);

            }

            actionMode = null;
            selectedView = null;

            patientsList.setEnabled(true);
        }
    };

    private void deletePatient() {
        Patient patient = (Patient) adapter.getItem(selectedPosition);
        dao.remove(patient);
        adapter.removeRow(selectedPosition);
    }

/*    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.activity_patients_list_contextual_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_lista_alunos_menu_delete:

                AdapterView.AdapterContextMenuInfo menuInfo =
                        (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                Paciente patient = (Paciente) adapter.getItem(menuInfo.position);
                dao.remove(patient);
                adapter.removeRow(menuInfo.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }*/

    private void createHardcodedPatients() {
        String[] names = getResources().getStringArray(R.array.names);
        String[] genders = getResources().getStringArray(R.array.genders);
        String[] medicalPlans = getResources().getStringArray(R.array.medical_plans);
        String[] researchAgreements = getResources().getStringArray(R.array.researchAgreements);


        for (int i = 0; i < names.length; i++) {
            final String nome = names[i];
            final Gender gender = "M".equalsIgnoreCase(genders[i]) ? Gender.MASCULINE : Gender.FEMININE;
            final MedicalPlan medicalPlan = MedicalPlan.valueOf(medicalPlans[i]);
            final boolean agreesWithResearch = "true".equalsIgnoreCase(researchAgreements[i]);

            Patient patient = new Patient(nome, gender, medicalPlan, agreesWithResearch);
            dao.save(patient);
        }
    }

    private void configureList() {
//        System.out.println("Rodando novamente configureList()");
//        List<Paciente> pacientes = dao.findAll();
        patientsList = findViewById(R.id.activity_main_lista_alunos);

        adapter = new PatientListAdapter(this);
        patientsList.setAdapter(adapter);

//        configuraOnItemClickListener(patientsList);

        patientsList.setOnItemLongClickListener((parent, view, position, id) -> {
/*            final Paciente patient = (Paciente) parent.getItemAtPosition(position);
            dao.remove(patient);
            adapter.removeRow(position);
            return true;*/

            if (actionMode != null) {
                return false;
            }

            selectedPosition = position;

            view.setBackgroundColor(Color.LTGRAY);

            selectedView = view;

            patientsList.setEnabled(false);

            actionMode = startSupportActionMode(mActionModeCallback);

            return true;
        });

        registerForContextMenu(patientsList);

        patientsList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
    }

    private void configuraOnItemClickListener(ListView patientsList) {
        patientsList.setOnItemClickListener(((parent, view, position, id) -> {
            final Patient patient = (Patient) parent.getItemAtPosition(position);

            callEditor(patient);
        }));
    }

    private void callEditor(Patient patient) {
        Intent editarPaciente = new Intent(PatientsListActivity.this, PatientFormActivity.class);
        editarPaciente.putExtra(KEY_PATIENT, patient);
        startActivityForResult(editarPaciente, CODE_NEW_PATIENT);
    }

    public void clickedAdd() {
        Intent intent = new Intent(this, PatientFormActivity.class);
        startActivityForResult(intent, CODE_NEW_PATIENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if (requestCode == CODE_NEW_PATIENT && resultCode == RESULT_OK) {
            Patient patient = (Patient) intent.getSerializableExtra(KEY_PATIENT);
//            Log.i("PatientsListActivity", "onActivityResult: recebi o seguinte paciente: " + patient);
            dao.save(patient);
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    public static final String FILTERS_SETTING = "com.vilelapinheiro.FILTROS";
    public static final String AGREED = "only_agreed";

    public void readFilterSetting() {
        SharedPreferences preferences = getSharedPreferences(FILTERS_SETTING, MODE_PRIVATE);

        filteredByAgreed = preferences.getBoolean(AGREED, false);

//        mudarCor(filteredByAgreed);
    }

    private void saveFilterSetting(boolean newState) {
        SharedPreferences preferences = getSharedPreferences(FILTERS_SETTING, MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean(AGREED, newState);
        edit.apply();
    }
}
