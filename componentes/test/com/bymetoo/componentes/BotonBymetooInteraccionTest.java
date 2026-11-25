package com.bymetoo.componentes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.MouseButton;
import org.assertj.swing.core.Robot;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.timing.Condition;
import org.assertj.swing.timing.Pause;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

/**
 * DI-3.2.3 · Casos de interacción (CI-*) que no cubren los demás tests.
 * <p>
 * El resto de casos CI ya están cubiertos por los tests E, EV, N, L y G (ver
 * la tabla de trazabilidad en {@code docs/qa/casos-interaccion.md}). Aquí se
 * prueban con clics y teclas reales (AssertJ Swing) los que faltaban.
 * </p>
 * <p>
 * Necesitan pantalla: sin ella se saltan. <b>No tocar el ratón mientras se
 * ejecutan.</b> {@code @RunWith(JUnitPlatform.class)} permite lanzarlos desde
 * NetBeans (Alt+F6).
 * </p>
 */
@RunWith(JUnitPlatform.class)
@Tag("gui")
@DisplayName("BotonBymetoo · casos de interacción (DI-3.2.3)")
public class BotonBymetooInteraccionTest {

    private static final String B = VentanaPruebaB.class.getName();

    private Robot robot;
    private FrameFixture ventana;
    private BotonBymetoo gastos;
    private BotonBymetoo repartos;
    private JButton otro;
    private ListenerContador enGastos;
    private ListenerContador enRepartos;

