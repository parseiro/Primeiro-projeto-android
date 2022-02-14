package com.vilelapinheiro.dao;

import android.util.Log;

import com.vilelapinheiro.model.Paciente;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PatientDAO {

    private final static List<Paciente> alunos = new ArrayList<>();

    private static Long nextId = 1L;

    public static List<Paciente> findAll() {
        return Collections.unmodifiableList(alunos);
    }

    public static void save(final Paciente newPatient) {
//        System.out.println("Salvando newPatient: " + newPatient.getNomeCompleto());

        long desiredId = newPatient.getId();

        if (desiredId == 0L) {
            // Completely new patient
            Log.i("PatientDAO", "save: new patient");
            newPatient.setId(nextId++);
            alunos.add(newPatient);
        } else {
            // editing existing patient

            Log.i("PatientDAO", "save: edit patient");
            Paciente oldPatient = null;

            for (int i = 0; i < alunos.size(); i++) {
                if (alunos.get(i).getId() == desiredId) {
                    // found it

                    alunos.set(i, newPatient); // replace it

                    break; // stop the search
                }
            }
        }
    }
}
