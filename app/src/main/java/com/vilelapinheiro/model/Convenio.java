package com.vilelapinheiro.model;

import java.io.Serializable;

public enum Convenio implements Serializable {
    UNIMED("Unimed"), AMIL("Amil"), COOPERMED("Coopermed"), CASSI("Cassi");

    private String name;

    private Convenio(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