    @BeforeEach
    void abrirVentana() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Necesita pantalla");
        robot = BasicRobot.robotWithNewAwtHierarchy();
        enGastos = new ListenerContador();
        enRepartos = new ListenerContador();
        JFrame frame = GuiActionRunner.execute(() -> {
            var f = new JFrame("Casos de interacción");
            f.setLayout(new FlowLayout());
            gastos = new BotonGastos();
            repartos = new BotonRepartos();
            for (var b : new BotonBymetoo[] {gastos, repartos}) {
                b.setNavegacionAutomatica(false);
                f.add(b);
            }
            gastos.addBotonBymetooListener(enGastos);
            repartos.addBotonBymetooListener(enRepartos);
            otro = new JButton("Otro");
            f.add(otro);
            f.setSize(520, 120);
            return f;
        });
        ventana = new FrameFixture(robot, frame);
        ventana.show();
        ventana.requireVisible();
        ventana.focus();
        robot.moveMouse(otro);
        robot.waitForIdle();
        limpiar();
    }

    @AfterEach
    void cerrar() {
        if (ventana != null) {
            ventana.cleanUp();
        }
        GuiActionRunner.execute(() -> Arrays.stream(Window.getWindows()).forEach(Window::dispose));
    }

    @Test
    @DisplayName("CI-08 clic con navegación real cancelada: no cambia de ventana")
    void ci08_navegacionCancelada() {
        enGastos.cancelarNavegacion = true;
        GuiActionRunner.execute(() -> {
            gastos.setVentanaDestino(B);
            gastos.setNavegacionAutomatica(true);
        });
        robot.click(gastos, MouseButton.LEFT_BUTTON);
        robot.waitForIdle();
        assertEquals(1, enGastos.pulsados.size());
        assertTrue(enGastos.pulsados.get(0).isNavegacionCancelada());
        assertEquals(0, ventanasAbiertas(VentanaPruebaB.class), "No se abre B");
        assertTrue(GuiActionRunner.execute(() -> ventana.target().isDisplayable()), "La ventana sigue abierta");
        assertEquals(EstadoBoton.HOVER, estado(gastos), "Vuelve a su estado anterior (puntero encima)");
    }

    @Test
    @DisplayName("CI-09 al hacer clic el botón recibe el foco")
    void ci09_focoAlPulsar() {
        robot.click(gastos, MouseButton.LEFT_BUTTON);
        robot.waitForIdle();
        assertTrue(GuiActionRunner.execute(gastos::isFocusOwner));
    }

    @Test
    @DisplayName("CI-11 doble clic con navegación real abre una sola ventana destino")
    void ci11_dobleClicNavegacion() {
        GuiActionRunner.execute(() -> {
            gastos.setVentanaDestino(B);
            gastos.setNavegacionAutomatica(true);
        });
        robot.click(gastos, MouseButton.LEFT_BUTTON, 2);
        robot.waitForIdle();
        assertEquals(1, ventanasAbiertas(VentanaPruebaB.class), "Solo una VentanaPruebaB");
    }

    @Test
    @DisplayName("CI-12 doble clic sobre un botón activo: sigue ACTIVO")
    void ci12_dobleClicActivo() {
        GuiActionRunner.execute(() -> gastos.setActivo(true));
        limpiar();
        robot.click(gastos, MouseButton.LEFT_BUTTON, 2);
        robot.waitForIdle();
        assertEquals(2, enGastos.pulsados.size());
        assertEquals(EstadoBoton.ACTIVO, estado(gastos));
    }

    @Test
    @DisplayName("CI-13 10 clics rápidos: se procesan todos y termina en HOVER")
    void ci13_diezClics() {
        for (int i = 0; i < 10; i++) {
            robot.click(gastos, MouseButton.LEFT_BUTTON);
        }
        robot.waitForIdle();
        assertEquals(10, enGastos.pulsados.size());
        assertEquals(EstadoBoton.HOVER, estado(gastos));
    }

    @Test
    @DisplayName("CI-14 10 clics, quitar el listener y 1 clic más: no recibe el último")
    void ci14_quitarListener() {
        for (int i = 0; i < 10; i++) {
            robot.click(gastos, MouseButton.LEFT_BUTTON);
        }
        GuiActionRunner.execute(() -> gastos.removeBotonBymetooListener(enGastos));
        robot.click(gastos, MouseButton.LEFT_BUTTON);
        robot.waitForIdle();
        assertEquals(10, enGastos.pulsados.size());
    }

    @Test
    @DisplayName("CI-15 Gastos → Repartos → Gastos: ninguno se queda ACTIVO")
    void ci15_alternarBotones() {
        robot.click(gastos, MouseButton.LEFT_BUTTON);
        robot.click(repartos, MouseButton.LEFT_BUTTON);
        robot.click(gastos, MouseButton.LEFT_BUTTON);
        robot.waitForIdle();
        assertEquals(2, enGastos.pulsados.size());
        assertEquals(1, enRepartos.pulsados.size());
        assertEquals(EstadoBoton.NORMAL, estado(repartos));
        assertEquals(EstadoBoton.HOVER, estado(gastos));
    }

    @Test
    @DisplayName("CI-16 50 pulsaciones de Espacio: sin errores y termina en NORMAL")
    void ci16_cincuentaEspacios() {
        robot.focusAndWaitForFocusGain(gastos);
        // El puntero fuera del botón, para probar solo el teclado
        robot.moveMouse(otro);
        robot.waitForIdle();
        for (int i = 0; i < 50; i++) {
            robot.pressAndReleaseKey(KeyEvent.VK_SPACE);
        }
        robot.waitForIdle();
        assertEquals(50, enGastos.pulsados.size());
        assertEquals(EstadoBoton.NORMAL, estado(gastos));
    }

    @Test
    @DisplayName("CI-20 entrar y salir 5 veces: 10 cambios alternos y termina en NORMAL")
    void ci20_cincoHover() {
        for (int i = 0; i < 5; i++) {
            robot.moveMouse(gastos);
            esperarEstado(gastos, EstadoBoton.HOVER);
            robot.moveMouse(otro);
            esperarEstado(gastos, EstadoBoton.NORMAL);
        }
        robot.waitForIdle();
        assertEquals(10, enGastos.estados.size());
        for (int i = 0; i < 10; i++) {
            var esperado = i % 2 == 0 ? EstadoBoton.HOVER : EstadoBoton.NORMAL;
            assertEquals(esperado, enGastos.estados.get(i).getEstadoNuevo(), "cambio " + i);
        }
        assertEquals(EstadoBoton.NORMAL, estado(gastos));
    }

    @Test
    @DisplayName("CI-21 / CI-22 hover y salida sobre un botón activo: sigue ACTIVO")
    void ci21ci22_hoverEnActivo() {
        GuiActionRunner.execute(() -> gastos.setActivo(true));
        limpiar();
        robot.moveMouse(gastos);
        robot.waitForIdle();
        assertEquals(EstadoBoton.ACTIVO, estado(gastos), "CI-21");
        robot.moveMouse(otro);
        robot.waitForIdle();
        assertEquals(EstadoBoton.ACTIVO, estado(gastos), "CI-22");
        assertEquals(0, enGastos.estados.size(), "Sin cambios de estado");
    }

    @Test
    @DisplayName("CI-24 deshabilitado: doble clic y 10 clics no pulsan")
    void ci24_deshabilitadoVariosClics() {
        GuiActionRunner.execute(() -> gastos.setEnabled(false));
        robot.click(gastos, MouseButton.LEFT_BUTTON, 2);
        for (int i = 0; i < 10; i++) {
            robot.click(gastos, MouseButton.LEFT_BUTTON);
        }
        robot.waitForIdle();
        assertEquals(0, enGastos.pulsados.size());
        assertEquals(EstadoBoton.DESHABILITADO, estado(gastos));
    }

    @Test
    @DisplayName("CI-32 volver a habilitar con el puntero encima: NORMAL y el hover vuelve al mover")
    void ci32_rehabilitarConPunteroEncima() {
        robot.moveMouse(gastos);
        robot.waitForIdle();
        GuiActionRunner.execute(() -> gastos.setEnabled(false));
        GuiActionRunner.execute(() -> gastos.setEnabled(true));
        assertEquals(EstadoBoton.NORMAL, estado(gastos));
        robot.moveMouse(otro);
        robot.waitForIdle();
        robot.moveMouse(gastos);
        esperarEstado(gastos, EstadoBoton.HOVER);
        assertEquals(EstadoBoton.HOVER, estado(gastos));
    }

    @Test
    @DisplayName("CI-34 setEnabled(false) dos veces: un solo cambio de estado")
    void ci34_deshabilitarDosVeces() {
        GuiActionRunner.execute(() -> {
            gastos.setEnabled(false);
            gastos.setEnabled(false);
        });
        assertEquals(1, enGastos.estados.size());
    }

    private void limpiar() {
        for (var c : new ListenerContador[] {enGastos, enRepartos}) {
            c.estados.clear();
            c.pulsados.clear();
            c.propiedades.clear();
            c.orden.clear();
        }
    }

    /**
     * Espera (máx. 2 s) a que el botón llegue al estado indicado. El robot mueve
     * el ratón de verdad y el sistema puede tardar un poco en entregar el evento.
     */
    private void esperarEstado(BotonBymetoo b, EstadoBoton esperado) {
        robot.waitForIdle();
        Pause.pause(new Condition("estado " + esperado) {
            @Override
            public boolean test() {
                return estado(b) == esperado;
            }
        }, 2000);
    }

    private static EstadoBoton estado(BotonBymetoo b) {
        return GuiActionRunner.execute(b::getEstado);
    }

    private static long ventanasAbiertas(Class<? extends Window> tipo) {
        return GuiActionRunner.execute(() -> Arrays.stream(Window.getWindows())
                .filter(w -> tipo.isInstance(w) && w.isShowing()).count());
    }
}
