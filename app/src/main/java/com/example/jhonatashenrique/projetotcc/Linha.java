package com.example.jhonatashenrique.projetotcc;

import java.io.Serializable;

public class Linha implements Serializable {

    long id;
    String nomeLinha;
    String detalhes;
    String url;

    public Linha (long id, String nomeLinha, String detalhes, String url){
        super();
        this.id = id;
        this.nomeLinha = nomeLinha;
        this.detalhes = detalhes;
        this.url = url;
    }
    public Linha (){ }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNomeLinha() {
        return nomeLinha;
    }

    public void setNomeLinha(String nomeLinha) {
        this.nomeLinha = nomeLinha;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    @Override
    public String toString() {
        return "Linha: " + nomeLinha +" | Detalhes: " + detalhes;
    }
}
