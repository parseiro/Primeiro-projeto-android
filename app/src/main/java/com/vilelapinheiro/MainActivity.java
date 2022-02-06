package com.vilelapinheiro;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Lista de alunos");

        setContentView(R.layout.activity_main);
        List<String> alunos = new ArrayList<>(Arrays.asList("Dunha0000", "margo", "Jose", "Maria", "Ana"));
        ListView listaDeAlunos = findViewById(R.id.activity_main_lista_alunos);

        listaDeAlunos.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                alunos));

    }
}
