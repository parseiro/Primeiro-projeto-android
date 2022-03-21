package com.vilelapinheiro.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(tableName = "appointments", foreignKeys = @ForeignKey(
        entity = Patient.class, parentColumns = "patient_id", childColumns = "patient_id"))
public class Appointment implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name= "appointment_id")
    private long appointmentId;

    private LocalDateTime horario;

    @ColumnInfo(name = "patient_id", index = true)
    private long patientId;


    public long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public LocalDateTime getHorario() {
        return horario;
    }

    public void setHorario(LocalDateTime horario) {
        this.horario = horario;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", horario=" + horario +
                ", patientId=" + patientId +
                '}';
    }
}
