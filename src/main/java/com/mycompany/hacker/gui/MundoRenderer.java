package com.mycompany.hacker.gui;

import com.mycompany.hacker.logica.*;
import java.awt.*;

public class MundoRenderer {

    public void dibujarTablero(Graphics2D g2, JuegoModelo modelo, int celdaSize) {
        for (int i = 0; i < modelo.getFilas(); i++) {
            for (int j = 0; j < modelo.getColumnas(); j++) {
                int x = j * celdaSize;
                int y = i * celdaSize;

                // Fondo de la celda
                g2.setColor(Color.BLACK);
                g2.drawRect(x, y, celdaSize, celdaSize);

                int contenido = modelo.getTablero()[i][j];
                dibujarContenidoCelda(g2, contenido, x, y, celdaSize, modelo);
            }
        }
        dibujarSonar(g2, modelo.getPosicionPing(), celdaSize);
    }

    private void dibujarContenidoCelda(Graphics2D g2, int tipo, int x, int y, int size, JuegoModelo modelo) {
        switch (tipo) {
            case JuegoModelo.PARED -> {
                g2.setColor(Color.GRAY);
                g2.fillRect(x + 1, y + 1, size - 1, size - 1);
            }
            case JuegoModelo.SALIDA -> {
                boolean abierta = modelo.getItemsRecogidos() >= modelo.getItemsTotales();
                g2.setColor(abierta ? Color.GREEN : new Color(100, 0, 0));
                g2.fillRect(x + 2, y + 2, size - 4, size - 4);
                if (size > 20) {
                    g2.setColor(Color.WHITE);
                    g2.drawString("EXIT", x + 5, y + size / 2);
                }
            }
            case JuegoModelo.ITEM -> {
                g2.setColor(Color.CYAN);
                g2.fillOval(x + size / 4, y + size / 4, size / 2, size / 2);
            }
            case JuegoModelo.EXPLOIT_VIRUS -> {
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2));
                g2.drawRect(x + 10, y + 10, size - 20, size - 20);
                g2.setColor(Color.RED);
                g2.fillOval(x + size / 3, y + size / 3, size / 3, size / 3);
            }
        }
    }

    public void dibujarEntidades(Graphics2D g2, JuegoModelo modelo, int size) {
        // Jugador
        Posicion p = modelo.getJugador();
        dibujarAvatar(g2, p.x * size, p.y * size, size, Color.GREEN, true);

        // Enemigos
        for (Enemigo e : modelo.getEnemigos()) {
            Color c = Color.RED;
            int offset = 8;

            if (e.estaCongelado()) {
                c = new Color(0, 150, 255);
            } else if (e instanceof EnemigoCorredor) {
                c = Color.ORANGE;
                offset = 10;
            } else if (e instanceof EnemigoTanque) {
                c = Color.MAGENTA;
                offset = 4;
            }

            dibujarAvatar(g2, e.getPos().x * size, e.getPos().y * size, size, c, false);
        }
    }

    private void dibujarAvatar(Graphics2D g2, int x, int y, int size, Color c, boolean esJugador) {
        g2.setColor(c);
        if (esJugador) {
            g2.fillRect(x + 6, y + 6, size - 12, size - 12);
            g2.setColor(Color.WHITE);
            g2.drawRect(x + 10, y + 10, size - 20, size - 20);
        } else {
            g2.fillOval(x + 8, y + 8, size - 16, size - 16);
        }
    }

    private void dibujarSonar(Graphics2D g2, Posicion ping, int size) {
        if (ping == null) {
            return;
        }
        int x = ping.x * size;
        int y = ping.y * size;
        g2.setColor(new Color(255, 255, 0, 100));
        g2.setStroke(new BasicStroke(3));
        g2.drawOval(x + 2, y + 2, size - 4, size - 4);
    }

    public void dibujarPantallaFin(Graphics2D g2, JuegoModelo modelo, int sw, int sh) {
        g2.setColor(new Color(0, 0, 0, 235));
        g2.fillRect(0, 0, sw, sh);

        boolean victoria = modelo.getItemsRecogidos() >= modelo.getItemsTotales() && !modelo.getMensajeFin().contains("FIREWALL");
        Color colorPrincipal = victoria ? Color.GREEN : Color.RED;
        String titulo = victoria ? "--- ACCESO CONCEDIDO ---" : "--- CONEXIÓN TERMINADA ---";

        java.util.Random rnd = new java.util.Random(modelo.getMensajeFin().hashCode());
        String ipAleatoria = (rnd.nextInt(150) + 100) + "." + rnd.nextInt(255) + "." + rnd.nextInt(255) + "." + rnd.nextInt(255);
        String macAleatoria = String.format("%02X:%02X:%02X:%02X:%02X:%02X",
                rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256),
                rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        int puerto = rnd.nextInt(9000) + 1000;

        g2.setFont(new Font("Monospaced", Font.BOLD, 35));
        FontMetrics fm = g2.getFontMetrics();
        g2.setColor(colorPrincipal);
        g2.drawString(titulo, (sw - fm.stringWidth(titulo)) / 2, sh / 2 - 60);

        g2.setFont(new Font("Monospaced", Font.PLAIN, 18));
        g2.setColor(Color.WHITE);
        String log = "LOG_EVENT: " + modelo.getMensajeFin();
        fm = g2.getFontMetrics();
        g2.drawString(log, (sw - fm.stringWidth(log)) / 2, sh / 2 - 10);

        g2.setFont(new Font("Monospaced", Font.PLAIN, 14));
        g2.setColor(victoria ? Color.CYAN : Color.ORANGE);

        String infoIP = "TRACE_IP  : " + ipAleatoria + ":" + puerto;
        String infoMAC = "ADDR_MAC  : " + macAleatoria;
        String status = victoria ? "ENCRYPTION: TUNNEL STABLE" : "PROTOCOL  : TRACE COMPLETE (100%)";

        int yInicioInfo = sh / 2 + 30;
        fm = g2.getFontMetrics();
        g2.drawString(infoIP, (sw - fm.stringWidth(infoIP)) / 2, yInicioInfo);
        g2.drawString(infoMAC, (sw - fm.stringWidth(infoMAC)) / 2, yInicioInfo + 20);
        g2.drawString(status, (sw - fm.stringWidth(status)) / 2, yInicioInfo + 40);

        g2.setFont(new Font("Monospaced", Font.BOLD, 16));
        g2.setColor(Color.WHITE);
        String subMsg = ">> PRESIONA [ESPACIO] PARA REINTENTAR EL VÍNCULO";
        fm = g2.getFontMetrics();
        g2.drawString(subMsg, (sw - fm.stringWidth(subMsg)) / 2, sh / 2 + 120);

        g2.setColor(colorPrincipal);
        g2.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[]{10, 5}, 0)); // Línea discontinua
        g2.drawRect(60, sh / 2 - 110, sw - 120, 260);
    }

}
