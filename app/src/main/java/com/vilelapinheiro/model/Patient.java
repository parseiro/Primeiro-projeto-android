package com.vilelapinheiro.model;

import java.io.Serializable;

public class Patient implements Serializable {
    private long id;

    private String nomeCompleto;

    private Gender gender;

    private MedicalPlan medicalPlan;

    boolean agreesWithResearch;

    public Patient(String nomeCompleto, Gender gender, MedicalPlan medicalPlan, boolean agreesWithResearch) {
        this.nomeCompleto = nomeCompleto;
        this.gender = gender;
        this.medicalPlan = medicalPlan;
        this.agreesWithResearch = agreesWithResearch;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getSexoShortString() { return gender == Gender.FEMININE ? "F" : "M"; }

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
