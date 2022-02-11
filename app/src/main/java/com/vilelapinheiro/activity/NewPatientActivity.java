package com.vilelapinheiro.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vilelapinheiro.R;
import com.vilelapinheiro.dao.PacienteDAO;
import com.vilelapinheiro.model.Convenio;
import com.vilelapinheiro.model.Paciente;
import com.vilelapinheiro.model.Sexo;

import java.util.ArrayList;
import java.util.List;

public class NewPatientActivity extends AppCompatActivity {
    private EditText campoNome;
    private CheckBox concordaPesquisas;
    private RadioGroup sexoRadiogroup;
    private Spinner spinnerConvenios;

    PacienteDAO dao = new PacienteDAO();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Novo paciente");
        setContentView(R.layout.entrega1);
        initializeFields();


        {
            final List<String> lista = new ArrayList<>();
            lista.add("");
            lista.add("Unimed");
            lista.add("Amil");
            lista.add("Cassi");
            lista.add("Coopermil");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    lista);
            spinnerConvenios.setAdapter(adapter);
        }


    }

    private void initializeFields() {
        campoNome = findViewById(R.id.nameTexteditor);
        concordaPesquisas = findViewById(R.id.pesquisaCheckbox);
        sexoRadiogroup = findViewById(R.id.sexoRadiogroup);
        spinnerConvenios = findViewById(R.id.spinnerConvenios);
    }

    public void clicouSalvar(View view) {

        final Paciente aluno = criarNovoPaciente();
        if (aluno == null) return;

        dao.save(aluno);

//        startActivity(new Intent(this, MainActivity.class));
        finish();


    }

    @Nullable
    private Paciente criarNovoPaciente() {
        final String nomeCompleto = campoNome.getText().toString();
        if (nomeCompleto.isEmpty()) {
            final String mensagem = "O nome não pode ser vazio";
            Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();

            campoNome.requestFocus();
            return null;
        }

        final String convenioString = (String) spinnerConvenios.getSelectedItem();
        if (convenioString == null || convenioString.isEmpty()) {
            final String mensagem = "Selecione um convênio";
            Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();

            spinnerConvenios.requestFocus();
            return null;
        }


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

        final Paciente aluno = new Paciente(nomeCompleto, sexo,
                new Convenio(convenioString), concorda);
        return aluno;
    }

    public void clicouLimpar(View view) {
        String mensagem = "Limpou campos";

//        System.out.println(mensagem);

        Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();

        campoNome.setText(null);
        sexoRadiogroup.clearCheck();
        concordaPesquisas.setChecked(false);
        spinnerConvenios.setSelection(0);
    }
}
