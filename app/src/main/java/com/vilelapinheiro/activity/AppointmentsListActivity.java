package com.vilelapinheiro.activity;


import static com.vilelapinheiro.AgendaApplication.CODE_EDIT_APPOINTMENT;
import static com.vilelapinheiro.AgendaApplication.CODE_NEW_APPOINTMENT;
import static com.vilelapinheiro.AgendaApplication.KEY_APPOINTMENT_ID;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import com.vilelapinheiro.AppointmentListAdapter;
import com.vilelapinheiro.R;
import com.vilelapinheiro.dao.RoomAppointmentDAO;
import com.vilelapinheiro.database.IPatientsDatabase;
import com.vilelapinheiro.model.Appointment;

public class AppointmentsListActivity extends AppCompatActivity {
    private ListView appointmentsList;
    private RoomAppointmentDAO appointmentDAO;
    private AppointmentListAdapter adapter;
    private ActionMode actionMode;
    private int selectedPosition = -1;
    private View selectedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.appointments));
        setContentView(R.layout.activity_appointments);

        appointmentDAO = IPatientsDatabase.getInstance(this)
                .getRoomAppointmentDAO();

        configureList();

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        adapter.clearAndAddAll(appointmentDAO.findAll());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_appointments_top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.activity_appointment_new) {
            clickedAdd();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clickedAdd() {
        Intent intent = new Intent(this, AppointmentFormActivity.class);
        startActivityForResult(intent, CODE_NEW_APPOINTMENT);
    }

    private void configureList() {
//        System.out.println("Rodando novamente configureList()");
        appointmentsList = findViewById(R.id.appointments_list);

        adapter = new AppointmentListAdapter(this);
        appointmentsList.setAdapter(adapter);

//        configuraOnItemClickListener(appointmentsList);

        configureLongClick();

        registerForContextMenu(appointmentsList);

        appointmentsList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
    }

    private void configureLongClick() {
        appointmentsList.setOnItemLongClickListener((parent, view, position, id) -> {

            if (actionMode != null) {
                return false;
            }

            selectedPosition = position;

            view.setBackgroundColor(Color.LTGRAY);

            selectedView = view;

            appointmentsList.setEnabled(false);

            actionMode = startSupportActionMode(mActionModeCallback);

            return true;
        });
    }

/*    private void configuraOnItemClickListener(ListView patientsList) {
        patientsList.setOnItemClickListener(((parent, view, position, id) -> {
            final Patient patient = (Patient) parent.getItemAtPosition(position);

            callViewer(patient);
        }));
    }*/

    private final ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.activity_appointments_contextual_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            final int itemId = item.getItemId();

            if (itemId == R.id.activity_appointment_menu_delete) {
                askAndDeleteAppointment(AppointmentsListActivity.this);
                mode.finish();
                return true;
            } else if (itemId == R.id.activity_appointment_menu_edit) {
                Appointment appointment = (Appointment) adapter.getItem(selectedPosition);
                callEditor(appointment);
                mode.finish();
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (selectedView != null) {
                selectedView.setBackgroundColor(Color.TRANSPARENT);
            }

            actionMode = null;
            selectedView = null;

            appointmentsList.setEnabled(true);
        }
    };

    private void callEditor(Appointment appointment) {
        Intent editAppointment = new Intent(AppointmentsListActivity.this, AppointmentFormActivity.class);
        editAppointment.putExtra(KEY_APPOINTMENT_ID, appointment.getAppointmentId());
        startActivityForResult(editAppointment, CODE_EDIT_APPOINTMENT);
    }

    private void askAndDeleteAppointment(Context context) {
        AlertDialog.Builder builder = new AlertDialog
                .Builder(context)
                .setTitle(R.string.are_you_sure)
                .setMessage(R.string.are_you_sure_to_remove_appointment)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    deleteAppointment();
                })
                .setNegativeButton(R.string.no, null);
        builder.show();
    }

    private void deleteAppointment() {
        Appointment appointment = (Appointment) adapter.getItem(selectedPosition);
        appointmentDAO.remove(appointment);
        adapter.removeRow(selectedPosition);
    }

/*    private void callViewer(Patient patient) {
        Intent viewPatient = new Intent(AppointmentsListActivity.this, AppointmentViewActivity.class);
        viewPatient.putExtra(KEY_PATIENT_ID, patient.getId());
        startActivityForResult(viewPatient, CODE_NEW_PATIENT);
    }*/

}