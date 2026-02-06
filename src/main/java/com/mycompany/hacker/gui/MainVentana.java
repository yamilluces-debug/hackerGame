package com.mycompany.hacker.gui;

import com.mycompany.hacker.logica.Posicion;
import com.mycompany.hacker.logica.JuegoModelo;
import com.mycompany.hacker.logica.Enemigo;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainVentana extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private PanelJuego panelJuego;

    public MainVentana() {
        setTitle("Hacker Grid: Breach Protocol");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Agregar paneles
        mainPanel.add(crearPanelMenu(), "MENU");
        panelJuego = new PanelJuego(this);
        mainPanel.add(panelJuego, "JUEGO");

        add(mainPanel);
        mostrarMenu(); // Método nuevo para mostrar menú
    }

    // --- MÉTODOS PÚBLICOS PARA NAVEGACIÓN (Solución del error) ---
    public void mostrarMenu() {
        cardLayout.show(mainPanel, "MENU");
        mainPanel.requestFocusInWindow();
    }

    public void iniciarPartida(int f, int c) {
        panelJuego.iniciarJuego(f, c);
        cardLayout.show(mainPanel, "JUEGO");
        panelJuego.requestFocusInWindow(); // Importante para detectar teclas
    }

    // -------------------------------------------------------------
    private JPanel crearPanelMenu() {
        JPanel menu = new JPanel(new GridBagLayout());
        menu.setBackground(Color.BLACK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel title = new JLabel("CONFIGURACIÓN DE RED");
        title.setForeground(Color.GREEN);
        title.setFont(new Font("Monospaced", Font.BOLD, 24));
        menu.add(title, gbc);

        // Inputs
        gbc.gridy++;
        JPanel configPanel = new JPanel();
        configPanel.setOpaque(false);

        JLabel lblF = new JLabel("Filas:");
        lblF.setForeground(Color.WHITE);
        JTextField txtFilas = new JTextField("10", 3);
        JLabel lblC = new JLabel("Cols:");
        lblC.setForeground(Color.WHITE);
        JTextField txtCols = new JTextField("10", 3);

        configPanel.add(lblF);
        configPanel.add(txtFilas);
        configPanel.add(lblC);
        configPanel.add(txtCols);
        menu.add(configPanel, gbc);

        // Botón Start
        gbc.gridy++;
        JButton btnStart = new JButton("INICIAR HACKEO");
        btnStart.setBackground(Color.DARK_GRAY);
        btnStart.setForeground(Color.GREEN);
        btnStart.addActionListener(e -> {
            try {
                int f = Integer.parseInt(txtFilas.getText());
                int c = Integer.parseInt(txtCols.getText());
                // Validar min/max
                if (f < 5 || c < 5 || f > 50 || c > 50) {
                    JOptionPane.showMessageDialog(this, "Tamaño entre 5 y 50");
                    return;
                }
                iniciarPartida(f, c);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Introduce números válidos");
            }
        });
        menu.add(btnStart, gbc);

        return menu;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainVentana().setVisible(true);
        });
    }
}

// --- CLASE INTERNA PARA EL DIBUJO DEL TABLERO ---
class PanelJuego extends JPanel implements KeyListener {

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
        int altoDisponible = getHeight() > 0 ? getHeight() : 600;
        int anchoDisponible = getWidth() > 0 ? getWidth() : 800;

        // Evitar división por cero
        if (filas > 0 && cols > 0) {
            celdaSize = Math.min((altoDisponible - 50) / filas, anchoDisponible / cols);
        }
        if (celdaSize < 10) {
            celdaSize = 10;
        }
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

        int filas = modelo.getFilas();
        int cols = modelo.getColumnas();

        // 1. DIBUJAR TABLERO
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < cols; j++) {
                int x = j * celdaSize;
                int y = i * celdaSize;

                g2.setColor(Color.BLACK);
                g2.drawRect(x, y, celdaSize, celdaSize);

                int contenido = modelo.getTablero()[i][j];

                if (contenido == JuegoModelo.PARED) {
                    g2.setColor(Color.GRAY);
                    g2.fillRect(x + 1, y + 1, celdaSize - 1, celdaSize - 1);
                } else if (contenido == JuegoModelo.SALIDA) {
                    if (modelo.getItemsRecogidos() >= modelo.getItemsTotales()) {
                        g2.setColor(Color.GREEN);
                    } else {
                        g2.setColor(new Color(100, 0, 0));
                    }
                    g2.fillRect(x + 2, y + 2, celdaSize - 4, celdaSize - 4);
                    g2.setColor(Color.WHITE);
                    // Solo dibujar texto si la celda es grande
                    if (celdaSize > 20) {
                        g2.drawString("EXIT", x + 5, y + celdaSize / 2);
                    }
                } else if (contenido == JuegoModelo.ITEM) {
                    g2.setColor(Color.CYAN);
                    g2.fillOval(x + celdaSize / 4, y + celdaSize / 4, celdaSize / 2, celdaSize / 2);
                }
            }
        }

        // 2. DIBUJAR JUGADOR
        Posicion p = modelo.getJugador();
        g2.setColor(Color.GREEN);
        g2.fillRect(p.x * celdaSize + 5, p.y * celdaSize + 5, celdaSize - 10, celdaSize - 10);

        // 3. DIBUJAR ENEMIGOS
        g2.setColor(Color.RED);
        for (Enemigo e : modelo.getEnemigos()) {
            g2.fillOval(e.getPos().x * celdaSize + 5, e.getPos().y * celdaSize + 5, celdaSize - 10, celdaSize - 10);
        }

        // 4. HUD
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.BOLD, 16));
        String estado = "DATOS: " + modelo.getItemsRecogidos() + " / " + modelo.getItemsTotales();
        g2.drawString(estado, 10, getHeight() - 10);

        // 5. MENSAJE FIN DE JUEGO
        if (modelo.isJuegoTerminado()) {
            g2.setColor(new Color(0, 0, 0, 150));
            g2.fillRect(0, getHeight() / 2 - 50, getWidth(), 100);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 30));
            FontMetrics fm = g2.getFontMetrics();
            int textoAncho = fm.stringWidth(modelo.getMensajeFin());
            g2.drawString(modelo.getMensajeFin(), (getWidth() - textoAncho) / 2, getHeight() / 2 + 10);

            g2.setFont(new Font("Arial", Font.PLAIN, 14));
            g2.drawString("Presiona ESPACIO para volver al menú", (getWidth() - 250) / 2, getHeight() / 2 + 40);
        }
    }

    // --- CONTROLES (INPUT) ---
    @Override
    public void keyPressed(KeyEvent e) {
        if (modelo == null) {
            return;
        }

        if (modelo.isJuegoTerminado()) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                // AQUI ESTABA EL ERROR: Ahora usamos el método limpio
                ventanaPrincipal.mostrarMenu();
            }
            return;
        }

        int dx = 0, dy = 0;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                dy = -1;
                break;
            case KeyEvent.VK_DOWN:
                dy = 1;
                break;
            case KeyEvent.VK_LEFT:
                dx = -1;
                break;
            case KeyEvent.VK_RIGHT:
                dx = 1;
                break;
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
