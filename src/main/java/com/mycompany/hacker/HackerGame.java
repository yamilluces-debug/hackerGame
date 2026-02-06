package com.mycompany.hacker;

import com.mycompany.hacker.gui.MainVentana;

public class HackerGame {

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(() -> {
            new MainVentana().setVisible(true);
        });
    }
}
