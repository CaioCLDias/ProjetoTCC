package com.example.jhonatashenrique.projetotcc;

/**
 * Created by JhonatasHenrique on 18/10/2017.
 */

public class Coord {
    private int id;
    private double coordenadas;
    private double segundo;

    public Coord(int id, double coordenadas, double segundo) {
        super();
        this.id = id;
        this.coordenadas = coordenadas;
        this.segundo = segundo;
    }

    public Coord() {    }

    public double getSegundo() {
        return segundo;
    }

    public void setSegundo(double segundo) {
        this.segundo = segundo;
    }

    @Override
    public String toString() {
        return "" + coordenadas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(double coordenadas) {
        this.coordenadas = coordenadas;
    }


}
