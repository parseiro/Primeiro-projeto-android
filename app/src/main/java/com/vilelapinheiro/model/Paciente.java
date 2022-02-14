package com.vilelapinheiro.model;

import java.io.Serializable;

public class Paciente implements Serializable {
    private long id;

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

    public Sexo getSexo() {
        return sexo;
    }

    public String getSexoShortString() { return sexo == Sexo.FEMININO ? "F" : "M"; }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public Convenio getConvenio() {
        return convenio;
    }

    public void setConvenio(Convenio convenio) {
        this.convenio = convenio;
    }

    public boolean isConcordaPesquisas() {
        return concordaPesquisas;
    }

    public void setConcordaPesquisas(boolean concordaPesquisas) {
        this.concordaPesquisas = concordaPesquisas;
    }

    @Override
    public String toString() {
        return nomeCompleto;
    }
}
