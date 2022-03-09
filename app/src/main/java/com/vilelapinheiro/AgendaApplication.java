package com.vilelapinheiro;

import android.app.Application;
import android.util.Log;

import com.vilelapinheiro.dao.PatientDAO;
import com.vilelapinheiro.model.Gender;
import com.vilelapinheiro.model.MedicalPlan;
import com.vilelapinheiro.model.Patient;

public class AgendaApplication extends Application {
    private final PatientDAO dao = new PatientDAO();

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("Leonardo", "onCreate: rodando Application.onCreate");

        createHardcodedPatients();
    }

    private void createHardcodedPatients() {
        String[] names = getResources().getStringArray(R.array.names);
        String[] genders = getResources().getStringArray(R.array.genders);
        String[] medicalPlans = getResources().getStringArray(R.array.medical_plans);
        String[] researchAgreements = getResources().getStringArray(R.array.researchAgreements);


        for (int i = 0; i < names.length; i++) {
            final String nome = names[i];
            final Gender gender = "M".equalsIgnoreCase(genders[i]) ? Gender.MASCULINE : Gender.FEMININE;
            final MedicalPlan medicalPlan = MedicalPlan.valueOf(medicalPlans[i]);
            final boolean agreesWithResearch = "true".equalsIgnoreCase(researchAgreements[i]);

            Patient patient = new Patient(nome, gender, medicalPlan, agreesWithResearch);
            dao.save(patient);
        }
    }

}
