package com.mycompany.hacker.gui.hud;

import com.mycompany.hacker.logica.JuegoModelo;
import java.awt.*;

public class BarraInferior implements HudComponent {

    @Override
    public void dibujar(Graphics2D g2, JuegoModelo modelo, int sw, int sh, int celdaSize) {
        g2.setFont(new Font("Monospaced", Font.BOLD, 14));
        g2.setColor(new Color(10, 30, 10, 240));
        g2.fillRect(0, sh - 50, sw, 50);

        g2.setColor(Color.GREEN);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(0, sh - 50, sw, sh - 50);

        g2.setColor(Color.CYAN);
        String textoPkts = "PKTS: " + modelo.getItemsRecogidos() + "/" + modelo.getItemsTotales();
        g2.drawString(textoPkts, 20, sh - 18);

        if (!modelo.getMensajeSistema().isEmpty()) {
            String fullMsg = "SYS: " + modelo.getMensajeSistema();

            FontMetrics fm = g2.getFontMetrics();
            int anchoTexto = fm.stringWidth(fullMsg);

            int xMensaje = sw - anchoTexto - 20;

            g2.setColor(Color.YELLOW);
            g2.drawString(fullMsg, xMensaje, sh - 18);
        }
    }
}
