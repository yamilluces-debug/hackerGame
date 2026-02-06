package com.mycompany.hacker.logica;

public class Enemigo {

    private Posicion pos;

    public Enemigo(int x, int y) {
        this.pos = new Posicion(x, y);
    }

    public Posicion getPos() {
        return pos;
    }

    // Lógica simple de persecución: Intenta acercarse al jugador
    public void moverHacia(Posicion objetivo, JuegoModelo modelo) {
        int dx = 0;
        int dy = 0;

        // Decidir dirección en X
        if (pos.x < objetivo.x) {
            dx = 1;
        } else if (pos.x > objetivo.x) {
            dx = -1;
        }

        // Decidir dirección en Y (si no se mueve en X, o priorizar eje más lejano)
        if (dx == 0) {
            if (pos.y < objetivo.y) {
                dy = 1;
            } else if (pos.y > objetivo.y) {
                dy = -1;
            }
        }

        // Validar si la casilla destino es caminable (sin paredes ni otros enemigos)
        // Nota: Esta es una IA muy básica. Si choca con pared, pierde el turno.
        if (modelo.esCeldaValidaParaEnemigo(pos.x + dx, pos.y + dy)) {
            pos.x += dx;
            pos.y += dy;
        }
    }
}
