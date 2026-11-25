package com.bymetoo.componentes;

import static com.bymetoo.componentes.Utilidades.enEdt;
import static com.bymetoo.componentes.Utilidades.entrarRaton;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.Test;

/**
 * DI-3.3.1 · Pruebas de las observaciones O-1 y O-2 del plan DI-3.1.3.
 * <p>
 * Comprueban requisitos del diseño (Sprint 2 y bocetos de DI-3.1.2) que hoy
 * <b>no se cumplen</b>: están pensadas para fallar hasta que se corrija el
 * componente. Cada fallo tiene su issue (DI-3.3.6).
 * </p>
 * <p>
 * {@code @RunWith(JUnitPlatform.class)} permite ejecutarlo desde NetBeans
 * (Alt+F6): los proyectos Ant de NetBeans solo lanzan tests con el runner de
 * JUnit 4, y este puente ejecuta por debajo los tests de JUnit 5.
 * </p>
 */
@RunWith(JUnitPlatform.class)
@DisplayName("BotonBymetoo · hallazgos de QA")
public class BotonBymetooHallazgosTest {

    @Test
    @DisplayName("O-1a CONTORNO: el hover se distingue del estado normal")
    void o1a_hoverVisibleEnContorno() {
        var b = enEdt(BotonSalir::new);
        var normal = BotonBymetooEstadosTest.fondo(BotonBymetooValoresTest.pintar(b));
        entrarRaton(b);
        var hover = BotonBymetooEstadosTest.fondo(BotonBymetooValoresTest.pintar(b));
        assertNotEquals(normal.getRGB(), hover.getRGB(),
                "El fondo en HOVER es igual que en NORMAL: el usuario no ve el hover");
    }

    @Test
    @DisplayName("O-1b MENU: activo se distingue de hover")
    void o1b_activoDistintoDeHoverEnMenu() {
        var b = enEdt(BotonInicio::new);
        entrarRaton(b);
        var hover = BotonBymetooEstadosTest.fondo(BotonBymetooValoresTest.pintar(b));
        enEdt(() -> b.setActivo(true));
        var activo = BotonBymetooEstadosTest.fondo(BotonBymetooValoresTest.pintar(b));
        assertNotEquals(hover.getRGB(), activo.getRGB(),
                "En el menú HOVER y ACTIVO se pintan igual");
    }

    @Test
    @DisplayName("O-2 el texto deshabilitado se distingue del normal")
    void o2_textoDeshabilitadoDistinto() {
        var b = enEdt(BotonSalir::new);
        assertNotEquals(b.getColorTexto(), b.getColorTextoDeshabilitado(),
                "colorTextoDeshabilitado es igual que colorTexto");
    }
}
