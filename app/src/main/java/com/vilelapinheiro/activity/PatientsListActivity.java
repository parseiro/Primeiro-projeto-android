package com.vilelapinheiro.activity;

import static com.vilelapinheiro.AgendaApplication.CODE_EDIT_PATIENT;
import static com.vilelapinheiro.AgendaApplication.CODE_NEW_PATIENT;
import static com.vilelapinheiro.AgendaApplication.KEY_PATIENT_ID;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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

import com.vilelapinheiro.AgendaApplication;
import com.vilelapinheiro.PatientListAdapter;
import com.vilelapinheiro.R;
import com.vilelapinheiro.dao.RoomAppointmentDAO;
import com.vilelapinheiro.dao.RoomPatientDAO;
import com.vilelapinheiro.database.IPatientsDatabase;
import com.vilelapinheiro.model.Appointment;
import com.vilelapinheiro.model.Patient;

import java.util.List;

public class PatientsListActivity extends AppCompatActivity {


    private ListView patientsList;

    private RoomPatientDAO patientDAO;
    private PatientListAdapter adapter;
    private ActionMode actionMode;
    private int selectedPosition = -1;
    private View selectedView;

    private boolean filteredByAgreed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_patients_list);
        setTitle(getString(R.string.patients));

        patientDAO = IPatientsDatabase.getInstance(this)
                .getRoomPatientDAO();

        configureList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        adapter.clearAndAddAll(patientDAO.findAll(), filteredByAgreed);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_patients_list_top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final int id = item.getItemId();

        if (id == R.id.activity_patients_list_new) {
            clickedAdd();
            return true;
        } else if (id == R.id.agreed) {
            boolean newState = !item.isChecked();
//                item.setChecked(newState);
            clickedFilterByAgreed(newState);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem agreedMenuItem = menu.findItem(R.id.agreed);
//        Log.i("Leonardo", "onPrepareOptionsMenu: " + filteredByAgreed);
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
            mode.getMenuInflater().inflate(R.menu.activity_patients_contextual_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            final int itemId = item.getItemId();

            if (itemId == R.id.activity_patients_menu_delete) {
                askAndDeletePatient(PatientsListActivity.this);
                mode.finish();
                return true;
            } else if (itemId == R.id.activity_patients_menu_edit) {
                Patient patient = (Patient) adapter.getItem(selectedPosition);
                callEditor(patient);
                mode.finish();
                return true;
            } else {
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

    private void askAndDeletePatient(Context context) {
        AlertDialog.Builder builder = new AlertDialog
                .Builder(context)
                .setTitle(R.string.are_you_sure)
                .setMessage(R.string.are_you_sure_to_remove_patient)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    deletePatient();
                })
                .setNegativeButton(R.string.no, null);
        builder.show();
    }

    private void deletePatient() {
        Patient patient = (Patient) adapter.getItem(selectedPosition);

        RoomAppointmentDAO appointmentDAO = AgendaApplication.getAppointmentDAO();
        List<Appointment> allByPatientId = appointmentDAO.findAllByPatientId(patient.getPatientId());
        for (Appointment ap : allByPatientId) {
            appointmentDAO.remove(ap);
        }

        patientDAO.remove(patient);
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

    private void configureList() {
//        System.out.println("Rodando novamente configureList()");
        patientsList = findViewById(R.id.activity_patients_list);

        adapter = new PatientListAdapter(this);
        patientsList.setAdapter(adapter);

//        configuraOnItemClickListener(patientsList);

        configureLongClick();

        registerForContextMenu(patientsList);

        patientsList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
    }

    private void configureLongClick() {
        patientsList.setOnItemLongClickListener((parent, view, position, id) -> {

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
    }

/*    private void configuraOnItemClickListener(ListView patientsList) {
        patientsList.setOnItemClickListener(((parent, view, position, id) -> {
            final Patient patient = (Patient) parent.getItemAtPosition(position);

            callViewer(patient);
        }));
    }*/

    private void callEditor(Patient patient) {
        Intent editarPaciente = new Intent(PatientsListActivity.this, PatientFormActivity.class);
        editarPaciente.putExtra(AgendaApplication.KEY_PATIENT, patient);
        startActivityForResult(editarPaciente, CODE_EDIT_PATIENT);
    }

    private void callViewer(Patient patient) {
        Intent viewPatient = new Intent(PatientsListActivity.this, PatientViewActivity.class);
        viewPatient.putExtra(KEY_PATIENT_ID, patient.getPatientId());
        startActivityForResult(viewPatient, CODE_NEW_PATIENT);
    }

    public void clickedAdd() {
        Intent intent = new Intent(this, PatientFormActivity.class);
        startActivityForResult(intent, CODE_NEW_PATIENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if (requestCode == CODE_NEW_PATIENT && resultCode == RESULT_OK) {
            Patient patient = (Patient) intent.getSerializableExtra(AgendaApplication.KEY_PATIENT);
//            Log.i("PatientsListActivity", "onActivityResult: recebi o seguinte paciente: " + patient);
            patientDAO.saveNew(patient);
        } else if (requestCode == CODE_EDIT_PATIENT && resultCode == RESULT_OK) {
            Patient patient = (Patient) intent.getSerializableExtra(AgendaApplication.KEY_PATIENT);
//            Log.i("PatientsListActivity", "onActivityResult: recebi o seguinte paciente: " + patient);
            patientDAO.update(patient);
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    public static final String FILTERS_SETTING = "com.vilelapinheiro.FILTROS";
    public static final String AGREED = "only_agreed";

    private void saveFilterSetting(boolean newState) {
        SharedPreferences preferences = getSharedPreferences(FILTERS_SETTING, MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean(AGREED, newState);
        edit.apply();
    }
}
