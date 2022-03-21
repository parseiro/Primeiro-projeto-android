package com.vilelapinheiro.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.vilelapinheiro.LocalDateTimeConverter;
import com.vilelapinheiro.dao.RoomAppointmentDAO;
import com.vilelapinheiro.dao.RoomPatientDAO;
import com.vilelapinheiro.model.Appointment;
import com.vilelapinheiro.model.Patient;

@Database(entities = {Patient.class, Appointment.class}, version = 4, exportSchema = false)
@TypeConverters(LocalDateTimeConverter.class)
public abstract class IPatientsDatabase extends RoomDatabase {
    public abstract RoomPatientDAO getRoomPatientDAO();

    public abstract RoomAppointmentDAO getRoomAppointmentDAO();

    private static IPatientsDatabase instance;

    public static IPatientsDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (IPatientsDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context, IPatientsDatabase.class, "agenda.db")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}
