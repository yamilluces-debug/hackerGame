package com.mycompany.hacker.logica;

public class EnemigoTanque extends Enemigo {

    private boolean debeMoverse = false;

    public EnemigoTanque(int x, int y) {
        super(x, y);
    }

    @Override
    public void actuar(Posicion objetivo, JuegoModelo modelo) {
        if (estaCongelado()) {
            turnosCongelado--;
            return;
        }

        if (debeMoverse) {
            moverHaciaObjetivo(objetivo, modelo);
        }
        debeMoverse = !debeMoverse;
    }
}
