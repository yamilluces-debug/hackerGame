package com.mycompany.hacker.gui;

import java.awt.*;
import javax.swing.*;

public class PanelMenu extends JPanel {

    private MainVentana ventanaPrincipal;
    private JTextField txtFilas;
    private JTextField txtCols;

    public PanelMenu(MainVentana v) {
        this.ventanaPrincipal = v;
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Título
        JLabel title = new JLabel("CONFIGURACIÓN DE RED");
        title.setForeground(Color.GREEN);
        title.setFont(new Font("Monospaced", Font.BOLD, 24));
        add(title, gbc);

        // Inputs
        gbc.gridy++;
        JPanel configPanel = new JPanel();
        configPanel.setOpaque(false);

        JLabel lblF = new JLabel("Filas:");
        lblF.setForeground(Color.WHITE);
        txtFilas = new JTextField("10", 3);

        JLabel lblC = new JLabel("Cols:");
        lblC.setForeground(Color.WHITE);
        txtCols = new JTextField("10", 3);

        configPanel.add(lblF);
        configPanel.add(txtFilas);
        configPanel.add(lblC);
        configPanel.add(txtCols);
        add(configPanel, gbc);

        // Botón Start
        gbc.gridy++;
        JButton btnStart = new JButton("INICIAR HACKEO");
        btnStart.setBackground(Color.DARK_GRAY);
        btnStart.setForeground(Color.GREEN);

        btnStart.addActionListener(e -> validarYArrancar());
        add(btnStart, gbc);
    }

    private void validarYArrancar() {
        try {
            int f = Integer.parseInt(txtFilas.getText());
            int c = Integer.parseInt(txtCols.getText());

            if (f < 5 || c < 5 || f > 50 || c > 50) {
                JOptionPane.showMessageDialog(this, "Tamaño entre 5 y 50");
                return;
            }
            ventanaPrincipal.iniciarPartida(f, c);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Introduce números válidos");
        }
    }
}
