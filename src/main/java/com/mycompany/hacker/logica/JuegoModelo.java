package com.mycompany.hacker.logica;

import com.mycompany.hacker.logica.Enemigo;
import java.util.ArrayList;
import java.util.Random;

public class JuegoModelo {

    // Definición de constantes para el tablero
    public static final int VACIO = 0;
    public static final int PARED = 1;
    public static final int ITEM = 2;
    public static final int SALIDA = 3;

    private int[][] tablero;
    private int filas;
    private int columnas;
    private Posicion jugador;
    private ArrayList<Enemigo> enemigos;

    private int itemsTotales;
    private int itemsRecogidos;
    private boolean juegoTerminado;
    private boolean victoria;
    private String mensajeFin;

    public JuegoModelo(int f, int c) {
        this.filas = f;
        this.columnas = c;
        generarNivel();
    }

    private void generarNivel() {
        tablero = new int[filas][columnas];
        enemigos = new ArrayList<>();
        itemsRecogidos = 0;
        itemsTotales = 0;
        juegoTerminado = false;
        Random rand = new Random();

        // 1. Llenar tablero (Paredes aleatorias ~20%)
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (rand.nextDouble() < 0.2) {
                    tablero[i][j] = PARED;
                } else {
                    tablero[i][j] = VACIO;
                }
            }
        }

        // 2. Colocar Jugador (asegurar que no sea pared)
        jugador = new Posicion(0, 0);
        tablero[0][0] = VACIO;

        // 3. Colocar Salida (lejos del inicio)
        tablero[filas - 1][columnas - 1] = SALIDA;

        // 4. Colocar Ítems y Enemigos
        int numItems = (filas * columnas) / 10; // 10% del mapa son items
        int numEnemigos = (filas * columnas) / 15; // Un poco menos de enemigos

        colocarObjetoAleatorio(ITEM, numItems);
        spawnEnemigos(numEnemigos);

        // Contar items reales generados
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (tablero[i][j] == ITEM) {
                    itemsTotales++;
                }
            }
        }
    }

    private void colocarObjetoAleatorio(int tipo, int cantidad) {
        Random rand = new Random();
        int colocados = 0;
        while (colocados < cantidad) {
            int r = rand.nextInt(filas);
            int c = rand.nextInt(columnas);
            // Evitar inicio, fin y paredes
            if (tablero[r][c] == VACIO && (r != 0 || c != 0)) {
                tablero[r][c] = tipo;
                colocados++;
            }
        }
    }

    private void spawnEnemigos(int cantidad) {
        Random rand = new Random();
        int colocados = 0;
        while (colocados < cantidad) {
            int r = rand.nextInt(filas);
            int c = rand.nextInt(columnas);
            // Evitar estar muy cerca del jugador al inicio
            if (tablero[r][c] == VACIO && (r + c > 4)) {
                enemigos.add(new Enemigo(r, c));
                colocados++;
            }
        }
    }

    // --- LÓGICA DEL TURNO ---
    public void procesarTurnoJugador(int dx, int dy) {
        if (juegoTerminado) {
            return;
        }

        int nuevaX = jugador.x + dx;
        int nuevaY = jugador.y + dy;

        // 1. Validar Movimiento Jugador (Límites y Paredes)
        if (nuevaX >= 0 && nuevaX < columnas && nuevaY >= 0 && nuevaY < filas) {
            if (tablero[nuevaY][nuevaX] != PARED) {
                jugador.x = nuevaX;
                jugador.y = nuevaY;

                // Chequear eventos de casilla
                chequearCasilla();
            }
        }

        // Si el juego terminó por tocar la salida o morir, paramos
        if (juegoTerminado) {
            return;
        }

        // 2. Turno de Enemigos
        moverEnemigos();

        // 3. Chequear colisión post-movimiento enemigos
        chequearColisionEnemigos();
    }

    private void chequearCasilla() {
        int celda = tablero[jugador.y][jugador.x];

        if (celda == ITEM) {
            itemsRecogidos++;
            tablero[jugador.y][jugador.x] = VACIO; // Borrar item
        } else if (celda == SALIDA) {
            if (itemsRecogidos >= itemsTotales) {
                juegoTerminado = true;
                victoria = true;
                mensajeFin = "¡HACKEO COMPLETADO!";
            }
        }
    }

    private void moverEnemigos() {
        for (Enemigo e : enemigos) {
            e.moverHacia(jugador, this);
        }
    }

    private void chequearColisionEnemigos() {
        for (Enemigo e : enemigos) {
            if (e.getPos().equals(jugador)) {
                juegoTerminado = true;
                victoria = false;
                mensajeFin = "¡TE HA ATRAPADO EL FIREWALL!";
            }
        }
    }

    public boolean esCeldaValidaParaEnemigo(int x, int y) {
        // Límites
        if (x < 0 || x >= columnas || y < 0 || y >= filas) {
            return false;
        }
        // Paredes
        if (tablero[y][x] == PARED) {
            return false;
        }
        // Salida (Opcional: los enemigos no deberían bloquear la salida)
        if (tablero[y][x] == SALIDA) {
            return false;
        }

        // Evitar que dos enemigos ocupen la misma celda
        for (Enemigo e : enemigos) {
            if (e.getPos().x == x && e.getPos().y == y) {
                return false;
            }
        }
        return true;
    }

    // Getters para la Vista
    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public int[][] getTablero() {
        return tablero;
    }

    public Posicion getJugador() {
        return jugador;
    }

    public ArrayList<Enemigo> getEnemigos() {
        return enemigos;
    }

    public int getItemsRecogidos() {
        return itemsRecogidos;
    }

    public int getItemsTotales() {
        return itemsTotales;
    }

    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    public String getMensajeFin() {
        return mensajeFin;
    }
}
