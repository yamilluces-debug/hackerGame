package com.mycompany.hacker.gui;

import com.mycompany.hacker.logica.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;

public class PanelJuego extends JPanel implements KeyListener {

    private JuegoModelo modelo;
    private MainVentana ventanaPrincipal;
    private int celdaSize = 40;

    public PanelJuego(MainVentana v) {
        this.ventanaPrincipal = v;
        this.setBackground(Color.DARK_GRAY);
        this.setFocusable(true);
        this.addKeyListener(this);
    }

    public void iniciarJuego(int filas, int cols) {
        modelo = new JuegoModelo(filas, cols);
        int altoDisp = getHeight() > 0 ? getHeight() : 600;
        int anchoDisp = getWidth() > 0 ? getWidth() : 800;
        if (filas > 0 && cols > 0) {
            celdaSize = Math.min((altoDisp - 50) / filas, anchoDisp / cols);
        }
        celdaSize = Math.max(celdaSize, 10);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (modelo == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Separación de responsabilidades de dibujo
        dibujarTablero(g2);
        dibujarEntidades(g2);
        dibujarHUD(g2);

        if (modelo.isJuegoTerminado()) {
            dibujarPantallaFin(g2);
        }
    }

    private void dibujarTablero(Graphics2D g2) {
        for (int i = 0; i < modelo.getFilas(); i++) {
            for (int j = 0; j < modelo.getColumnas(); j++) {
                int x = j * celdaSize;
                int y = i * celdaSize;

                g2.setColor(Color.BLACK);
                g2.drawRect(x, y, celdaSize, celdaSize);

                int contenido = modelo.getTablero()[i][j];
                switch (contenido) {
                    case JuegoModelo.PARED -> {
                        g2.setColor(Color.GRAY);
                        g2.fillRect(x + 1, y + 1, celdaSize - 1, celdaSize - 1);
                    }
                    case JuegoModelo.SALIDA -> {
                        boolean abierta = modelo.getItemsRecogidos() >= modelo.getItemsTotales();
                        g2.setColor(abierta ? Color.GREEN : new Color(100, 0, 0));
                        g2.fillRect(x + 2, y + 2, celdaSize - 4, celdaSize - 4);
                        if (celdaSize > 20) {
                            g2.setColor(Color.WHITE);
                            g2.drawString("EXIT", x + 5, y + celdaSize / 2);
                        }
                    }
                    case JuegoModelo.ITEM -> {
                        g2.setColor(Color.CYAN);
                        g2.fillOval(x + celdaSize / 4, y + celdaSize / 4, celdaSize / 2, celdaSize / 2);
                    }
                    case JuegoModelo.EXPLOIT_VIRUS -> {
                        g2.setColor(Color.WHITE);
                        g2.setStroke(new BasicStroke(2));
                        g2.drawRect(x + 10, y + 10, celdaSize - 20, celdaSize - 20);
                        g2.setColor(Color.RED);
                        g2.fillOval(x + celdaSize / 3, y + celdaSize / 3, celdaSize / 3, celdaSize / 3);
                    }
                }
            }
        }

        Posicion ping = modelo.getPosicionPing();
        if (ping != null) {
            int x = ping.x * celdaSize;
            int y = ping.y * celdaSize;
            g2.setColor(new Color(255, 255, 0, 100));
            g2.setStroke(new BasicStroke(3));
            g2.drawOval(x + 2, y + 2, celdaSize - 4, celdaSize - 4);
            g2.drawString("SONAR", x + 5, y + 15);
        }

    }

    private void dibujarEntidades(Graphics2D g2) {
        // --- 1. DIBUJAR JUGADOR (Cubo de Datos Verde) ---
        Posicion p = modelo.getJugador();
        int px = p.x * celdaSize;
        int py = p.y * celdaSize;

        g2.setColor(Color.GREEN);
        g2.fillRect(px + 6, py + 6, celdaSize - 12, celdaSize - 12);
        // Brillo interno del jugador
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(1));
        g2.drawRect(px + 10, py + 10, celdaSize - 20, celdaSize - 20);

        // --- 2. DIBUJAR ENEMIGOS ---
        for (Enemigo e : modelo.getEnemigos()) {
            int ex = e.getPos().x * celdaSize;
            int ey = e.getPos().y * celdaSize;
            int centroX = ex + (celdaSize / 2);
            int centroY = ey + (celdaSize / 2);

            if (e.estaCongelado()) {
                g2.setColor(new Color(0, 150, 255));
            } else if (e instanceof EnemigoCorredor) {
                g2.setColor(Color.ORANGE);
            } else if (e instanceof EnemigoTanque) {
                g2.setColor(Color.MAGENTA);
            } else if (e instanceof EnemigoBasico) {
                g2.setColor(Color.RED);
            }

            if (e instanceof EnemigoBasico || e instanceof EnemigoTanque || e instanceof EnemigoCorredor) {
                int offset = (e instanceof EnemigoBasico) ? 8 : (e instanceof EnemigoCorredor) ? 10 : 4;
                g2.fillOval(ex + offset, ey + offset, celdaSize - (offset * 2), celdaSize - (offset * 2));
            }

        }
    }

