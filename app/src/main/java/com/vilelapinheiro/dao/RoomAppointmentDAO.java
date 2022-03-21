package com.vilelapinheiro.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.vilelapinheiro.model.Appointment;
import com.vilelapinheiro.model.Patient;

import java.util.List;

@Dao
public interface RoomAppointmentDAO {
    @Insert
    void saveNew(Appointment appointment);

    @Update
    void update(Appointment appointment);

    @Query("SELECT * from appointments")
    public List<Appointment> findAll();

    @Delete
    public void remove(Appointment appointment);

    @Query("SELECT * from appointments WHERE appointment_id = :id")
    public Appointment findById(long id);

    @Query("SELECT * from appointments a where a.patient_id = :patientId")
    public List<Appointment> findAllByPatientId(long patientId);
}
