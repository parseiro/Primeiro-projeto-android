package com.vilelapinheiro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vilelapinheiro.R;
import com.vilelapinheiro.dao.PatientDAO;
import com.vilelapinheiro.model.Convenio;
import com.vilelapinheiro.model.Paciente;
import com.vilelapinheiro.model.Sexo;

import java.util.Arrays;
import java.util.List;

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
        patient = (Paciente) intent.getSerializableExtra("patient");
        if (patient == null) {
            setTitle("Novo paciente");
            patient = new Paciente("", Sexo.MASCULINO, Convenio.UNIMED, true);
        } else {
            setTitle("Editar paciente");
        }

        nameField.setText(patient.getNomeCompleto());
        switch (patient.getSexo()) {
            case MASCULINO:
//                Log.i("PatientFormActivity", "masculino");
                masculineRdButton.setChecked(true);
                break;
            case FEMININO:
//                Log.i("PatientFormActivity", "feminino");
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

        {
            final List<Convenio> lista = Arrays.asList(Convenio.values());
/*            for (Convenio c : Convenio.values()) {

            }
            lista.add("Unimed");
            lista.add("Amil");
            lista.add("Cassi");
            lista.add("Coopermil");*/
            ArrayAdapter<Convenio> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    lista);
            spinnerConvenios.setAdapter(adapter);
        }
    }

    public void clicouSalvar(View view) {

        final Paciente patient = getPatientData();
        if (patient == null) return;

        dao.save(patient);

//        startActivity(new Intent(this, MainActivity.class));
        finish();


    }

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
/*
        if (convenioString == null || convenioString.isEmpty()) {
            final String mensagem = "Selecione um convênio";
            Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();

            spinnerConvenios.requestFocus();
            return null;
        }
        Convenio convenio = Convenio.valueOf(convenioString);
*/


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

    public void clicouLimpar(View view) {
        String mensagem = "Limpou campos";

        Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();

        nameField.setText(null);
        sexoRadiogroup.clearCheck();
        concordaPesquisas.setChecked(false);
        spinnerConvenios.setSelection(0);
    }

    public void cancelar(View view) {
        finish();
    }
}
