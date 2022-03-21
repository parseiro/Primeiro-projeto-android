package com.vilelapinheiro.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class PatientWithAppointment {
    @Embedded
    public Patient patient;

    @Relation(parentColumn = "patientId",
            entityColumn = "appointmentId")
    public List<Appointment> appointments;
}
