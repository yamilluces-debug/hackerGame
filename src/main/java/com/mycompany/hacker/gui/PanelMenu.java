package com.mycompany.hacker.gui;

import java.awt.*;
import javax.swing.*;

public class PanelMenu extends JPanel {

    private MainVentana ventanaPrincipal;
    private JComboBox<String> comboDificultad;
    private JTextField txtSeed; // Para la semilla opcional

    public PanelMenu(MainVentana v) {
        this.ventanaPrincipal = v;
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título con estilo
        JLabel title = new JLabel("BREACH PROTOCOL v1.0", SwingConstants.CENTER);
        title.setForeground(Color.GREEN);
        title.setFont(new Font("Monospaced", Font.BOLD, 28));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        // Selector de Dificultad
        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel lblDif = new JLabel("NIVEL DE ACCESO:");
        lblDif.setForeground(Color.WHITE);
        add(lblDif, gbc);

        gbc.gridx = 1;
        String[] niveles = {"NOVATO (8x8)", "HACKER (15x15)", "ELITE (25x25)", "PERSONALIZADO"};
        comboDificultad = new JComboBox<>(niveles);
        add(comboDificultad, gbc);

        // Campo para Semilla (Opcional)
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblSeed = new JLabel("NODO (SEED):");
        lblSeed.setForeground(Color.GRAY);
        add(lblSeed, gbc);

        gbc.gridx = 1;
        txtSeed = new JTextField();
        add(txtSeed, gbc);

        // Botón Iniciar
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton btnStart = new JButton("ESTABLECER CONEXIÓN");
        estilizarBoton(btnStart, Color.GREEN);
        btnStart.addActionListener(e -> procesarInicio());
        add(btnStart, gbc);

        // Botón Ayuda
        gbc.gridy++;
        JButton btnAyuda = new JButton("MANUAL DE INTRUSIÓN");
        estilizarBoton(btnAyuda, Color.CYAN);
        btnAyuda.addActionListener(e -> mostrarAyuda());
        add(btnAyuda, gbc);
    }

    private void estilizarBoton(JButton btn, Color c) {
        btn.setBackground(Color.BLACK);
        btn.setForeground(c);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Monospaced", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createLineBorder(c, 2));
    }

    private void procesarInicio() {
        int f = 10, c = 10;
        String seleccion = (String) comboDificultad.getSelectedItem();

        switch (seleccion) {
            case "NOVATO (8x8)" -> {
                f = 8;
                c = 8;
            }
            case "HACKER (15x15)" -> {
                f = 15;
                c = 15;
            }
            case "ELITE (25x25)" -> {
                f = 25;
                c = 25;
            }
            case "PERSONALIZADO" -> {
                f = 20;
                c = 20;
            }
        }

        ventanaPrincipal.iniciarPartida(f, c);
    }

    private void mostrarAyuda() {
        VentanaManual manual = new VentanaManual(ventanaPrincipal);
        manual.setVisible(true);
    }
}
