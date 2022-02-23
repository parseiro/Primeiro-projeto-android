package com.vilelapinheiro.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import com.vilelapinheiro.PacienteAdapter;
import com.vilelapinheiro.R;
import com.vilelapinheiro.dao.PatientDAO;
import com.vilelapinheiro.model.Convenio;
import com.vilelapinheiro.model.Paciente;
import com.vilelapinheiro.model.Sexo;

public class PatientsListActivity extends AppCompatActivity {

    public static final String KEY_PATIENT = "patient";
    public static final int CODE_NEW_PATIENT = 101;
    private ListView patientsList;

    PatientDAO dao = new PatientDAO();
    private PacienteAdapter adapter;
    private ActionMode actionMode;
    private int selectedPosition = -1;
    private View selectedView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.patients_list);
        setTitle(getString(R.string.patients));


        createHardcodedPatients();
        configureList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.clearAndAddAll(dao.findAll());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_patients_list_top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_patients_list_about:
                Intent about = new Intent(this, AboutActivity.class);
                startActivity(about);
                return true;
            case R.id.activity_patients_list_new:
                clickedAdd();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                    Paciente patient = (Paciente) adapter.getItem(selectedPosition);
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
        Paciente patient = (Paciente) adapter.getItem(selectedPosition);
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
            final Sexo sexo = "M".equalsIgnoreCase(genders[i]) ? Sexo.MASCULINO : Sexo.FEMININO;
            final Convenio convenio = Convenio.valueOf(medicalPlans[i]);
            final boolean agreesWithResearch = "true".equalsIgnoreCase(researchAgreements[i]);

            Paciente paciente = new Paciente(nome, sexo, convenio, agreesWithResearch);
            dao.save(paciente);
        }
    }

    private void configureList() {
//        System.out.println("Rodando novamente configureList()");
//        List<Paciente> pacientes = dao.findAll();
        patientsList = findViewById(R.id.activity_main_lista_alunos);

        adapter = new PacienteAdapter(this);
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
            final Paciente patient = (Paciente) parent.getItemAtPosition(position);

            callEditor(patient);
        }));
    }

    private void callEditor(Paciente patient) {
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
            Paciente patient = (Paciente) intent.getSerializableExtra(KEY_PATIENT);
//            Log.i("PatientsListActivity", "onActivityResult: recebi o seguinte paciente: " + patient);
            dao.save(patient);
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }
}
