package com.mycompany.hacker.logica;

import java.util.Random;

public class Enemigo {

    private Posicion pos;
    private Random random;

    private int rangoVision = 5;
    private double inteligencia = 0.8;
    private int turnosCongelado = 0;

    public Enemigo(int x, int y) {
        this.pos = new Posicion(x, y);
        this.random = new Random();
    }

    public Posicion getPos() {
        return pos;
    }

    public void moverHacia(Posicion objetivo, JuegoModelo modelo) {
        if (turnosCongelado > 0) {
            turnosCongelado--;
            return;
        }

        // 1. Calcular distancia al jugador
        int distancia = Math.abs(pos.x - objetivo.x) + Math.abs(pos.y - objetivo.y);

        // 2. DECISIÓN DE IA
        if (distancia <= rangoVision) {
            // ESTADO: PERSECUCIÓN
            if (random.nextDouble() < inteligencia) {
                // Movimiento inteligente (hacia el jugador)
                intentarAcercarse(objetivo, modelo);
            } else {
                // El enemigo se "confunde" y se mueve aleatoriamente aunque te vea
                moverAleatoriamente(modelo);
            }
        } else {
            // ESTADO: PATRULLA / VAGAR
            moverAleatoriamente(modelo);
        }
    }

    private void intentarAcercarse(Posicion objetivo, JuegoModelo modelo) {
        int dx = 0;
        int dy = 0;

        if (pos.x < objetivo.x) {
            dx = 1;
        } else if (pos.x > objetivo.x) {
            dx = -1;
        }

        if (pos.y < objetivo.y) {
            dy = 1;
        } else if (pos.y > objetivo.y) {
            dy = -1;
        }

        boolean moverEnX = random.nextBoolean();

        if (dx != 0 && moverEnX) {
            if (intentarMover(dx, 0, modelo)) {
                return;
            }
        }

        if (dy != 0) {
            if (intentarMover(0, dy, modelo)) {
                return;
            }
        }

        if (dx != 0 && !moverEnX) {
            intentarMover(dx, 0, modelo);
        }
    }

    private void moverAleatoriamente(JuegoModelo modelo) {
        int direccion = random.nextInt(4);
        int dx = 0, dy = 0;

        switch (direccion) {
            case 0:
                dy = -1;
                break;
            case 1:
                dy = 1;
                break;
            case 2:
                dx = -1;
                break;
            case 3:
                dx = 1;
                break;
        }

        intentarMover(dx, dy, modelo);
    }

    private boolean intentarMover(int dx, int dy, JuegoModelo modelo) {
        int nuevaX = pos.x + dx;
        int nuevaY = pos.y + dy;

        if (modelo.esCeldaValidaParaEnemigo(nuevaX, nuevaY)) {
            pos.x = nuevaX;
            pos.y = nuevaY;
            return true;
        }
        return false;
    }

    public void congelar(int turnos) {
        this.turnosCongelado = turnos;
    }

    public boolean estaCongelado() {
        return turnosCongelado > 0;
    }
}
