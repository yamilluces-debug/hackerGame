package com.mycompany.hacker.logica;

import java.util.Random;

public abstract class Enemigo {

    protected Posicion pos;
    protected Random random = new Random();
    protected int turnosCongelado = 0;

    public Enemigo(int x, int y) {
        this.pos = new Posicion(x, y);
    }

    public Posicion getPos() {
        return pos;
    }

    public void congelar(int turnos) {
        this.turnosCongelado = turnos;
    }

    public boolean estaCongelado() {
        return turnosCongelado > 0;
    }

    public abstract void actuar(Posicion objetivo, JuegoModelo modelo);

    protected void moverHaciaObjetivo(Posicion objetivo, JuegoModelo modelo) {
        int dx = (pos.x < objetivo.x) ? 1 : (pos.x > objetivo.x) ? -1 : 0;
        int dy = (pos.y < objetivo.y) ? 1 : (pos.y > objetivo.y) ? -1 : 0;

        if (random.nextBoolean() && dx != 0) {
            if (intentarMover(dx, 0, modelo)) {
                return;
            }
        }
        if (dy != 0) {
            intentarMover(0, dy, modelo);
        }
    }

    protected boolean intentarMover(int dx, int dy, JuegoModelo modelo) {
        int nx = pos.x + dx;
        int ny = pos.y + dy;
        if (modelo.esCeldaValidaParaEnemigo(nx, ny)) {
            pos.x = nx;
            pos.y = ny;
            return true;
        }
        return false;
    }
}
