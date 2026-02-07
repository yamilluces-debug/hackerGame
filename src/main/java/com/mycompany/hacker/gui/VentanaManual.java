package com.mycompany.hacker.gui;

import java.awt.*;
import javax.swing.*;

public class VentanaManual extends JDialog {

    public VentanaManual(JFrame padre) {
        super(padre, "MANUAL DE OPERACIONES: BREACH PROTOCOL", true);
        setSize(550, 450);
        setLocationRelativeTo(padre);
        setResizable(false);

        // Panel principal con estética oscura
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.BLACK);
        panelPrincipal.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));

        // TabbedPane personalizado
        JTabbedPane pestañas = new JTabbedPane();
        pestañas.setBackground(Color.DARK_GRAY);
        pestañas.setForeground(Color.BLACK);

        // Agregar secciones
        pestañas.addTab("MISIÓN", crearPanelTexto(getHistoria()));
        pestañas.addTab("CONTROLES", crearPanelTexto(getControles()));
        pestañas.addTab("INTELIGENCIA", crearPanelTexto(getEnemigos()));

        panelPrincipal.add(pestañas, BorderLayout.CENTER);

        // Botón de cierre
        JButton btnCerrar = new JButton("ENTENDIDO");
        btnCerrar.setBackground(Color.BLACK);
        btnCerrar.setForeground(Color.GREEN);
        btnCerrar.setFont(new Font("Monospaced", Font.BOLD, 14));
        btnCerrar.addActionListener(e -> dispose());
        panelPrincipal.add(btnCerrar, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private JPanel crearPanelTexto(String contenido) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.BLACK);

        JTextArea area = new JTextArea(contenido);
        area.setEditable(false);
        area.setBackground(Color.BLACK);
        area.setForeground(Color.GREEN);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setMargin(new Insets(20, 20, 20, 20));

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(null);
        p.add(scroll, BorderLayout.CENTER);

        return p;
    }

    private String getHistoria() {
        return "--- ARCHIVO CLASIFICADO ---\n\n"
                + "Año 2099. La información no es poder, es la única moneda de cambio.\n\n"
                + "Has sido contratado como un 'Breacher' independiente para infiltrarte en "
                + "los servidores de la Corporación OmniCorp. Tu objetivo es simple pero "
                + "suicida: extraer los Paquetes de Datos Alpha dispersos por la red.\n\n"
                + "A medida que robes datos, el sistema de seguridad detectará tu rastro "
                + "y el nivel de ALERTA subirá. Si los Firewalls (Enemigos) detectan tu "
                + "IP física, la conexión será terminada permanentemente.\n\n"
                + "Entra, roba los datos y sal por el puerto de escape antes de que sea tarde.";
    }

    private String getControles() {
        return "--- MANUAL DE INTRUSIÓN ---\n\n"
                + "MOVIMIENTO:\n"
                + "• Flechas: Desplazarse un nodo (Gasta 1 turno).\n\n"
                + "HABILIDADES DE SOFTWARE:\n"
                + "• SHIFT + Flecha (OVERCLOCK):\n"
                + "  Salto de 2 casillas. Permite saltar sobre muros o enemigos.\n\n"
                + "• CTRL + Flecha (SONAR PING):\n"
                + "  Lanza un señuelo que atrae a los enemigos cercanos por 4 turnos.\n\n"
                + "• ESPACIO (PULSO EMP):\n"
                + "  Sobrecarga los circuitos enemigos cercanos, congelándolos 3 turnos.\n\n"
                + "• TECLA Q (VIRUS SOBRECARGA):\n"
                + "  Si tienes un Exploit recolectado, elimina al enemigo más cercano.";
    }

    private String getEnemigos() {
        return "--- BASE DE DATOS DE CONTRAMEDIDAS ---\n\n"
                + "• ROJO (BÁSICO):\n"
                + "  Software de rastreo estándar. Te seguirá una vez detectado.\n\n"
                + "• NARANJA (CORREDOR):\n"
                + "  Algoritmo de respuesta rápida. Se mueve 2 casillas en línea recta.\n\n"
                + "• MAGENTA (TANQUE):\n"
                + "  Firewall pesado. Solo se mueve cada 2 turnos de usuario.\n\n"
                + "• CYAN (PAQUETE):\n"
                + "  Tu objetivo. Debes recolectar todos para abrir la salida.\n\n"
                + "• CUADRADO BLANCO (EXPLOIT):\n"
                + "  Virus de un solo uso para eliminar amenazas.";
    }
}
