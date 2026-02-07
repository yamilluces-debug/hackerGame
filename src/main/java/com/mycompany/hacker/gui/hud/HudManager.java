package com.mycompany.hacker.gui.hud;

import com.mycompany.hacker.logica.JuegoModelo;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class HudManager {

    private List<HudComponent> componentes;

    public HudManager() {
        componentes = new ArrayList<>();
        componentes.add(new SeccionLateral());
        componentes.add(new BarraInferior());
    }

    public void dibujarTodo(Graphics2D g2, JuegoModelo modelo, int sw, int sh, int celdaSize) {
        for (HudComponent c : componentes) {
            c.dibujar(g2, modelo, sw, sh, celdaSize);
        }
    }
}
