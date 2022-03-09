package com.vilelapinheiro.dao;

import android.util.Log;

import com.vilelapinheiro.model.Patient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PatientDAO {

    private final static List<Patient> patients = new ArrayList<>();

    private static Long nextId = 1L;

    public List<Patient> findAll() {
        Log.i("Leonardo", "findAll: tamanho da lista: " + patients.size());
        return Collections.unmodifiableList(patients);
    }

    public void save(final Patient newPatient) {
//        System.out.println("Salvando newPatient: " + newPatient.getNomeCompleto());

        long desiredId = newPatient.getId();

        if (desiredId == 0L) {
            // Completely new patient
            Log.i("PatientDAO", "save: new patient");
            newPatient.setId(nextId++);
            patients.add(newPatient);
        } else {
            // editing existing patient

//            Log.i("PatientDAO", "save: edit patient");

            for (int i = 0; i < patients.size(); i++) {
                if (patients.get(i).getId() == desiredId) {
                    // found it

                    patients.set(i, newPatient); // replace it

                    break; // stop the search
                }
            }
        }
    }

    public void remove(Patient patient) {
        patients.remove(patient);
    }
}
