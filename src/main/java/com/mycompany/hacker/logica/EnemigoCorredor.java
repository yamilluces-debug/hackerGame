package com.mycompany.hacker.logica;

public class EnemigoCorredor extends Enemigo {

    public EnemigoCorredor(int x, int y) {
        super(x, y);
    }

    @Override
    public void actuar(Posicion objetivo, JuegoModelo modelo) {
        if (estaCongelado()) {
            turnosCongelado--;
            return;
        }

        int dx = (pos.x < objetivo.x) ? 1 : (pos.x > objetivo.x) ? -1 : 0;
        int dy = (pos.y < objetivo.y) ? 1 : (pos.y > objetivo.y) ? -1 : 0;

        boolean intentarXPrimero = random.nextBoolean();

        if (intentarXPrimero) {
            if (dx != 0 && intentarDoblePaso(dx, 0, modelo)) {
                return;
            }
            if (dy != 0) {
                intentarDoblePaso(0, dy, modelo);
            }
        } else {
            if (dy != 0 && intentarDoblePaso(0, dy, modelo)) {
                return;
            }
            if (dx != 0) {
                intentarDoblePaso(dx, 0, modelo);
            }
        }
    }

    private boolean intentarDoblePaso(int dx, int dy, JuegoModelo modelo) {
        if (intentarMover(dx, dy, modelo)) {
            intentarMover(dx, dy, modelo);
            return true;
        }
        return false;
    }
}
