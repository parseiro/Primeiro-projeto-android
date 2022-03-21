package com.vilelapinheiro.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "patients")
public class Patient implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name =  "patient_id")
    private long patientId;

    private String nomeCompleto;

    private Gender gender;

    public Gender getGender() {
        return gender;
    }

    public MedicalPlan getMedicalPlan() {
        return medicalPlan;
    }

    private MedicalPlan medicalPlan;

    boolean agreesWithResearch;

    public Patient(String nomeCompleto, Gender gender, MedicalPlan medicalPlan, boolean agreesWithResearch) {
        this.nomeCompleto = nomeCompleto;
        this.gender = gender;
        this.medicalPlan = medicalPlan;
        this.agreesWithResearch = agreesWithResearch;
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public Gender getSexo() {
        return gender;
    }

    public String getSexoShortString() {
        return gender == Gender.FEMININE ? "F" : "M";
    }

    public void setSexo(Gender gender) {
        this.gender = gender;
    }

    public MedicalPlan getConvenio() {
        return medicalPlan;
    }

    public void setConvenio(MedicalPlan medicalPlan) {
        this.medicalPlan = medicalPlan;
    }

    public boolean isAgreesWithResearch() {
        return agreesWithResearch;
    }

    public void setAgreesWithResearch(boolean agreesWithResearch) {
        this.agreesWithResearch = agreesWithResearch;
    }

    @Override
    public String toString() {
        return nomeCompleto;
    }
}
