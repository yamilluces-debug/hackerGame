package com.mycompany.hacker.gui;

import java.awt.*;
import javax.swing.*;

public class VentanaConfiguracion extends JDialog {

    private JComboBox<String> comboDificultad;
    private MainVentana ventanaPrincipal;

    public VentanaConfiguracion(MainVentana padre) {
        super(padre, "CONFIGURACIÓN DE ENLACE", true);
        this.ventanaPrincipal = padre;

        setSize(350, 200);
        setLocationRelativeTo(padre);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lbl = new JLabel("SELECCIONAR NIVEL DE ACCESO:");
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Monospaced", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lbl, gbc);

        String[] niveles = {"NOVATO (8x8)", "HACKER (15x15)", "ELITE (25x25)"};
        comboDificultad = new JComboBox<>(niveles);
        gbc.gridy = 1;
        panel.add(comboDificultad, gbc);

        JButton btnConfirmar = new JButton("ESTABLECER CONEXIÓN");
        btnConfirmar.setBackground(Color.BLACK);
        btnConfirmar.setForeground(Color.GREEN);
        btnConfirmar.setFont(new Font("Monospaced", Font.BOLD, 13));
        btnConfirmar.addActionListener(e -> confirmar());
        gbc.gridy = 2;
        panel.add(btnConfirmar, gbc);

        add(panel);
    }

    private void confirmar() {
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
        }

        dispose(); // Cerrar ventana de config
        ventanaPrincipal.iniciarPartida(f, c);
    }
}
