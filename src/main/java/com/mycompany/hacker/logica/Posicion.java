package com.mycompany.hacker.logica;

public class Posicion {

    public int x;
    public int y;

    public Posicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Posicion posicion = (Posicion) o;
        return x == posicion.x && y == posicion.y;
    }
}
