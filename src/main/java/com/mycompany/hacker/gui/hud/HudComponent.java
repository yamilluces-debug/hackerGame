package com.mycompany.hacker.gui.hud;

import com.mycompany.hacker.logica.JuegoModelo;
import java.awt.Graphics2D;

public interface HudComponent {

    void dibujar(Graphics2D g2, JuegoModelo modelo, int sw, int sh, int celdaSize);
}
