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

        moverHaciaObjetivo(objetivo, modelo);
    }
}
