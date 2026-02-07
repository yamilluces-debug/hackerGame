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
    private MundoRenderer mundoRenderer = new MundoRenderer();
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
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        mundoRenderer.dibujarTablero(g2, modelo, celdaSize);
        mundoRenderer.dibujarEntidades(g2, modelo, celdaSize);

        hudManager.dibujarTodo(g2, modelo, getWidth(), getHeight(), celdaSize);

        if (modelo.isJuegoTerminado()) {
            mundoRenderer.dibujarPantallaFin(g2, modelo, getWidth(), getHeight());
        }
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
