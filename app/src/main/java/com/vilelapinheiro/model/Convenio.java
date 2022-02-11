package com.vilelapinheiro.model;

public class Convenio {
    private String nomeConvenio;

    public Convenio(final String nomeConvenio) {
        this.nomeConvenio = nomeConvenio;
    }

    @Override
    public String toString() {
        return nomeConvenio;
    }
}
