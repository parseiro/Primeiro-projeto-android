package com.vilelapinheiro.activity;

import static com.vilelapinheiro.AgendaApplication.KEY_APPOINTMENT_ID;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.vilelapinheiro.AgendaApplication;
import com.vilelapinheiro.R;
import com.vilelapinheiro.UtilsDate;
import com.vilelapinheiro.dao.RoomAppointmentDAO;
import com.vilelapinheiro.dao.RoomPatientDAO;
import com.vilelapinheiro.model.Appointment;
import com.vilelapinheiro.model.Patient;

import java.time.LocalDateTime;
import java.util.List;

public class AppointmentFormActivity extends AppCompatActivity {
    private EditText editTextDataReuniao;
    private LocalDateTime horarioTemp;
    private Appointment appointment;
    private Spinner patientsSpinner;
    private RoomPatientDAO patientDAO;
    private RoomAppointmentDAO appointmentDAO;
    private List<Patient> patientList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.appointment_form_layout);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        patientDAO = AgendaApplication.getPatientDAO();
        appointmentDAO = AgendaApplication.getAppointmentDAO();

        Intent intent = getIntent();
        if (intent.hasExtra(KEY_APPOINTMENT_ID)) {
            Long appointmentId = (Long) intent.getSerializableExtra(KEY_APPOINTMENT_ID);

            appointment = appointmentDAO.findById(appointmentId);

            Log.i("Leonardo", "editando: " + appointment);

            setTitle(getString(R.string.edit_appointment));
        } else {
            appointment = new Appointment();

            setTitle(getString(R.string.new_appointment));
        }

        initializeFields();

        fillAppointmentData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_appointment_form_top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_patient_form_menu_salvar:
                clickedSave();
                return true;
            case android.R.id.home:
            case R.id.activity_patient_form_menu_cancelar:
                clickedCancel();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fillAppointmentData() {
        {
//            Log.i("Leonardo", "fillAppointmentData appointment: " + appointment);
            horarioTemp = appointment.getHorario();
            if (horarioTemp == null) horarioTemp = LocalDateTime.now();
            String textoData = UtilsDate.formatDate(
                    AppointmentFormActivity.this,
                    horarioTemp
            );
            editTextDataReuniao.setText(textoData);
        }

        {
            Long patientId = appointment.getPatientId();
//            Log.i("Leonardo", "fillAppointmentData patientId: " + patientId);
            if (patientId == null) {
                patientsSpinner.setSelection(0);
            } else {
                Patient patient = patientDAO.findById(patientId);
//                Log.i("Leonardo", "fillAppointmentData patient: " + patient);
                if (patient == null) {
                    patientsSpinner.setSelection(0);
                } else {
                    int position = patientList.indexOf(patient);
                    Log.i("Leonardo", "fillAppointmentData position: " + position);

                    SpinnerAdapter adapter = patientsSpinner.getAdapter();
                    for (int i = 0; i < adapter.getCount(); i++) {
                        Patient pt = (Patient) adapter.getItem(i);
                        if (patientId == pt.getPatientId()) {
                            patientsSpinner.setSelection(i);
                            break;
                        }
                    }


                }
            }
        }
    }

    private void initializeFields() {
        editTextDataReuniao = findViewById(R.id.editTextDataReuniao);

        editTextDataReuniao.setFocusable(false);

//        calendarDataReuniao = Calendar.getInstance();
        horarioTemp = appointment.getHorario() != null ? appointment.getHorario() : LocalDateTime.now();

        DatePickerDialog.OnDateSetListener listenerDataReuniao = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
//                calendarDataReuniao.set(year, month, dayOfMonth);
                horarioTemp = LocalDateTime.of(year, month, dayOfMonth, 0, 0);
//                Log.i("Leonardo", "onDateSet: " + horarioTemp);

                String textoData = UtilsDate.formatDate(
                        AppointmentFormActivity.this,
                        horarioTemp
                );
                editTextDataReuniao.setText(textoData);

            }
        };

        editTextDataReuniao.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (horarioTemp == null) horarioTemp = LocalDateTime.now();

                DatePickerDialog picker = new DatePickerDialog(AppointmentFormActivity.this,
                        listenerDataReuniao,
                        horarioTemp.getYear(),
                        horarioTemp.getMonthValue(),
                        horarioTemp.getDayOfMonth());

                picker.show();
            }
        });

        {
            patientList = patientDAO.findAll();
            patientsSpinner = findViewById(R.id.spinnerPatients);
            ArrayAdapter<Patient> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    patientList);
            patientsSpinner.setAdapter(adapter);
        }
    }

    public void clickedSave() {
        final Appointment appointment = getAppointmentData();
        if (appointment == null) return;

        if (appointment.getAppointmentId() == 0) {
            // save new
            appointmentDAO.saveNew(appointment);
        } else {
            // update
            appointmentDAO.update(appointment);
        }

        finishActivity(RESULT_OK, appointment);
    }

    private void finishActivity(int resultCode, @Nullable Appointment appointment) {
        Intent intent = new Intent();

        if (appointment != null) {
            intent.putExtra(KEY_APPOINTMENT_ID, appointment);
        }
        setResult(resultCode, intent);
        finish();
    }

    private void clickedCancel() {
        finishActivity(RESULT_CANCELED, null);
    }

    @Override
    public void onBackPressed() {
        clickedCancel();
    }

    @Nullable
    private Appointment getAppointmentData() {

        // validate data
        if (horarioTemp == null) {
            Toast.makeText(this,
                    getString(R.string.date_cant_be_empty),
                    Toast.LENGTH_LONG).show();
            return null;
        }

        // prepare data
        Patient patient = (Patient) patientsSpinner.getSelectedItem();

        // save data
        appointment.setHorario(horarioTemp);
        appointment.setPatientId(patient.getPatientId());

        return appointment;
    }
}
