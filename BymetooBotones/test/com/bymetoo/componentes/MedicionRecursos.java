package com.bymetoo.componentes;

import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * DI-3.4.5 · Programa para medir el uso de recursos del componente.
 * <p>
 * No es un test: se ejecuta con <b>Run File (Mayús+F6)</b> mientras VisualVM
 * está conectado al proceso. Hace cuatro escenarios con pausas entre ellos
 * para que se vean en las gráficas de VisualVM, y al final imprime una tabla
 * con memoria, tiempo e hilos.
 * </p>
 * <ol>
 *   <li>E1: crear 1 000 botones (memoria por botón).</li>
 *   <li>E2: 10 000 cambios de estado hover/normal (CPU y basura).</li>
 *   <li>E3: abrir y cerrar 200 ventanas con botones (fugas de memoria).</li>
 *   <li>E4: navegar 100 veces entre dos ventanas con el botón (fugas al navegar).</li>
 * </ol>
 */
public final class MedicionRecursos {

    /** Pausa entre escenarios para que se distingan en VisualVM (ms). */
    private static final int PAUSA = Integer.getInteger("pausa", 3000);

    private MedicionRecursos() {
    }

    /**
     * Ejecuta los escenarios.
     *
     * @param args no se usan
     * @throws Exception si falla algo del EDT
     */
    public static void main(String[] args) throws Exception {
        System.out.println("PID " + ProcessHandle.current().pid()
                + ": conecta VisualVM a este proceso. Empieza en " + PAUSA / 1000 + " s.");
        Thread.sleep(PAUSA);
        var base = memoriaUsada();
        var filas = new ArrayList<String>();

        // E1
        var t = System.nanoTime();
        var botones = new ArrayList<BotonBymetoo>();
        SwingUtilities.invokeAndWait(() -> {
            for (int i = 0; i < 1000; i++) {
                botones.add(i % 2 == 0 ? new BotonSalir() : new BotonInicio());
            }
        });
        var ms = (System.nanoTime() - t) / 1_000_000;
        var e1 = memoriaUsada() - base;
        filas.add(fila("E1 crear 1 000 botones", ms, e1, String.format("%.1f KB/botón", e1 / 1024.0 / 1000)));
        Thread.sleep(PAUSA);

        // E2
        var b = botones.get(0);
        t = System.nanoTime();
        SwingUtilities.invokeAndWait(() -> {
            for (int i = 0; i < 10_000; i++) {
                raton(b, MouseEvent.MOUSE_ENTERED);
                raton(b, MouseEvent.MOUSE_EXITED);
            }
        });
        ms = (System.nanoTime() - t) / 1_000_000;
        filas.add(fila("E2 10 000 hover/normal", ms, memoriaUsada() - base,
                String.format("%.3f ms/cambio", ms / 20_000.0)));
        botones.clear();
        Thread.sleep(PAUSA);

        // E3
        var antes = memoriaUsada();
        t = System.nanoTime();
        for (int i = 0; i < 200; i++) {
            SwingUtilities.invokeAndWait(() -> {
                var f = new JFrame("E3");
                f.setLayout(new FlowLayout());
                for (int j = 0; j < 8; j++) {
                    var boton = new BotonResumen();
                    boton.setNavegacionAutomatica(false);
                    f.add(boton);
                }
                f.pack();
                f.setVisible(true);
                f.dispose();
            });
        }
        ms = (System.nanoTime() - t) / 1_000_000;
        filas.add(fila("E3 abrir/cerrar 200 ventanas", ms, memoriaUsada() - antes,
                "debe quedar cerca de 0 tras el GC"));
        Thread.sleep(PAUSA);

        // E4
        antes = memoriaUsada();
        t = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            var destino = (i % 2 == 0 ? VentanaPruebaB.class : VentanaPruebaA.class).getName();
            SwingUtilities.invokeAndWait(() -> {
                var f = new JFrame("E4");
                var boton = new BotonRepartos();
                boton.setVentanaDestino(destino);
                f.add(boton);
                f.pack();
                f.setVisible(true);
                boton.pulsar();
            });
            SwingUtilities.invokeAndWait(() -> Arrays.stream(Window.getWindows()).forEach(Window::dispose));
        }
        ms = (System.nanoTime() - t) / 1_000_000;
        filas.add(fila("E4 navegar 100 veces", ms, memoriaUsada() - antes,
                "debe quedar cerca de 0 tras el GC"));

        System.out.println();
        System.out.println(String.format("%-32s %10s %14s  %s", "Escenario", "Tiempo", "Memoria", "Nota"));
        filas.forEach(System.out::println);
        System.out.println("Hilos vivos al final: " + Thread.activeCount()
                + " · Ventanas sin liberar: " + Window.getWindows().length
                + " · Heap máximo: " + Runtime.getRuntime().maxMemory() / 1024 / 1024 + " MB");
        System.out.println("JVM: " + ManagementFactory.getRuntimeMXBean().getVmVersion());
        Thread.sleep(PAUSA);
        System.exit(0);
    }

    private static void raton(BotonBymetoo b, int id) {
        b.dispatchEvent(new MouseEvent(b, id, System.currentTimeMillis(), 0, 5, 5, 0, false));
    }

    /** Memoria usada del heap después de pedir un GC (aproximada). */
    private static long memoriaUsada() throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            System.gc();
            Thread.sleep(100);
        }
        var r = Runtime.getRuntime();
        return r.totalMemory() - r.freeMemory();
    }

    private static String fila(String nombre, long ms, long bytes, String nota) {
        return String.format("%-32s %7d ms %11.1f KB  %s", nombre, ms, bytes / 1024.0, nota);
    }
}
