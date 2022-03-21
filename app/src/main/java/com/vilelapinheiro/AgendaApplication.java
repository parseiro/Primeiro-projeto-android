package com.vilelapinheiro;

import android.app.Application;
import android.util.Log;

import com.vilelapinheiro.dao.RoomAppointmentDAO;
import com.vilelapinheiro.dao.RoomPatientDAO;
import com.vilelapinheiro.database.IPatientsDatabase;
import com.vilelapinheiro.model.Appointment;
import com.vilelapinheiro.model.Gender;
import com.vilelapinheiro.model.MedicalPlan;
import com.vilelapinheiro.model.Patient;

import java.time.LocalDateTime;

public class AgendaApplication extends Application {
    public static final String KEY_APPOINTMENT_ID = "appointment_id";
    public static final String KEY_PATIENT_ID = "patient_id";
    public static final int CODE_NEW_PATIENT = 101;
    public static final int CODE_EDIT_PATIENT = 102;
    public static final int CODE_EDIT_APPOINTMENT = 103;
    public static final int CODE_NEW_APPOINTMENT = 103;
    public static final String KEY_PATIENT = "patient";

    private static RoomPatientDAO patientDAO;
    private static RoomAppointmentDAO appointmentDAO;

    public static RoomPatientDAO getPatientDAO() {
        return patientDAO;
    }

    public static RoomAppointmentDAO getAppointmentDAO() {
        return appointmentDAO;
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        Log.i("Leonardo", "onCreate: rodando Application.onCreate");

        initializeRoomDatabases();
        createHardcodedPatients();
        createHardcodedAppointments();

    }

    private void createHardcodedAppointments() {
        if (0 == appointmentDAO.findAll().size()) {
            Appointment appointment1 = new Appointment();
            appointment1.setPatientId(1);
            appointment1.setHorario(LocalDateTime.now());
            appointmentDAO.saveNew(appointment1);

        }
    }

    private void initializeRoomDatabases() {
        patientDAO = IPatientsDatabase.getInstance(this)
                .getRoomPatientDAO();
        appointmentDAO = IPatientsDatabase.getInstance(this)
                .getRoomAppointmentDAO();
    }

    private void createHardcodedPatients() {


        if (0 == patientDAO.findAll().size()) {
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
                patientDAO.saveNew(patient);
            }
        }
    }
}
