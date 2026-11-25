package com.bymetoo.componentes;

import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import javax.swing.SwingUtilities;

/**
 * Métodos de ayuda comunes a todas las pruebas del componente.
 * <p>
 * Swing no es seguro entre hilos, así que todo lo que toca un botón se
 * ejecuta en el EDT con {@link #enEdt(Callable)} o {@link #enEdt(Runnable)}.
 * </p>
 */
final class Utilidades {

    private Utilidades() {
    }

    /**
     * Las 8 subclases del componente, para las pruebas parametrizadas.
     *
     * @return un botón nuevo de cada tipo, creado en el EDT
     */
    static Stream<BotonBymetoo> todosLosBotones() {
        return Stream.of(
                enEdt(BotonInicio::new),
                enEdt(BotonPerfil::new),
                enEdt(BotonCompartir::new),
                enEdt(BotonGastos::new),
                enEdt(BotonRepartos::new),
                enEdt(BotonNuevaCompra::new),
                enEdt(BotonResumen::new),
                enEdt(BotonSalir::new));
    }

    /**
     * Un botón de cada estilo: MENU, CONTORNO_SUAVE y CONTORNO.
     *
     * @return tres botones nuevos, creados en el EDT
     */
    static Stream<BotonBymetoo> unoDeCadaEstilo() {
        return Stream.of(
                enEdt(BotonInicio::new),
                enEdt(BotonCompartir::new),
                enEdt(BotonSalir::new));
    }

    /**
     * Ejecuta el código en el EDT y devuelve su resultado. Si el código lanza
     * una excepción, se vuelve a lanzar en el hilo del test para que
     * {@code assertThrows} la pueda ver.
     */
    static <T> T enEdt(Callable<T> codigo) {
        if (SwingUtilities.isEventDispatchThread()) {
            try {
                return codigo.call();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        var resultado = new AtomicReference<T>();
        try {
            SwingUtilities.invokeAndWait(() -> {
                try {
                    resultado.set(codigo.call());
                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            });
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException re) {
                throw re;
            }
            if (e.getCause() instanceof Error err) {
                throw err;
            }
            throw new IllegalStateException(e.getCause());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
        return resultado.get();
    }

    /** Igual que {@link #enEdt(Callable)} pero sin resultado. */
    static void enEdt(Runnable codigo) {
        enEdt(() -> {
            codigo.run();
            return null;
        });
    }

    /** Simula que el puntero entra en el botón. */
    static void entrarRaton(BotonBymetoo boton) {
        enviarRaton(boton, MouseEvent.MOUSE_ENTERED, MouseEvent.NOBUTTON, 5, 5);
    }

    /** Simula que el puntero sale del botón. */
    static void salirRaton(BotonBymetoo boton) {
        enviarRaton(boton, MouseEvent.MOUSE_EXITED, MouseEvent.NOBUTTON, -5, -5);
    }

    /**
     * Simula un clic: pulsar y soltar con el botón indicado en (x, y).
     */
    static void clic(BotonBymetoo boton, int botonRaton, int x, int y) {
        enviarRaton(boton, MouseEvent.MOUSE_PRESSED, botonRaton, x, y);
        enviarRaton(boton, MouseEvent.MOUSE_RELEASED, botonRaton, x, y);
    }

    private static void enviarRaton(BotonBymetoo boton, int id, int botonRaton, int x, int y) {
        enEdt(() -> boton.dispatchEvent(new MouseEvent(
                boton, id, System.currentTimeMillis(), 0, x, y, 1, false, botonRaton)));
    }
}
