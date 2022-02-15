package com.vilelapinheiro.activity;

import static com.vilelapinheiro.activity.PatientsListActivity.KEY_PATIENT;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vilelapinheiro.R;
import com.vilelapinheiro.dao.PatientDAO;
import com.vilelapinheiro.model.Convenio;
import com.vilelapinheiro.model.Paciente;
import com.vilelapinheiro.model.Sexo;

import java.util.Arrays;

public class PatientFormActivity extends AppCompatActivity {
    private EditText nameField;
    private CheckBox concordaPesquisas;
    private RadioGroup sexoRadiogroup;
    private RadioButton masculineRdButton, feminineRdButton;
    private Spinner spinnerConvenios;
    private Paciente patient;

    PatientDAO dao = new PatientDAO();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.patient_form_layout);
        initializeFields();

        Intent intent = getIntent();
        patient = (Paciente) intent.getSerializableExtra(KEY_PATIENT);
        if (patient == null) {
            patient = new Paciente("", Sexo.MASCULINO, Convenio.UNIMED, true);
            setTitle("Novo paciente");
        } else {
            setTitle("Editar paciente");
        }

        fillPatientsData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_patients_form_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_patient_form_menu_salvar:
//                Log.w("TAG", "salvar");
                clickedSave();
                break;
            case R.id.activity_patient_form_menu_cancelar:
//                Log.w("TAG", "cancelar");
                clickedCancel();
                break;
            case R.id.activity_patient_form_menu_limpar:
                clicouLimpar();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fillPatientsData() {
        nameField.setText(patient.getNomeCompleto());
        switch (patient.getSexo()) {
            case MASCULINO:
                masculineRdButton.setChecked(true);
                break;
            case FEMININO:
                feminineRdButton.setChecked(true);
                break;
        }

        concordaPesquisas.setChecked(patient.isConcordaPesquisas());

        int position = Arrays.asList(Convenio.values()).indexOf(patient.getConvenio());
        spinnerConvenios.setSelection(position);
    }

    private void initializeFields() {
        nameField = findViewById(R.id.nameTexteditor);
        concordaPesquisas = findViewById(R.id.pesquisaCheckbox);
        sexoRadiogroup = findViewById(R.id.sexoRadiogroup);
        masculineRdButton = findViewById(R.id.masculineRadioBtn);
        feminineRdButton = findViewById(R.id.feminineRadioBtn);
        spinnerConvenios = findViewById(R.id.spinnerConvenios);

        ArrayAdapter<Convenio> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                Arrays.asList(Convenio.values()));
        spinnerConvenios.setAdapter(adapter);
    }

    public void clickedSave() {
        final Paciente patient = getPatientData();
        if (patient == null) return;

        dao.save(patient);

        finish();
    }

//    public void clickedSave(View view) {
//        clickedSave();
//    }

    @Nullable
    private Paciente getPatientData() {
        final String nameString = nameField.getText().toString();
        if (nameString.isEmpty()) {
            final String mensagem = "O nome não pode ser vazio";
            Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();

            nameField.requestFocus();
            return null;
        }

        final Convenio convenio = (Convenio) spinnerConvenios.getSelectedItem();

        final Sexo sexo;
        switch (sexoRadiogroup.getCheckedRadioButtonId()) {
            case R.id.masculineRadioBtn:
                sexo = Sexo.MASCULINO;
                break;
            case R.id.feminineRadioBtn:
                sexo = Sexo.FEMININO;
                break;
            case -1:
            default:
                final String mensagem = "Por favor selecione o sexo";
                Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();

                return null;
        }

        final boolean concorda = concordaPesquisas.isChecked();

        patient.setNomeCompleto(nameString);
        patient.setSexo(sexo);
        patient.setConvenio(convenio);
        patient.setConcordaPesquisas(concorda);

        return patient;
    }

    public void clicouLimpar() {
        String mensagem = "Limpou campos";

        Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();

        nameField.setText(null);
        sexoRadiogroup.clearCheck();
        concordaPesquisas.setChecked(false);
        spinnerConvenios.setSelection(0);
    }

//    public void clickedCancel(View view) {
//        clickedCancel();
//    }

    private void clickedCancel() {
        finish();
    }
}
