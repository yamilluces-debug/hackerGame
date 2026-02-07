package com.mycompany.hacker.logica;

import java.io.*;

public class PersistenciaDatos {

    private static final String ARCHIVO = "progreso.dat";

    public static int cargarMonedas() {
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {
            return Integer.parseInt(br.readLine());
        } catch (Exception e) {
            return 0;
        }
    }

    public static void guardarMonedas(int cantidad) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
            pw.println(cantidad);
        } catch (IOException e) {
            System.err.println("Error al guardar progreso: " + e.getMessage());
        }
    }

    public static int[] cargarProgreso() {
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {
            return new int[]{
                Integer.parseInt(br.readLine()),
                Integer.parseInt(br.readLine()),
                Integer.parseInt(br.readLine()),
                Integer.parseInt(br.readLine())
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
            e.printStackTrace();
        }
    }
}
