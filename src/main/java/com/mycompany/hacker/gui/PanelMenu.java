package com.mycompany.hacker.gui;

import java.awt.*;
import javax.swing.*;

public class PanelMenu extends JPanel {

    private MainVentana ventanaPrincipal;

    public PanelMenu(MainVentana v) {
        this.ventanaPrincipal = v;
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título Principal
        JLabel title = new JLabel("BREACH PROTOCOL v1.0", SwingConstants.CENTER);
        title.setForeground(Color.GREEN);
        title.setFont(new Font("Monospaced", Font.BOLD, 32));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(title, gbc);

        // Subtítulo decorativo
        JLabel subtitle = new JLabel("SISTEMA OPERATIVO DE INTRUSIÓN REMOTA", SwingConstants.CENTER);
        subtitle.setForeground(new Color(0, 100, 0));
        subtitle.setFont(new Font("Monospaced", Font.PLAIN, 12));
        gbc.gridy = 1;
        add(subtitle, gbc);

        gbc.gridy = 2;
        add(Box.createVerticalStrut(20), gbc);

        // --- Botón 1: EJECUTAR TRABAJO ---
        gbc.gridy = 3;
        JButton btnTrabajo = new JButton("EJECUTAR TRABAJO DE INFILTRACIÓN");
        estilizarBoton(btnTrabajo, Color.GREEN);
        btnTrabajo.addActionListener(e -> {
            VentanaConfiguracion vConfig = new VentanaConfiguracion(ventanaPrincipal);
            vConfig.setVisible(true);
        });
        add(btnTrabajo, gbc);

        // --- Botón 2: MERCADO NEGRO (TIENDA) ---
        gbc.gridy = 4;
        JButton btnTienda = new JButton("MERCADO NEGRO (UPGRADES)");
        estilizarBoton(btnTienda, Color.YELLOW);
        btnTienda.addActionListener(e -> {
            new VentanaTienda(ventanaPrincipal).setVisible(true);
        });
        add(btnTienda, gbc);

        // --- Botón 3: AYUDA ---
        gbc.gridy = 5;
        JButton btnAyuda = new JButton("CONSULTAR MANUAL DE RED");
        estilizarBoton(btnAyuda, Color.CYAN);
        btnAyuda.addActionListener(e -> {
            VentanaManual manual = new VentanaManual(ventanaPrincipal);
            manual.setVisible(true);
        });
        add(btnAyuda, gbc);
    }

    private void estilizarBoton(JButton btn, Color c) {
        btn.setBackground(Color.BLACK);
        btn.setForeground(c);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(350, 45));
        btn.setFont(new Font("Monospaced", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createLineBorder(c, 2));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(20, 20, 20));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(Color.BLACK);
            }
        });
    }
}
