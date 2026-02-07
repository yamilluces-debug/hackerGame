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

    private Posicion posicionPing = null;
    private int turnosPingRestantes = 0;
    private int cooldownDash = 0;
    private final int MAX_COOLDOWN_DASH = 5;

    public static final int EXPLOIT_VIRUS = 4;

    private int heatLevel = 1; // 1: Sigilo, 2: Sospecha, 3: Brecha
    private int virusInventario = 0;
    private boolean refuerzosEnviados = false;

    private int monedasTotales;
    private int monedasGanadasEnPartida;
    private boolean progresoGuardado = false;

    private int duracionEMP;
    private int maxCooldownDash;
    private boolean tieneEscudo;

    public JuegoModelo(int f, int c) {
        this.filas = f;
        this.columnas = c;
        int[] datos = PersistenciaDatos.cargarProgreso();
        this.monedasTotales = datos[0];
        this.duracionEMP = datos[1];
        this.maxCooldownDash = datos[2];
        this.tieneEscudo = datos[3] == 1;
        this.mensajeFin = "";
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
        int numItems = (filas * columnas) / 8;
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

        while (colocados < cantidad && intentos < 2000) {
            int r = rand.nextInt(filas);
            int c = rand.nextInt(columnas);

            if (tablero[r][c] == VACIO && (r != 0 || c != 0)) {
                if (!hayObjetoAdyacente(r, c)) {
                    tablero[r][c] = tipo;
                    colocados++;
                }
            }
            intentos++;
        }
    }

    private boolean hayObjetoAdyacente(int r, int c) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int nr = r + i;
                int nc = c + j;

                if (nr >= 0 && nr < filas && nc >= 0 && nc < columnas) {
                    int contenido = tablero[nr][nc];
                    if (contenido == ITEM || contenido == EXPLOIT_VIRUS) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void spawnEnemigos(int cantidad) {
        Random rand = new Random();
        int colocados = 0;
        int intentos = 0;

        while (colocados < cantidad && intentos < 1000) {
            int r = rand.nextInt(filas);
            int c = rand.nextInt(columnas);

            if (tablero[r][c] == VACIO && (r + c > 4)) {
                int probabilidad = rand.nextInt(100); // Rango de 0 a 99

                if (probabilidad < 60) {
                    // 60% de las veces: Básico
                    enemigos.add(new EnemigoBasico(c, r));
                } else if (probabilidad < 85) {
                    // 25% de las veces: Tanque
                    enemigos.add(new EnemigoTanque(c, r));
                } else {
                    // 15% de las veces: Corredor
                    enemigos.add(new EnemigoCorredor(c, r));
                }

                colocados++;
            }
            intentos++;
        }
    }

    private void moverEnemigos() {
        Posicion objetivoEfectivo = (posicionPing != null) ? posicionPing : jugador;

        for (Enemigo e : enemigos) {
            if (posicionPing != null) {
                // Si hay ping, todos van al ping sin importar el Heat
                e.actuar(posicionPing, this);
                continue;
            }

            // Comportamiento por Heat Level
            switch (heatLevel) {
                case 1 -> {
                    // En sigilo, solo patrullan al azar (pasamos null como objetivo)
                    e.actuar(null, this);
                }
                case 2 -> {
                    // Solo los Básicos te siguen, los demás patrullan
                    if (e instanceof EnemigoBasico) {
                        e.actuar(jugador, this);
                    } else {
                        e.actuar(null, this);
                    }
                }
                case 3 -> {
                    // Todos a por ti
                    e.actuar(jugador, this);
                }
            }
        }
    }

    private void chequearCasilla() {
        int celda = tablero[jugador.y][jugador.x];
        if (celda == ITEM) {
            itemsRecogidos++;
            tablero[jugador.y][jugador.x] = VACIO;
            actualizarHeatLevel(); // <--- IMPORTANTE
        } else if (celda == EXPLOIT_VIRUS) {
            virusInventario++;
            tablero[jugador.y][jugador.x] = VACIO;
            mensajeSistema = "VIRUS ADQUIRIDO (Presiona Q).";
        } else if (celda == SALIDA && itemsRecogidos >= itemsTotales) {
            juegoTerminado = true;
            victoria = true;
            mensajeFin = "¡HACKEO EXITOSO!";
            finalizarPartida();
        }
    }

    /*public void activarHabilidadEspecial() {
        if (cooldownHabilidad == 0) {
            for (Enemigo e : enemigos) {
                e.congelar(duracionEMP); // USA LA MEJORA
            }
            cooldownHabilidad = 10;
        }
    }*/
    public void activarHabilidadEspecial() {
        if (juegoTerminado) {
            return;
        }

        if (cooldownHabilidad == 0) {
            for (Enemigo e : enemigos) {
                e.congelar(duracionEMP);
            }
            cooldownHabilidad = MAX_COOLDOWN;
            mensajeSistema = "¡EMP LANZADO! ENEMIGOS CONGELADOS.";
        } else {
            mensajeSistema = "HABILIDAD EN RECARGA... (" + cooldownHabilidad + ")";
        }
    }

    public int getHeatLevel() {
        return heatLevel;
    }

    public int getVirusInventario() {
        return virusInventario;
    }

    public boolean hayLineaDeVision(Posicion p1, Posicion p2) {

        return true;
    }

    public void notificarDisparoTorreta() {
        juegoTerminado = true;
        victoria = false;
        mensajeFin = "¡ELIMINADO POR SNIPER!";
    }

    public void procesarTurnoJugador(int dx, int dy, boolean esDash) {
        if (juegoTerminado) {
            finalizarPartida();
            return;
        }

        mensajeSistema = "";
        int multiplicador = 1;

        if (esDash) {
            if (cooldownDash == 0) {
                multiplicador = 2;
                cooldownDash = MAX_COOLDOWN_DASH;
                mensajeSistema = "¡OVERCLOCK ACTIVADO!";
            } else {
                mensajeSistema = "DASH EN ENFRIAMIENTO (" + cooldownDash + ")";
                return;
            }
        }

        int nuevaX = jugador.x + (dx * multiplicador);
        int nuevaY = jugador.y + (dy * multiplicador);

        if (nuevaX >= 0 && nuevaX < columnas && nuevaY >= 0 && nuevaY < filas) {
            if (tablero[nuevaY][nuevaX] != PARED) {
                jugador.x = nuevaX;
                jugador.y = nuevaY;
                chequearCasilla();
                finalizarTurno();
            }
        }

    }

    public void lanzarPing(int dx, int dy) {
        if (juegoTerminado || posicionPing != null) {
            return;
        }

        int px = jugador.x + (dx * 3);
        int py = jugador.y + (dy * 3);

        px = Math.max(0, Math.min(columnas - 1, px));
        py = Math.max(0, Math.min(filas - 1, py));

        posicionPing = new Posicion(px, py);
        turnosPingRestantes = 4;

        mensajeSistema = "SEÑUELO ENVIADO A [" + px + "," + py + "]";

        finalizarTurno();
    }

    private void finalizarTurno() {
        // Decrementar cooldowns
        if (cooldownDash > 0) {
            cooldownDash--;
        }
        if (cooldownHabilidad > 0) {
            cooldownHabilidad--;
        }
        // Manejar duración del Ping
        if (turnosPingRestantes > 0) {
            turnosPingRestantes--;
            if (turnosPingRestantes == 0) {
                posicionPing = null;
            }
        }

        moverEnemigos();
        chequearColisionEnemigos();
    }

    private void finalizarPartida() {
        if (progresoGuardado) {
            return;
        }

        monedasGanadasEnPartida = itemsRecogidos * 10;
        if (victoria) {
            monedasGanadasEnPartida += 10;
        }

        monedasTotales += monedasGanadasEnPartida;

        int escudoGuardar = tieneEscudo ? 1 : 0;

        PersistenciaDatos.guardarProgreso(
                monedasTotales,
                duracionEMP,
                maxCooldownDash,
                escudoGuardar
        );

        progresoGuardado = true;
    }

    public int getCooldown() {
        return cooldownHabilidad;
    }

    public String getMensajeSistema() {
        return mensajeSistema;
    }

    public Posicion getPosicionPing() {
        return posicionPing;
    }

    public int getCooldownDash() {
        return cooldownDash;
    }

    private void chequearColisionEnemigos() {
        for (Enemigo e : enemigos) {
            if (e.getPos().equals(jugador)) {
                if (tieneEscudo) {
                    tieneEscudo = false;
                    PersistenciaDatos.guardarProgreso(monedasTotales, duracionEMP, maxCooldownDash, 0);
                    mensajeSistema = "¡ESCUDO ACTIVADO! FIREWALL BLOQUEADO.";
                    e.congelar(2);
                } else {
                    juegoTerminado = true;
                    victoria = false;
                    finalizarPartida();
                }
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

    private void actualizarHeatLevel() {
        double progreso = (double) itemsRecogidos / itemsTotales;

        if (progreso >= 1.0 && heatLevel < 3) {
            heatLevel = 3;
            mensajeSistema = "¡ALERTA MÁXIMA! SISTEMA COMPROMETIDO.";
            generarRefuerzos();
        } else if (progreso >= 0.5 && heatLevel < 2) {
            heatLevel = 2;
            mensajeSistema = "SISTEMA EN ESTADO DE SOSPECHA.";
        }
    }

    private void generarRefuerzos() {
        if (refuerzosEnviados) {
            return;
        }
        Random rand = new Random();
        int creados = 0;
        // Intentar crear 2 enemigos cerca de la salida (filas-1, columnas-1)
        while (creados < 2) {
            int r = filas - 1 - rand.nextInt(3);
            int c = columnas - 1 - rand.nextInt(3);
            if (tablero[r][c] == VACIO && (r != jugador.y || c != jugador.x)) {
                enemigos.add(new EnemigoCorredor(c, r)); // Refuerzos rápidos
                creados++;
            }
        }
        refuerzosEnviados = true;
    }

    public void usarVirusSobrecarga() {
        if (virusInventario <= 0 || enemigos.isEmpty()) {
            return;
        }

        Enemigo masCercano = null;
        double distMin = Double.MAX_VALUE;

        for (Enemigo e : enemigos) {
            double d = Math.sqrt(Math.pow(e.getPos().x - jugador.x, 2) + Math.pow(e.getPos().y - jugador.y, 2));
            if (d < distMin) {
                distMin = d;
                masCercano = e;
            }
        }

        if (masCercano != null) {
            enemigos.remove(masCercano);
            virusInventario--;
            mensajeSistema = "VIRUS EJECUTADO: ENEMIGO ELIMINADO.";

        }
    }

    // Getters
    public int getFilas() {
        return filas;
    }

    public int getMonedasTotales() {
        return monedasTotales;
    }

    public int getMonedasGanadas() {
        return monedasGanadasEnPartida;
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
