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
        // Cálculo dinámico del tamaño de celda
        int altoDisp = getHeight() > 0 ? getHeight() : 600;
        int anchoDisp = getWidth() > 0 ? getWidth() : 800;
        if (filas > 0 && cols > 0) {
            celdaSize = Math.min((altoDisp - 50) / filas, anchoDisp / cols);
        }
        celdaSize = Math.max(celdaSize, 10); // Tamaño mínimo
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
                }
            }
        }
    }

    private void dibujarEntidades(Graphics2D g2) {
        // Jugador
        Posicion p = modelo.getJugador();
        g2.setColor(Color.GREEN);
        g2.fillRect(p.x * celdaSize + 5, p.y * celdaSize + 5, celdaSize - 10, celdaSize - 10);

        // Enemigos
        for (Enemigo e : modelo.getEnemigos()) {
            g2.setColor(e.estaCongelado() ? Color.BLUE : Color.RED);
            g2.fillOval(e.getPos().x * celdaSize + 5, e.getPos().y * celdaSize + 5, celdaSize - 10, celdaSize - 10);
        }
    }

    private void dibujarHUD(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.BOLD, 14));

        // Info Habilidad
        String txtHabilidad = (modelo.getCooldown() == 0) ? "[ESPACIO] EMP LISTO" : "EMP: " + modelo.getCooldown();
        g2.drawString(txtHabilidad, getWidth() - 180, getHeight() - 10);

        // Info Datos
        String txtDatos = "DATOS: " + modelo.getItemsRecogidos() + " / " + modelo.getItemsTotales();
        g2.drawString(txtDatos, 10, getHeight() - 10);

        // Mensajes Sistema
        if (!modelo.getMensajeSistema().isEmpty()) {
            g2.setColor(Color.YELLOW);
            g2.drawString(modelo.getMensajeSistema(), 10, getHeight() - 30);
        }
    }

    private void dibujarPantallaFin(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, getHeight() / 2 - 50, getWidth(), 100);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 30));

        String msg = modelo.getMensajeFin();
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2 + 10);

        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        String subMsg = "Presiona ESPACIO para volver al menú";
        g2.drawString(subMsg, (getWidth() - g2.getFontMetrics().stringWidth(subMsg)) / 2, getHeight() / 2 + 40);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (modelo == null) {
            return;
        }

        if (modelo.isJuegoTerminado()) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
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
        }

        if (dx != 0 || dy != 0) {
            modelo.procesarTurnoJugador(dx, dy);
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
