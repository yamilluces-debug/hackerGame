package com.mycompany.hacker.gui;

import com.mycompany.hacker.gui.hud.HudManager;
import com.mycompany.hacker.logica.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;

public class PanelJuego extends JPanel implements KeyListener {

    private JuegoModelo modelo;
    private HudManager hudManager = new HudManager();
    private MainVentana ventanaPrincipal;
    private int celdaSize = 40;
    public static final int ANCHO_HUD_LATERAL = 250;

    public PanelJuego(MainVentana v) {
        this.ventanaPrincipal = v;
        this.setBackground(Color.DARK_GRAY);
        this.setFocusable(true);
        this.addKeyListener(this);
    }

    public void iniciarJuego(int filas, int cols) {
        modelo = new JuegoModelo(filas, cols);

        int altoDisp = (getHeight() > 0 ? getHeight() : 600) - 60;
        int anchoDisp = (getWidth() > 0 ? getWidth() : 800) - ANCHO_HUD_LATERAL;

        if (filas > 0 && cols > 0) {
            celdaSize = Math.min(altoDisp / filas, anchoDisp / cols);
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

        dibujarTablero(g2);
        dibujarEntidades(g2);

        hudManager.dibujarTodo(g2, modelo, getWidth(), getHeight(), celdaSize);

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
