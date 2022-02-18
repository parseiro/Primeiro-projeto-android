package com.vilelapinheiro.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
    private int posicaoSelecionada = -1;
    private View viewSelecionada;

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
//                Log.w("TAG", "About");
                Intent about = new Intent(this, AboutActivity.class);
                startActivity(about);
                return true;
            case R.id.activity_patients_list_new:
//                Log.w("TAG", "Novo");
                clickedAdd();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
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
                    Paciente patient = (Paciente) adapter.getItem(posicaoSelecionada);
                    callEditor(patient);
                    Toast.makeText(PatientsListActivity.this, "Clicou editar", Toast.LENGTH_SHORT).show();
                    mode.finish();
                    return true;
                default:
                    return false;

            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (viewSelecionada != null) {
                viewSelecionada.setBackgroundColor(Color.TRANSPARENT);

            }

            actionMode = null;
            viewSelecionada = null;

            patientsList.setEnabled(true);
        }
    };

    private void deletePatient() {
        Paciente patient = (Paciente) adapter.getItem(posicaoSelecionada);
        dao.remove(patient);
        adapter.removeRow(posicaoSelecionada);
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
        String[] nomes = getResources().getStringArray(R.array.nomes);
        String[] sexos = getResources().getStringArray(R.array.sexos);
        String[] convenios = getResources().getStringArray(R.array.convenios);
        String[] pesquisas = getResources().getStringArray(R.array.pesquisas);


        for (int i = 0; i < nomes.length; i++) {
            final String nome = nomes[i];
            final Sexo sexo = "M".equalsIgnoreCase(sexos[i]) ? Sexo.MASCULINO : Sexo.FEMININO;
            final Convenio convenio = Convenio.valueOf(convenios[i]);
            final boolean concorda = "true".equalsIgnoreCase(pesquisas[i]) ? true : false;

            Paciente paciente = new Paciente(nome, sexo, convenio, concorda);
            PatientDAO.save(paciente);
        }
    }

    private void configureList() {
//        System.out.println("Rodando novamente configureList()");
//        List<Paciente> pacientes = PatientDAO.findAll();
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

            posicaoSelecionada = position;

            view.setBackgroundColor(Color.LTGRAY);

            viewSelecionada = view;

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
        } else if (resultCode == RESULT_CANCELED) {
//            Log.i("PatientsListActivity", "onActivityResult: cancelado!!");
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }
}
