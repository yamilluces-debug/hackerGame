# █ BREACH PROTOCOL █

### Hacker Grid Game v1.0

![GitHub stars](https://img.shields.io/github/stars/yamilluces-debug/hackerGame?style=flat-square)
![GitHub language count](https://img.shields.io/github/languages/count/yamilluces-debug/hackerGame?style=flat-square)
![NetBeans](https://img.shields.io/badge/IDE-NetBeans-blue?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-green?style=flat-square)

---

## 💻 DESCRIPCIÓN DEL PROYECTO

**Breach Protocol** es un juego de puzzle y estrategia por turnos, inspirado en los géneros *Roguelike* y *Ciberpunk*. El jugador asume el rol de un Breacher infiltrado en una red de servidores (Grid) con el objetivo de extraer paquetes de datos (PKTS) y escapar. Cada movimiento es crucial, ya que los programas de seguridad (Firewalls) reaccionan a tus pasos.

El proyecto está diseñado siguiendo una arquitectura modular (Separación Lógica/Visual) en Java, optimizado para ser fácilmente escalable y extensible.

## ⚙️ MECÁNICAS PRINCIPALES

El diseño del juego se centra en la toma de decisiones con información limitada y la gestión de recursos:

*   **Sistema de Turnos Sincrónico:** El jugador se mueve 1 vez, y todos los enemigos actúan inmediatamente después.
*   **Generación Procedural:** Mapas aleatorios con conectividad garantizada (Algoritmo BFS) para asegurar que la salida sea siempre accesible.
*   **Polimorfismo en IA:** Múltiples tipos de enemigos con patrones de movimiento únicos:
    *   🔴 **Básico:** Persecución estándar.
    *   🟠 **Corredor:** Se mueve dos casillas en línea recta (Alta amenaza).
    *   🟣 **Tanque:** Lento (Se mueve cada 2 turnos).
*   **Heat Level Dinámico:** El nivel de alerta del sistema (SIGILO > SOSPECHA > BRECHA) aumenta al recoger datos, cambiando el comportamiento de la IA y enviando refuerzos.
*   **Progresión Meta:** Sistema de **Créditos ($)** persistente que permite al jugador comprar mejoras permanentes (EMP, Dash, Escudo) entre partidas.

## 💾 TECNOLOGÍA Y ARQUITECTURA

Este proyecto fue desarrollado en Java, utilizando las siguientes prácticas:

*   **Lenguaje:** Java (v17+)
*   **Entorno:** Apache NetBeans (Recomendado)
*   **Interfaz Gráfica:** Java Swing (Personalizado con temática Ciberpunk).
*   **Arquitectura:** Principios de **Modelo-Vista-Controlador (MVC)** estricta, con clases de dominio sin dependencias de Swing (`JuegoModelo`, `Enemigo`).
*   **Delegación de Renderizado:** Uso de `MundoRenderer` y `HudManager` para desacoplar la lógica de dibujo (Graphics2D) de la lógica de juego.
*   **Persistencia:** Guardado de progreso (monedas y mejoras) encriptado con un simple **XOR** para mantener la coherencia con el tema.

## 🕹️ CONTROLES Y HABILIDADES

| Comando | Acción | Tipo de Turno |
| :--- | :--- | :--- |
| **Flechas** | Movimiento estándar (1 casilla). | Gasta Turno |
| **SHIFT + Flechas** | **OVERCLOCK (DASH):** Salto de 2 casillas. | Gasta Turno (CD) |
| **CTRL + Flechas** | **SONAR PING:** Lanza un señuelo que atrae enemigos. | Gasta Turno |
| **ESPACIO** | **PULSO EMP:** Congela todos los enemigos cercanos. | Gasta Turno (CD) |
| **Q** | **VIRUS SOBRECARGA:** Elimina al enemigo más cercano (consumible). | Acción Libre |

## 🚀 INSTALACIÓN Y EJECUCIÓN

Sigue estos pasos para clonar el repositorio y ejecutar el juego en NetBeans:

1.  **Clonar Repositorio:**
    ```bash
    git clone https://github.com/yamilluces-debug/hackerGame.git
    ```

2.  **Abrir en NetBeans:**
    *   Abre NetBeans.
    *   Ve a `File` -> `Open Project...` y selecciona la carpeta clonada.

3.  **Ejecutar:**
    *   Identifica la clase principal: **`HackerGame.java`**.
    *   Haz clic derecho sobre el archivo y selecciona **Run File** (o ejecuta el proyecto directamente).

El juego se iniciará con la pantalla de menú (`PanelMenu`).
