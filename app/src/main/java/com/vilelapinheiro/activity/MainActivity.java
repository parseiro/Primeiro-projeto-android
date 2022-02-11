package com.vilelapinheiro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vilelapinheiro.R;
import com.vilelapinheiro.dao.PacienteDAO;
import com.vilelapinheiro.model.Paciente;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final ListView listaDeAlunos = findViewById(R.id.activity_main_lista_alunos);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Pacientes");
        setContentView(R.layout.activity_main);

        configureList();

    }

    @Override
    protected void onResume() {
        super.onResume();

        configureList();
    }

    private void configureList() {
        List<Paciente> alunos = PacienteDAO.getAlunos();

        listaDeAlunos.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                alunos));
    }

    public void clicouAdd(View view) {
        startActivity(new Intent(this, NewPatientActivity.class));
    }
}
