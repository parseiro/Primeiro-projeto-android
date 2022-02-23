package com.vilelapinheiro.model;

import java.io.Serializable;

public enum MedicalPlan implements Serializable {
    UNIMED("Unimed"), AMIL("Amil"), COOPERMED("Coopermed"), CASSI("Cassi");

    private final String name;

    MedicalPlan(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
