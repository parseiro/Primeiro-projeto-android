package com.vilelapinheiro.activity;

import static com.vilelapinheiro.activity.PatientsListActivity.KEY_PATIENT;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.vilelapinheiro.R;
import com.vilelapinheiro.model.MedicalPlan;
import com.vilelapinheiro.model.Gender;
import com.vilelapinheiro.model.Patient;

import java.util.Arrays;

public class PatientFormActivity extends AppCompatActivity {
    private EditText nameField;
    private CheckBox agreeWithResearch;
    private RadioGroup sexRadioGroup;
    private RadioButton masculineRdButton, feminineRdButton;
    private Spinner planSpinner;
    private Patient patient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.patient_form_layout);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initializeFields();

        Intent intent = getIntent();
        if (intent.hasExtra(KEY_PATIENT)) {
            patient = (Patient) intent.getSerializableExtra(KEY_PATIENT);
            setTitle(getString(R.string.edit_patient));
        } else {
            patient = new Patient("", Gender.MASCULINE, MedicalPlan.UNIMED, true);
            setTitle(getString(R.string.new_patient));
        }

        fillPatientsData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_patients_form_menu, menu);
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
            case R.id.activity_patient_form_menu_limpar:
                clickedClear();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void fillPatientsData() {
        nameField.setText(patient.getNomeCompleto());
        switch (patient.getSexo()) {
            case MASCULINE:
                masculineRdButton.setChecked(true);
                break;
            case FEMININE:
                feminineRdButton.setChecked(true);
                break;
        }

        agreeWithResearch.setChecked(patient.isAgreesWithResearch());

        int position = Arrays.asList(MedicalPlan.values()).indexOf(patient.getConvenio());
        planSpinner.setSelection(position);
    }

    private void initializeFields() {
        nameField = findViewById(R.id.nameTexteditor);
        agreeWithResearch = findViewById(R.id.pesquisaCheckbox);
        sexRadioGroup = findViewById(R.id.sexoRadiogroup);
        masculineRdButton = findViewById(R.id.masculineRadioBtn);
        feminineRdButton = findViewById(R.id.feminineRadioBtn);
        planSpinner = findViewById(R.id.spinnerConvenios);

        ArrayAdapter<MedicalPlan> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                Arrays.asList(MedicalPlan.values()));
        planSpinner.setAdapter(adapter);
    }

    public void clickedSave() {
        final Patient patient = getPatientData();
        if (patient == null) return;

        finishActivity(RESULT_OK, patient);
    }

    private void finishActivity(int resultCode, @Nullable Patient patient) {
        Intent intent = new Intent();

        if (patient != null) {
            intent.putExtra(KEY_PATIENT, patient);
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
    private Patient getPatientData() {
        final String nameString = nameField.getText().toString();
        if (nameString.isEmpty()) {
            Toast.makeText(this,
                    getString(R.string.name_cant_be_empty),
                    Toast.LENGTH_LONG).show();

            nameField.requestFocus();
            return null;
        }

        final MedicalPlan medicalPlan = (MedicalPlan) planSpinner.getSelectedItem();

        final Gender gender;
        switch (sexRadioGroup.getCheckedRadioButtonId()) {
            case R.id.masculineRadioBtn:
                gender = Gender.MASCULINE;
                break;
            case R.id.feminineRadioBtn:
                gender = Gender.FEMININE;
                break;
            case -1:
            default:
                Toast.makeText(this,
                        R.string.please_select_sex,
                        Toast.LENGTH_LONG).show();

                return null;
        }

        final boolean agrees = agreeWithResearch.isChecked();

        patient.setNomeCompleto(nameString);
        patient.setSexo(gender);
        patient.setConvenio(medicalPlan);
        patient.setAgreesWithResearch(agrees);

        return patient;
    }

    public void clickedClear() {
        Toast.makeText(this,
                R.string.have_cleared_fields,
                Toast.LENGTH_LONG).show();

        nameField.setText(null);
        sexRadioGroup.clearCheck();
        agreeWithResearch.setChecked(false);
        planSpinner.setSelection(0);
    }
}
