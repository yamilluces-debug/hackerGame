package com.mycompany.hacker.gui;

import com.mycompany.hacker.logica.PersistenciaDatos;
import java.awt.*;
import javax.swing.*;

public class VentanaTienda extends JDialog {

    private int[] progreso;
    private JLabel lblMonedas;

    public VentanaTienda(JFrame padre) {
        super(padre, "BLACK MARKET: HARDWARE UPGRADES", true);
        progreso = PersistenciaDatos.cargarProgreso();

        setSize(550, 480);
        setLocationRelativeTo(padre);
        setUndecorated(true);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));

        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setBackground(new Color(20, 20, 20));
        panelNorte.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblMonedas = new JLabel("CRÉDITOS DISPONIBLES: $" + progreso[0], SwingConstants.LEFT);
        lblMonedas.setForeground(Color.YELLOW);
        lblMonedas.setFont(new Font("Monospaced", Font.BOLD, 18));

        JLabel titulo = new JLabel("HARDWARE UPGRADES", SwingConstants.RIGHT);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Monospaced", Font.BOLD, 18));

        panelNorte.add(lblMonedas, BorderLayout.WEST);
        panelNorte.add(titulo, BorderLayout.EAST);
        mainPanel.add(panelNorte, BorderLayout.NORTH);

        JPanel panelItems = new JPanel(new GridLayout(3, 1, 15, 15));
        panelItems.setBackground(Color.BLACK);
        panelItems.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        panelItems.add(crearItemTienda("OVERCLOCK EMP", "Aumenta la duración de congelación a 5 turnos.", 500, 1, 5));
        panelItems.add(crearItemTienda("DASH OPTIMIZADO", "Reduce el cooldown de Dash de 5 a 3 turnos.", 500, 2, 3));
        panelItems.add(crearItemTienda("FIREWALL ESCUDO", "Otorga 1 uso de protección contra colisiones.", 200, 3, 1));

        mainPanel.add(panelItems, BorderLayout.CENTER);

        JButton btnSalir = new JButton("CERRAR CONEXIÓN CON EL MERCADO");
        btnSalir.setBackground(Color.BLACK);
        btnSalir.setForeground(Color.RED);
        btnSalir.setFont(new Font("Monospaced", Font.BOLD, 15));
        btnSalir.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        btnSalir.addActionListener(e -> dispose());

        JPanel panelSur = new JPanel(new FlowLayout());
        panelSur.setBackground(Color.BLACK);
        panelSur.add(btnSalir);

        mainPanel.add(panelSur, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel crearItemTienda(String nombre, String descripcion, int precio, int indiceProgreso, int valorMejora) {
        JPanel p = new JPanel(new BorderLayout(20, 0));
        p.setBackground(new Color(10, 10, 10));
        p.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));

        JPanel pTexto = new JPanel(new GridLayout(2, 1));
        pTexto.setOpaque(false);
        pTexto.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblNombre = new JLabel("<html><b>" + nombre + "</b> ($" + precio + ")</html>");
        lblNombre.setForeground(Color.GREEN);
        lblNombre.setFont(new Font("Monospaced", Font.BOLD, 14));

        JLabel lblDesc = new JLabel("<html>" + descripcion + "</html>");
        lblDesc.setForeground(Color.GRAY);
        lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 10));

        pTexto.add(lblNombre);
        pTexto.add(lblDesc);
        p.add(pTexto, BorderLayout.CENTER);

        boolean adquirido = progreso[indiceProgreso] == valorMejora;
        JButton btn = new JButton(adquirido ? "ADQUIRIDO" : "COMPRAR");
        btn.setPreferredSize(new Dimension(120, 0));

        if (adquirido) {
            btn.setEnabled(false);
            btn.setBackground(Color.BLACK);
            btn.setForeground(Color.GREEN);
            btn.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
        } else {
            btn.setBackground(Color.BLACK);
            btn.setForeground(Color.CYAN);
            btn.setBorder(BorderFactory.createLineBorder(Color.CYAN, 1));
        }

        btn.addActionListener(e -> {
            if (progreso[0] >= precio) {
                progreso[0] -= precio;
                progreso[indiceProgreso] = valorMejora;
                PersistenciaDatos.guardarProgreso(progreso[0], progreso[1], progreso[2], progreso[3]);
                lblMonedas.setText("CRÉDITOS DISPONIBLES: $" + progreso[0]);
                btn.setText("ADQUIRIDO");
                btn.setEnabled(false);
                btn.setBackground(Color.BLACK);
                btn.setForeground(Color.GREEN);
                btn.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
            } else {
                JOptionPane.showMessageDialog(this, "ERROR: CRÉDITOS INSUFICIENTES. ACCESO DENEGADO.");
            }
        });

        p.add(btn, BorderLayout.EAST);
        return p;
    }
}
