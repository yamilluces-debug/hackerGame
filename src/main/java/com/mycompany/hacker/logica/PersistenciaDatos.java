package com.mycompany.hacker.logica;

import java.io.*;

public class PersistenciaDatos {

    private static final String ARCHIVO = "progreso.dat";
    private static final int CLAVE_XOR = 0b1011;
    private static final String HEADER = "!!! DATA ENCRYPTED. This information has been protected against leaks, for (i)legality of its content.";

    private static int manipularDato(int dato) {
        return dato ^ CLAVE_XOR;
    }

    // ------------------------------------------------------------------
    public static int[] cargarProgreso() {
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {
            String cabecera = br.readLine();
            if (!HEADER.equals(cabecera)) {
                throw new IOException("Cabecera de archivo inválida. Datos comprometidos.");
            }

            return new int[]{
                manipularDato(Integer.parseInt(br.readLine())), // 0: Monedas
                manipularDato(Integer.parseInt(br.readLine())), // 1: Duracion EMP
                manipularDato(Integer.parseInt(br.readLine())), // 2: Cooldown Dash
                manipularDato(Integer.parseInt(br.readLine())) // 3: Tiene Escudo (0 o 1)
            };
        } catch (Exception e) {

            System.err.println("Advertencia de seguridad: Progreso corrupto o no encontrado. Reestableciendo valores.");
            return new int[]{0, 3, 5, 0};
        }
    }

    public static void guardarProgreso(int mon, int emp, int dash, int esc) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
            pw.println(HEADER);

            pw.println(manipularDato(mon));
            pw.println(manipularDato(emp));
            pw.println(manipularDato(dash));
            pw.println(manipularDato(esc));
        } catch (IOException e) {
            System.err.println("Error al guardar el progreso: " + e.getMessage());
        }
    }
}
