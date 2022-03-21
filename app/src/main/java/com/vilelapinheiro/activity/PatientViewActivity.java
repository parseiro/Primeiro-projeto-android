package com.vilelapinheiro.activity;


import static com.vilelapinheiro.AgendaApplication.KEY_PATIENT_ID;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.vilelapinheiro.R;
import com.vilelapinheiro.database.IPatientsDatabase;
import com.vilelapinheiro.model.Patient;

public class PatientViewActivity extends AppCompatActivity {
    private TextView idTextview;
    private TextView nameField;
    private CheckBox agreeWithResearch;
    private TextView sexTextview;
    private TextView medicalplanTextview;
    private Patient patient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.patient_show_layout);
        setTitle(R.string.view_patient);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initializeFields();

        Intent intent = getIntent();
        final Long patientId = (Long) intent.getSerializableExtra(KEY_PATIENT_ID);

        patient = IPatientsDatabase.getInstance(this).getRoomPatientDAO().findById(patientId);

        fillPatientsData();
    }

    private void fillPatientsData() {
        idTextview.setText("" + patient.getPatientId());
        nameField.setText(patient.getNomeCompleto());
        switch (patient.getSexo()) {
            case MASCULINE:
                sexTextview.setText(R.string.masculine);
                break;
            case FEMININE:
                sexTextview.setText(R.string.feminine);
                break;
        }

        agreeWithResearch.setChecked(patient.isAgreesWithResearch());

        medicalplanTextview.setText(patient.getConvenio().toString());
    }

    private void initializeFields() {
        idTextview = findViewById(R.id.idTextview);
        nameField = findViewById(R.id.fullnameTextView);
        agreeWithResearch = findViewById(R.id.pesquisaCheckboxReader);
        sexTextview = findViewById(R.id.sexTextview);
        medicalplanTextview = findViewById(R.id.medicalplanTextview);
    }

/*    public void clickedSave() {
        final Patient patient = getPatientData();
        if (patient == null) return;

        finishActivity(RESULT_OK, patient);
    }*/

    private void finishActivity(int resultCode, @Nullable Patient patient) {
/*        Intent intent = new Intent();

        if (patient != null) {
            intent.putExtra(KEY_PATIENT, patient);
        }
        setResult(resultCode, intent);*/
        finish();
    }

    private void clickedCancel() {
        finishActivity(RESULT_CANCELED, null);
    }

    @Override
    public void onBackPressed() {
        clickedCancel();
    }



/*    public void clickedClear() {
        Toast.makeText(this,
                R.string.have_cleared_fields,
                Toast.LENGTH_LONG).show();

        nameField.setText(null);
        sexRadioGroup.clearCheck();
        agreeWithResearch.setChecked(false);
        planSpinner.setSelection(0);
    }*/
}
