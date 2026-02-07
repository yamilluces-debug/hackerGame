package com.mycompany.hacker.logica;

public class EnemigoTorreta extends Enemigo {

    public EnemigoTorreta(int x, int y) {
        super(x, y);
    }

    @Override
    public void actuar(Posicion objetivo, JuegoModelo modelo) {
        if (estaCongelado()) {
            turnosCongelado--;
            return;
        }

        if (pos.x == objetivo.x || pos.y == objetivo.y) {
            if (modelo.hayLineaDeVision(pos, objetivo)) {
                modelo.notificarDisparoTorreta();
            }
        }
    }
}
