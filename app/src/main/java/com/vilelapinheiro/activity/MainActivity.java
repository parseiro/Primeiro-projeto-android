package com.vilelapinheiro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vilelapinheiro.R;
import com.vilelapinheiro.dao.PacienteDAO;
import com.vilelapinheiro.model.Convenio;
import com.vilelapinheiro.model.Paciente;
import com.vilelapinheiro.model.Sexo;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView patientsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Pacientes");
        setContentView(R.layout.activity_main);

        patientsList = findViewById(R.id.activity_main_lista_alunos);

        createEntities();
        configureList();

    }

    @Override
    protected void onResume() {
        super.onResume();

        configureList();
    }

    private void createEntities() {
        String[] nomes = getResources().getStringArray(R.array.nomes);
        String[] sexos = getResources().getStringArray(R.array.sexos);
        String[] convenios = getResources().getStringArray(R.array.convenios);
        String[] pesquisas = getResources().getStringArray(R.array.pesquisas);


        for (int i = 0; i < nomes.length; i++) {
            final String nome = nomes[i];
            final Sexo sexo = "M".equalsIgnoreCase(sexos[i]) ? Sexo.MASCULINO : Sexo.FEMININO;
            final Convenio convenio = new Convenio(convenios[i]);
            final boolean concorda = "true".equalsIgnoreCase(pesquisas[i]) ? true : false;

            Paciente paciente = new Paciente(nome, sexo, convenio, concorda);
            PacienteDAO.save(paciente);
        }
    }

    private void configureList() {
//        System.out.println("Rodando novamente configureList()");
        List<Paciente> pacientes = PacienteDAO.findAll();

        patientsList.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                pacientes));

        patientsList.setOnItemClickListener(((parent, view, position, id) -> {
            final Paciente patient = (Paciente) patientsList.getItemAtPosition(position);

            final String mensagem = "Clicou: " + patient.getNomeCompleto();
            Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();
        }));
    }

    public void clicouAdd(View view) {
        startActivity(new Intent(this, NewPatientActivity.class));
    }
}
