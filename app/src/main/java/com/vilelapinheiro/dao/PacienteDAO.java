package com.vilelapinheiro.dao;

import com.vilelapinheiro.model.Paciente;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PacienteDAO {

    private final static List<Paciente> alunos =
            new ArrayList<>(
//                    Arrays.asList("Dunha0000", "margo", "Jose", "Maria", "Ana")
            );

    public static List<Paciente> getAlunos() {
        return Collections.unmodifiableList(alunos);
    }

    public void save(Paciente aluno) {
        alunos.add(aluno);
    }
}
