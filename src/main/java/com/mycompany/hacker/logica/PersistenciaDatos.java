package com.mycompany.hacker.logica;

import java.io.*;

public class PersistenciaDatos {

    private static final String ARCHIVO = "progreso.dat";

    public static int[] cargarProgreso() {
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {
            return new int[]{
                Integer.parseInt(br.readLine()), // 0: Monedas
                Integer.parseInt(br.readLine()), // 1: Duracion EMP
                Integer.parseInt(br.readLine()), // 2: Cooldown Dash
                Integer.parseInt(br.readLine()) // 3: Tiene Escudo (0 o 1)
            };
        } catch (Exception e) {
            return new int[]{0, 3, 5, 0};
        }
    }

    public static void guardarProgreso(int mon, int emp, int dash, int esc) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
            pw.println(mon);
            pw.println(emp);
            pw.println(dash);
            pw.println(esc);
        } catch (IOException e) {
            System.err.println("Error al guardar el progreso: " + e.getMessage());
        }
    }
}
