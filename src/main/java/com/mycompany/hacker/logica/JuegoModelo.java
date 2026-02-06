package com.mycompany.hacker.logica;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class JuegoModelo {

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

    private int cooldownHabilidad = 0;
    private final int MAX_COOLDOWN = 10;
    private String mensajeSistema = "";

    public JuegoModelo(int f, int c) {
        this.filas = f;
        this.columnas = c;
        generarNivel();
    }

    private void generarNivel() {
        tablero = new int[filas][columnas];
        Random rand = new Random();
        boolean mapaValido = false;

        // BUCLE DE GENERACIÓN: Sigue intentando hasta que salga un mapa conectado
        while (!mapaValido) {
            int celdasVaciasTotales = 0;
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    if ((i == 0 && j == 0) || (i == filas - 1 && j == columnas - 1)) {
                        tablero[i][j] = VACIO;
                        celdasVaciasTotales++;
                    } else {
                        if (rand.nextDouble() < 0.15) {
                            tablero[i][j] = PARED;
                        } else {
                            tablero[i][j] = VACIO;
                            celdasVaciasTotales++;
                        }
                    }
                }
            }

            // 2. Validar conectividad (Algoritmo BFS)
            if (esMapaConectado(celdasVaciasTotales)) {
                mapaValido = true;
            }
        }

        jugador = new Posicion(0, 0);
        tablero[filas - 1][columnas - 1] = SALIDA;

        enemigos = new ArrayList<>();
        itemsRecogidos = 0;
        itemsTotales = 0;
        juegoTerminado = false;

        // Colocar ítems y enemigos
        int numItems = (filas * columnas) / 10;
        int numEnemigos = (filas * columnas) / 15;

        colocarObjetoAleatorio(ITEM, numItems);
        spawnEnemigos(numEnemigos);

        // Recalcular items totales reales
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (tablero[i][j] == ITEM) {
                    itemsTotales++;
                }
            }
        }
    }

    private boolean esMapaConectado(int totalCeldasVaciasEsperadas) {
        boolean[][] visitado = new boolean[filas][columnas];
        Queue<Posicion> cola = new LinkedList<>();

        // Empezamos desde el jugador
        cola.add(new Posicion(0, 0));
        visitado[0][0] = true;
        int celdasAlcanzadas = 0;

        int[] dx = {0, 0, 1, -1};
        int[] dy = {1, -1, 0, 0};

        while (!cola.isEmpty()) {
            Posicion actual = cola.poll();
            celdasAlcanzadas++;

            // Mirar vecinos
            for (int i = 0; i < 4; i++) {
                int nx = actual.x + dx[i];
                int ny = actual.y + dy[i];

                // Verificar límites
                if (nx >= 0 && nx < columnas && ny >= 0 && ny < filas) {
                    // Si no es pared y no lo hemos visitado
                    if (tablero[ny][nx] != PARED && !visitado[ny][nx]) {
                        visitado[ny][nx] = true;
                        cola.add(new Posicion(nx, ny));
                    }
                }
            }
        }

        return celdasAlcanzadas == totalCeldasVaciasEsperadas;
    }

    private void colocarObjetoAleatorio(int tipo, int cantidad) {
        Random rand = new Random();
        int colocados = 0;
        int intentos = 0;
        // Límite de intentos para evitar bucles infinitos si el mapa está muy lleno
        while (colocados < cantidad && intentos < 1000) {
            int r = rand.nextInt(filas);
            int c = rand.nextInt(columnas);

            if (tablero[r][c] == VACIO && (r != 0 || c != 0)) {
                tablero[r][c] = tipo;
                colocados++;
            }
            intentos++;
        }
    }

    private void spawnEnemigos(int cantidad) {
        Random rand = new Random();
        int colocados = 0;
        int intentos = 0;
        while (colocados < cantidad && intentos < 1000) {
            int r = rand.nextInt(filas);
            int c = rand.nextInt(columnas);

            // Spawnear lejos del jugador (suma de coordenadas > 4)
            if (tablero[r][c] == VACIO && (r + c > 4)) {
                enemigos.add(new Enemigo(r, c));
                colocados++;
            }
            intentos++;
        }
    }

    public void procesarTurnoJugador(int dx, int dy) {
        if (juegoTerminado) {
            return;
        }

        mensajeSistema = "";

        int nuevaX = jugador.x + dx;
        int nuevaY = jugador.y + dy;

        if (nuevaX >= 0 && nuevaX < columnas && nuevaY >= 0 && nuevaY < filas) {
            if (tablero[nuevaY][nuevaX] != PARED) {
                jugador.x = nuevaX;
                jugador.y = nuevaY;
                chequearCasilla();

                if (cooldownHabilidad > 0) {
                    cooldownHabilidad--;
                }
            }
        }

        if (juegoTerminado) {
            return;
        }

        moverEnemigos();
        chequearColisionEnemigos();
    }

    public int getCooldown() {
        return cooldownHabilidad;
    }

    public String getMensajeSistema() {
        return mensajeSistema;
    }

    public void activarHabilidadEspecial() {
        if (juegoTerminado) {
            return;
        }

        if (cooldownHabilidad == 0) {
            for (Enemigo e : enemigos) {
                e.congelar(3);
            }
            cooldownHabilidad = MAX_COOLDOWN;
            mensajeSistema = "¡EMP LANZADO! ENEMIGOS CONGELADOS.";
        } else {
            mensajeSistema = "HABILIDAD EN RECARGA... (" + cooldownHabilidad + ")";
        }
    }

    private void chequearCasilla() {
        int celda = tablero[jugador.y][jugador.x];
        if (celda == ITEM) {
            itemsRecogidos++;
            tablero[jugador.y][jugador.x] = VACIO;
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
        if (x < 0 || x >= columnas || y < 0 || y >= filas) {
            return false;
        }
        if (tablero[y][x] == PARED) {
            return false;
        }
        if (tablero[y][x] == SALIDA) {
            return false;
        }
        for (Enemigo e : enemigos) {
            if (e.getPos().x == x && e.getPos().y == y) {
                return false;
            }
        }
        return true;
    }

    // Getters
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
