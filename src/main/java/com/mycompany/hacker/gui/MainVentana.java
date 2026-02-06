package com.mycompany.hacker.gui;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainVentana extends JFrame {

    // Constantes para identificar pantallas
    private static final String VISTA_MENU = "MENU";
    private static final String VISTA_JUEGO = "JUEGO";

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private PanelJuego panelJuego;
    private PanelMenu panelMenu;

    public MainVentana() {
        setTitle("Hacker Grid: Breach Protocol");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Inicializar sub-paneles
        panelMenu = new PanelMenu(this);
        panelJuego = new PanelJuego(this);

        // Añadir al stack
        mainPanel.add(panelMenu, VISTA_MENU);
        mainPanel.add(panelJuego, VISTA_JUEGO);

        add(mainPanel);
        mostrarMenu();
    }

    public void mostrarMenu() {
        cardLayout.show(mainPanel, VISTA_MENU);
        panelMenu.requestFocusInWindow();
    }

    public void iniciarPartida(int f, int c) {
        panelJuego.iniciarJuego(f, c);
        cardLayout.show(mainPanel, VISTA_JUEGO);
        panelJuego.requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainVentana().setVisible(true);
        });
    }
}
