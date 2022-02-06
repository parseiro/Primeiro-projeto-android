package com.vilelapinheiro;

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

import java.util.ArrayList;
import java.util.List;

public class Entrega1Activity extends AppCompatActivity {
    private EditText campoNome;
    private CheckBox concordaPesquisas;
    private RadioGroup sexoRadiogroup;
    private Spinner spinnerConvenios;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.entrega1);

        campoNome = findViewById(R.id.nameTexteditor);
        concordaPesquisas = findViewById(R.id.pesquisaCheckbox);
        sexoRadiogroup = findViewById(R.id.sexoRadiogroup);

        spinnerConvenios = findViewById(R.id.spinnerConvenios);
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

    public void clicouSalvar(View view) {
//        System.out.println("clicouSalvar()");

        final String nomeCompleto = campoNome.getText().toString();
//        System.out.println("nomeCompleto: '" + nomeCompleto + "'");
        if (nomeCompleto.isEmpty()) {
            final String mensagem = "O nome não pode ser vazio";
            Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();

            campoNome.requestFocus();
            return;
        }

        String convenioString = (String) spinnerConvenios.getSelectedItem();
        if (convenioString == null || convenioString.isEmpty()) {
            final String mensagem = "Selecione um convênio";
            Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();

            spinnerConvenios.requestFocus();
            return;
        }


        if (sexoRadiogroup.getCheckedRadioButtonId() == -1) {
            final String mensagem = "Por favor selecione o sexo";
            Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();

            return;
        }

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
