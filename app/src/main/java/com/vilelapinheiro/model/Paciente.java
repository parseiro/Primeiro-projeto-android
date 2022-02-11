package com.vilelapinheiro.model;

public class Paciente {
    private String nomeCompleto;

    private Sexo sexo;

    private Convenio convenio;

    boolean concordaPesquisas;

    public Paciente(String nomeCompleto, Sexo sexo, Convenio convenio, boolean concordaPesquisas) {
        this.nomeCompleto = nomeCompleto;
        this.sexo = sexo;
        this.convenio = convenio;
        this.concordaPesquisas = concordaPesquisas;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public Convenio getConvenio() {
        return convenio;
    }

    public boolean isConcordaPesquisas() {
        return concordaPesquisas;
    }

    @Override
    public String toString() {
        return nomeCompleto;
    }
}