    private void dibujarHUD(Graphics2D g2) {
        int margenLargo = (modelo.getColumnas() * celdaSize) + 20;
        int alturaBase = getHeight() - 20;

        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, getHeight() - 50, getWidth(), 50);
        g2.setColor(Color.GREEN);
        g2.drawRect(0, getHeight() - 50, getWidth() - 1, 49);

        g2.setFont(new Font("Monospaced", Font.BOLD, 16));
        g2.setColor(Color.CYAN);
        String txtDatos = "DATOS EXTRAÍDOS: " + modelo.getItemsRecogidos() + "/" + modelo.getItemsTotales();
        g2.drawString(txtDatos, 20, alturaBase);

        if (!modelo.getMensajeSistema().isEmpty()) {
            g2.setColor(Color.YELLOW);
            g2.setFont(new Font("Monospaced", Font.ITALIC, 14));
            g2.drawString(">> " + modelo.getMensajeSistema(), 20, getHeight() - 65);
        }

        g2.setFont(new Font("Monospaced", Font.BOLD, 14));
        int yItem = 40;

        // Título lateral
        g2.setColor(Color.GREEN);
        g2.drawString("--- PROTOCOLOS ---", margenLargo, yItem);

        yItem += 30;
        dibujarEstadoHabilidad(g2, "EMP [SPACE]", modelo.getCooldown(), margenLargo, yItem);

        yItem += 25;
        dibujarEstadoHabilidad(g2, "DASH [SHIFT]", modelo.getCooldownDash(), margenLargo, yItem);

        yItem += 25;
        boolean pingActivo = modelo.getPosicionPing() != null;
        g2.setColor(pingActivo ? Color.ORANGE : Color.GREEN);
        g2.drawString("PING [P]: " + (pingActivo ? "ACTIVO" : "LISTO"), margenLargo, yItem);

        String[] nombresHeat = {"SIGILO", "SOSPECHA", "¡BRECHA!"};
        Color[] coloresHeat = {Color.GRAY, Color.ORANGE, Color.RED};

        g2.setColor(coloresHeat[modelo.getHeatLevel() - 1]);
        g2.drawString("NIVEL DE ALERTA: " + nombresHeat[modelo.getHeatLevel() - 1], margenLargo, yItem);

        yItem += 25;
        g2.setColor(Color.WHITE);
        g2.drawString("VIRUS [Q]: " + modelo.getVirusInventario(), margenLargo, yItem);
    }

    private void dibujarEstadoHabilidad(Graphics2D g2, String nombre, int cooldown, int x, int y) {
        if (cooldown == 0) {
            g2.setColor(Color.GREEN);
            g2.drawString(nombre + ": LISTO", x, y);
        } else {
            g2.setColor(Color.RED);
            g2.drawString(nombre + ": " + cooldown + "s", x, y);
            g2.fillRect(x, y + 5, 100 - (cooldown * 10), 3);
        }
    }

    private void dibujarPantallaFin(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setFont(new Font("Monospaced", Font.BOLD, 40));
        String msg = modelo.getMensajeFin();
        FontMetrics fm = g2.getFontMetrics();

        g2.setColor(Color.GREEN);
        int xMsg = (getWidth() - fm.stringWidth(msg)) / 2;
        int yMsg = getHeight() / 2;
        g2.drawString(msg, xMsg, yMsg);

        g2.setFont(new Font("Monospaced", Font.PLAIN, 18));
        g2.setColor(Color.WHITE);
        String subMsg = "PRESIONA [ESPACIO] PARA RECONECTAR";
        fm = g2.getFontMetrics();
        g2.drawString(subMsg, (getWidth() - fm.stringWidth(subMsg)) / 2, yMsg + 50);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (modelo == null || modelo.isJuegoTerminado()) {
            if (modelo != null && e.getKeyCode() == KeyEvent.VK_SPACE) {
                ventanaPrincipal.mostrarMenu();
            }
            return;
        }

        int dx = 0, dy = 0;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP ->
                dy = -1;
            case KeyEvent.VK_DOWN ->
                dy = 1;
            case KeyEvent.VK_LEFT ->
                dx = -1;
            case KeyEvent.VK_RIGHT ->
                dx = 1;
            case KeyEvent.VK_SPACE -> {
                modelo.activarHabilidadEspecial();
                repaint();
                return;
            }
            case KeyEvent.VK_Q -> {
                modelo.usarVirusSobrecarga();
                repaint();
            }
        }

        if (dx != 0 || dy != 0) {
            if (e.isControlDown()) {
                modelo.lanzarPing(dx, dy);
            } else if (e.isShiftDown()) {
                modelo.procesarTurnoJugador(dx, dy, true);
            } else {
                modelo.procesarTurnoJugador(dx, dy, false);
            }
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
