package com.vilelapinheiro.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.vilelapinheiro.model.Patient;

import java.util.List;

@Dao
public interface RoomPatientDAO {
    @Insert
    void saveNew(Patient patient);

    @Update
    void update(Patient patient);

    @Query("SELECT * from patients ORDER BY nomeCompleto ASC")
    public List<Patient> findAll();

    @Delete
    public void remove(Patient patient);

    @Query("SELECT * from patients WHERE patient_id = :id")
    public Patient findById(final long id);
}
