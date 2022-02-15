package com.vilelapinheiro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.activity_patients_list_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.activity_lista_alunos_menu_remover) {

            AdapterView.AdapterContextMenuInfo menuInfo =
                    (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Paciente patient = (Paciente) adapter.getItem(menuInfo.position);
            dao.remove(patient);
            adapter.removeRow(menuInfo.position);
        }

        return super.onContextItemSelected(item);
    }

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

        configuraOnItemClickListener(patientsList);

/*        patientsList.setOnItemLongClickListener((parent, view, position, id) -> {
            final Paciente patient = (Paciente) parent.getItemAtPosition(position);
            dao.remove(patient);
            adapter.removeRow(position);
            return true;
        });*/

        registerForContextMenu(patientsList);
    }

    private void configuraOnItemClickListener(ListView patientsList) {
        patientsList.setOnItemClickListener(((parent, view, position, id) -> {
            final Paciente patient = (Paciente) parent.getItemAtPosition(position);

            Intent editarPaciente = new Intent(PatientsListActivity.this, PatientFormActivity.class);
            editarPaciente.putExtra(KEY_PATIENT, patient);
            startActivityForResult(editarPaciente, CODE_NEW_PATIENT);

//            final String mensagem = "Clicou: " + patient.getNomeCompleto();
//            Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
        }));
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
            Log.i("PatientsListActivity", "onActivityResult: cancelado!!");
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }
}
