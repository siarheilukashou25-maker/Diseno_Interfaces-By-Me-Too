package com.bymetoo.componentes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.MouseButton;
import org.assertj.swing.core.Robot;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

/**
 * DI-3.3.5 · Pruebas de interfaz con clics reales (AssertJ Swing).
 * <p>
 * A diferencia de los otros tests, que lanzan los eventos de ratón a mano,
 * aquí un {@link Robot} mueve el ratón de verdad, hace clic y pulsa teclas
 * sobre una ventana abierta, igual que haría un usuario. Se usa AssertJ Swing
 * porque TestFX solo sirve para JavaFX y el componente es Swing.
 * </p>
 * <p>
 * Necesitan pantalla: sin ella se saltan (assumption). <b>Mientras se
 * ejecutan no hay que tocar el ratón ni el teclado.</b>
 * </p>
 * <p>
 * {@code @RunWith(JUnitPlatform.class)} permite ejecutarlo desde NetBeans
 * (Alt+F6), igual que el resto de tests.
 * </p>
 */
@RunWith(JUnitPlatform.class)
@Tag("gui")
@DisplayName("BotonBymetoo · interfaz con clics reales (AssertJ Swing)")
public class BotonBymetooGuiTest {

    private static final String B = VentanaPruebaB.class.getName();

    private Robot robot;
    private FrameFixture ventana;
    private BotonBymetoo boton;
    private JButton otro;
    private ListenerContador contador;

    @BeforeEach
    void abrirVentana() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Necesita pantalla");
        robot = BasicRobot.robotWithNewAwtHierarchy();
        contador = new ListenerContador();
        JFrame frame = GuiActionRunner.execute(() -> {
            var f = new JFrame("Prueba GUI BotonBymetoo");
            f.setLayout(new FlowLayout());
            boton = new BotonSalir();
            boton.setName("botonSalir");
            boton.setNavegacionAutomatica(false);
            boton.addBotonBymetooListener(contador);
            otro = new JButton("Otro");
            otro.setName("otro");
            f.add(boton);
            f.add(otro);
            f.setSize(400, 120);
            return f;
        });
        ventana = new FrameFixture(robot, frame);
        ventana.show();
        ventana.requireVisible();
        // Calentamiento: el primer evento de ratón de una ventana recién abierta
        // a veces se pierde mientras el sistema le da el foco.
        ventana.focus();
        robot.moveMouse(otro);
        robot.waitForIdle();
        // El calentamiento puede haber cambiado el estado: empezamos limpios
        contador.estados.clear();
        contador.pulsados.clear();
    }

    @AfterEach
    void cerrar() {
        if (ventana != null) {
            ventana.cleanUp();
        }
        GuiActionRunner.execute(() -> Arrays.stream(Window.getWindows()).forEach(Window::dispose));
    }

    @Test
    @DisplayName("G-01 pasar el ratón por encima pone HOVER y al salir vuelve a NORMAL")
    void g01_hoverReal() {
        robot.moveMouse(boton);
        robot.waitForIdle();
        assertEquals(EstadoBoton.HOVER, estado());
        robot.moveMouse(otro);
        robot.waitForIdle();
        assertEquals(EstadoBoton.NORMAL, estado());
        assertTrue(contador.estados.size() >= 2, "Se han avisado los cambios de estado");
    }

    @Test
    @DisplayName("G-02 un clic izquierdo lanza botonPulsado una vez")
    void g02_clicIzquierdo() {
        robot.click(boton, MouseButton.LEFT_BUTTON);
        robot.waitForIdle();
        assertEquals(1, contador.pulsados.size());
    }

    @Test
    @DisplayName("G-03 el clic derecho y el central no pulsan el botón")
    void g03_otrosBotones() {
        robot.click(boton, MouseButton.RIGHT_BUTTON);
        robot.click(boton, MouseButton.MIDDLE_BUTTON);
        robot.waitForIdle();
        assertEquals(0, contador.pulsados.size());
    }

    @Test
    @DisplayName("G-04 con el foco, Espacio e Intro pulsan el botón")
    void g04_teclado() {
        robot.focusAndWaitForFocusGain(boton);
        robot.pressAndReleaseKey(KeyEvent.VK_SPACE);
        robot.pressAndReleaseKey(KeyEvent.VK_ENTER);
        robot.waitForIdle();
        assertEquals(2, contador.pulsados.size());
    }

    @Test
    @DisplayName("G-05 se llega al botón con Tab")
    void g05_tab() {
        robot.focusAndWaitForFocusGain(otro);
        robot.pressAndReleaseKey(KeyEvent.VK_TAB);
        robot.waitForIdle();
        assertTrue(GuiActionRunner.execute(boton::isFocusOwner), "El botón tiene el foco");
    }

    @Test
    @DisplayName("G-06 deshabilitado: ni clic ni teclado hacen nada")
    void g06_deshabilitado() {
        GuiActionRunner.execute(() -> boton.setEnabled(false));
        robot.click(boton, MouseButton.LEFT_BUTTON);
        robot.pressAndReleaseKey(KeyEvent.VK_SPACE);
        robot.waitForIdle();
        assertEquals(0, contador.pulsados.size());
        assertEquals(EstadoBoton.DESHABILITADO, estado());
    }

    @Test
    @DisplayName("G-07 apretar, salir del botón y soltar fuera no cuenta como clic")
    void g07_arrastrarFuera() {
        robot.moveMouse(boton);
        robot.pressMouse(MouseButton.LEFT_BUTTON);
        robot.moveMouse(otro);
        robot.releaseMouse(MouseButton.LEFT_BUTTON);
        robot.waitForIdle();
        assertEquals(0, contador.pulsados.size());
    }

    @Test
    @DisplayName("G-08 doble clic: se registra cuántas veces se pulsa (observación O-3)")
    void g08_dobleClic() {
        robot.click(boton, MouseButton.LEFT_BUTTON, 2);
        robot.waitForIdle();
        // Comportamiento actual: cada clic del doble clic cuenta (O-3 del plan).
        assertEquals(2, contador.pulsados.size());
    }

    @Test
    @DisplayName("G-09 un clic real abre la ventana destino y cierra la actual")
    void g09_navegacionReal() {
        GuiActionRunner.execute(() -> {
            boton.setVentanaDestino(B);
            boton.setNavegacionAutomatica(true);
        });
        robot.click(boton, MouseButton.LEFT_BUTTON);
        // WindowFinder espera (hasta 5 s) a que aparezca la ventana B
        var b = WindowFinder.findFrame(VentanaPruebaB.class).withTimeout(5000).using(robot);
        b.requireVisible();
        assertFalse(GuiActionRunner.execute(() -> ventana.target().isDisplayable()),
                "La ventana de origen se ha cerrado");
    }

    @Test
    @DisplayName("G-10 mantener pulsado se pinta como activo y al soltar dentro se pulsa")
    void g10_mantenerPulsado() {
        var centro = GuiActionRunner.execute(() -> new Point(boton.getWidth() / 2, boton.getHeight() / 2));
        robot.moveMouse(boton, centro);
        robot.pressMouse(MouseButton.LEFT_BUTTON);
        robot.waitForIdle();
        assertEquals(0, contador.pulsados.size(), "Todavía no se ha soltado");
        robot.releaseMouse(MouseButton.LEFT_BUTTON);
        robot.waitForIdle();
        assertEquals(1, contador.pulsados.size());
    }

    private EstadoBoton estado() {
        return GuiActionRunner.execute(boton::getEstado);
    }
}
