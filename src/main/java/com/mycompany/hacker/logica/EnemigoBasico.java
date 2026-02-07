package com.mycompany.hacker.logica;

public class EnemigoBasico extends Enemigo {

    public EnemigoBasico(int x, int y) {
        super(x, y);
    }

    @Override
    public void actuar(Posicion objetivo, JuegoModelo modelo) {
        if (estaCongelado()) {
            turnosCongelado--;
            return;
        }

        if (objetivo == null) {
            int dir = new java.util.Random().nextInt(4);
            int dx = 0, dy = 0;
            if (dir == 0) {
                dy = -1;
            } else if (dir == 1) {
                dy = 1;
            } else if (dir == 2) {
                dx = -1;
            } else {
                dx = 1;
            }
            intentarMover(dx, dy, modelo);
        } else {
            moverHaciaObjetivo(objetivo, modelo);
        }
    }
}
