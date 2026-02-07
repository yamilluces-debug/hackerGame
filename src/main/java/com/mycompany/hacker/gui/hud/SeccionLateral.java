package com.mycompany.hacker.gui.hud;

import com.mycompany.hacker.logica.*;
import java.awt.*;

public class SeccionLateral implements HudComponent {

    public void dibujar(Graphics2D g2, JuegoModelo modelo, int sw, int sh, int celdaSize) {
        int xInicioTablero = modelo.getColumnas() * celdaSize;
        int x = xInicioTablero + 25;
        int y = 40;

        g2.setColor(new Color(20, 20, 20)); // Casi negro
        g2.fillRect(xInicioTablero, 0, sw - xInicioTablero, sh - 50);

        g2.setColor(Color.GREEN);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(xInicioTablero, 0, xInicioTablero, sh - 50);

        g2.setFont(new Font("Monospaced", Font.BOLD, 18));
        g2.drawString("--- SISTEMA ---", x, y);

        y += 40;
        dibujarAlerta(g2, modelo, x, y);

        y += 70;
        dibujarHabilidades(g2, modelo, x, y);
    }

    private void dibujarAlerta(Graphics2D g2, JuegoModelo modelo, int x, int y) {
        String[] nombres = {"SIGILO", "SOSPECHA", "¡BRECHA!"};
        Color[] colores = {Color.GRAY, Color.ORANGE, Color.RED};
        int lvl = modelo.getHeatLevel() - 1;

        g2.setFont(new Font("Monospaced", Font.BOLD, 14));
        g2.setColor(Color.WHITE);
        g2.drawString("RIESGO:", x, y);

        // Brillo para el nivel de riesgo
        g2.setColor(colores[lvl]);
        g2.setFont(new Font("Monospaced", Font.BOLD, 18));
        g2.drawString(">> " + nombres[lvl], x, y + 25);
    }

    private void dibujarHabilidades(Graphics2D g2, JuegoModelo modelo, int x, int y) {
        g2.setFont(new Font("Monospaced", Font.BOLD, 13));
        g2.setColor(Color.CYAN);
        g2.drawString("PROTOCOLOS ACTIVOS:", x, y);

        int espaciado = 28;

        // EMP (Espacio)
        dibujarEstadoHabilidad(g2, "EMP  [SPACE]", modelo.getCooldown(), x, y + espaciado);

        // DASH (Shift)
        dibujarEstadoHabilidad(g2, "DASH [SHIFT]", modelo.getCooldownDash(), x, y + (espaciado * 2));

        // PING (P o Ctrl)
        boolean pingActivo = modelo.getPosicionPing() != null;
        g2.setColor(pingActivo ? Color.ORANGE : Color.GREEN);
        g2.drawString("SONAR  [P]  : " + (pingActivo ? "ACTIVO" : "LISTO"), x, y + (espaciado * 3));

        // --- SECCIÓN DE INVENTARIO (VIRUS) ---
        int yInv = y + (espaciado * 5);
        g2.setColor(Color.WHITE);
        g2.drawString("EXPLOITS DISPONIBLES:", x, yInv);

        int numVirus = modelo.getVirusInventario();
        g2.setColor(numVirus > 0 ? Color.RED : Color.DARK_GRAY);
        g2.drawString("> VIRUS [Q] : x" + numVirus, x, yInv + 22);
    }

    private void dibujarEstadoHabilidad(Graphics2D g2, String nombre, int cooldown, int x, int y) {
        if (cooldown == 0) {
            g2.setColor(Color.GREEN);
            g2.drawString(nombre + ": LISTO", x, y);
        } else {
            g2.setColor(Color.RED);
            g2.drawString(nombre + ": " + cooldown + "s", x, y);

            // Dibujamos una pequeña barra de carga visual debajo del texto
            g2.setColor(new Color(100, 0, 0));
            g2.fillRect(x, y + 6, 100, 4); // Fondo de la barra
            g2.setColor(Color.RED);
            // La barra se llena según el cooldown (suponiendo max cooldown de 10)
            int ancho = Math.max(0, 100 - (cooldown * 10));
            g2.fillRect(x, y + 6, ancho, 4);
        }
    }
}
