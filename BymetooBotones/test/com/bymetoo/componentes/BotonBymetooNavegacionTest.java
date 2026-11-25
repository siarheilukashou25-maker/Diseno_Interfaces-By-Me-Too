package com.bymetoo.componentes;

import static com.bymetoo.componentes.Utilidades.enEdt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.Test;

/**
 * DI-3.3.1 · Navegación entre ventanas (casos N-* del plan DI-3.1.3). Usa las
 * ventanas falsas {@link VentanaPruebaA} y {@link VentanaPruebaB}.
 * <p>
 * {@code @RunWith(JUnitPlatform.class)} permite ejecutarlo desde NetBeans
 * (Alt+F6): los proyectos Ant de NetBeans solo lanzan tests con el runner de
 * JUnit 4, y este puente ejecuta por debajo los tests de JUnit 5.
 * </p>
 */
@RunWith(JUnitPlatform.class)
@DisplayName("BotonBymetoo · navegación")
public class BotonBymetooNavegacionTest {

    private static final String A = VentanaPruebaA.class.getName();
    private static final String B = VentanaPruebaB.class.getName();

    private BotonBymetoo boton;

    @BeforeEach
    void crearBoton() {
        boton = enEdt(BotonRepartos::new);
    }

    @AfterEach
    void cerrarTodas() {
        enEdt(() -> Arrays.stream(Window.getWindows()).forEach(Window::dispose));
    }

    @Test
    @DisplayName("N-01 existeVentanaDestino() es false con clase inexistente o vacía")
    void n01_noExiste() {
        enEdt(() -> boton.setVentanaDestino("vista.NoExiste"));
        assertFalse(boton.existeVentanaDestino());
        enEdt(() -> boton.setVentanaDestino(""));
        assertFalse(boton.existeVentanaDestino());
    }

    @Test
    @DisplayName("N-02 existeVentanaDestino() solo es true para ventanas")
    void n02_soloVentanas() {
        enEdt(() -> boton.setVentanaDestino(B));
        assertTrue(boton.existeVentanaDestino());
        enEdt(() -> boton.setVentanaDestino("java.lang.String"));
        assertFalse(boton.existeVentanaDestino());
    }

    @Test
    @DisplayName("N-03 pulsar abre la ventana destino y cierra la actual")
    void n03_navegaYCierra() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Necesita pantalla");
        var origen = mostrarEn(new VentanaPruebaA(), B);
        enEdt(boton::pulsar);
        assertTrue(hayVisible(VentanaPruebaB.class), "B abierta");
        assertFalse(enEdt(origen::isDisplayable), "A cerrada");
    }

    @Test
    @DisplayName("N-04 con cerrarVentanaActual = false la actual sigue abierta")
    void n04_noCierra() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Necesita pantalla");
        var origen = mostrarEn(new VentanaPruebaA(), B);
        enEdt(() -> {
            boton.setCerrarVentanaActual(false);
            boton.pulsar();
        });
        assertTrue(hayVisible(VentanaPruebaB.class), "B abierta");
        assertTrue(enEdt(origen::isVisible), "A sigue abierta");
    }

    @Test
    @DisplayName("N-05 dentro de su ventana destino se pone ACTIVO")
    void n05_activoEnSuVentana() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Necesita pantalla");
        mostrarEn(new VentanaPruebaA(), A);
        assertTrue(boton.isActivo());
        assertEquals(EstadoBoton.ACTIVO, boton.getEstado());
    }

    @Test
    @DisplayName("N-06 destino inexistente: se deshabilita con tooltip")
    void n06_destinoInexistente() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Necesita pantalla");
        mostrarEn(new VentanaPruebaA(), "vista.NoExiste");
        assertFalse(boton.isEnabled());
        assertEquals(EstadoBoton.DESHABILITADO, boton.getEstado());
        assertEquals("Pantalla no disponible: vista.NoExiste", boton.getToolTipText());
    }

    @Test
    @DisplayName("N-07 navegar() sin destino devuelve null")
    void n07_sinDestino() {
        enEdt(() -> boton.setVentanaDestino(""));
        assertNull(enEdt(boton::navegar));
    }

    @Test
    @DisplayName("N-08 ventana sin constructor vacío: IllegalStateException clara")
    void n08_sinConstructorVacio() {
        enEdt(() -> boton.setVentanaDestino(SinConstructorVacio.class.getName()));
        var error = assertThrows(IllegalStateException.class, () -> enEdt(boton::navegar));
        assertTrue(error.getMessage().contains("constructor"), error.getMessage());
    }

    @Test
    @DisplayName("N-09 navegar() estando ya en el destino devuelve la ventana actual")
    void n09_yaEnDestino() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Necesita pantalla");
        var origen = mostrarEn(new VentanaPruebaA(), A);
        var resultado = enEdt(boton::navegar);
        assertSame(origen, resultado);
        assertEquals(1, Arrays.stream(Window.getWindows())
                .filter(w -> w instanceof VentanaPruebaA && w.isDisplayable()).count());
    }

    @Test
    @DisplayName("Crear la ventana devuelve una instancia de la clase indicada")
    void crearVentana() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Necesita pantalla");
        var w = enEdt(() -> boton.crearVentana(B));
        assertInstanceOf(VentanaPruebaB.class, w);
    }

    // ------------------------------------------------------------------

    /** Pone el botón en la ventana, fija el destino y la muestra. */
    private Window mostrarEn(Window ventana, String destino) {
        return enEdt(() -> {
            boton.setVentanaDestino(destino);
            ventana.add(boton);
            ventana.pack();
            ventana.setVisible(true);
            return ventana;
        });
    }

    private static boolean hayVisible(Class<? extends Window> tipo) {
        return enEdt(() -> Arrays.stream(Window.getWindows())
                .anyMatch(w -> tipo.isInstance(w) && w.isVisible()));
    }
}
