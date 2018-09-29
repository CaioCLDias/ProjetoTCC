package com.example.jhonatashenrique.projetotcc;

import java.io.Serializable;

public class Pontos implements Serializable {

    long id, id_linha;
    String nome, detalhesPonto;
    double latitude, longitude;

    public Pontos (){ }

    public Pontos(int id){

    }

    public Pontos (long id, long id_linha, String nome, String detalhesPonto, double latitude, double longitude){
        super();
        this.id = id;
        this.id_linha = id_linha;
        this.nome = nome;
        this.detalhesPonto = detalhesPonto;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId_linha() {
        return id_linha;
    }

    public void setId_linha(long id_linha) {
        this.id_linha = id_linha;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDetalhesPonto() {
        return detalhesPonto;
    }

    public void setDetalhesPonto(String detalhesPonto) {
        this.detalhesPonto = detalhesPonto;
    }

    @Override
    public String toString(){
        return "Nome: "+nome +"| Detalhes: "+detalhesPonto +"| Latitude: "+latitude +", Longitude: "+longitude +"|";
    }
}
