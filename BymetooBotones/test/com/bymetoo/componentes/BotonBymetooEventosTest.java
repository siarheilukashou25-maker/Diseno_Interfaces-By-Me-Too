package com.bymetoo.componentes;

import static com.bymetoo.componentes.Utilidades.enEdt;
import static com.bymetoo.componentes.Utilidades.entrarRaton;
import static com.bymetoo.componentes.Utilidades.salirRaton;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.awt.GraphicsEnvironment;
import java.util.List;
import javax.swing.JFrame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.Test;

/**
 * DI-3.3.1 · Eventos del componente (casos EV-* del plan DI-3.1.3):
 * {@code estadoCambiado}, {@code botonPulsado}, cancelación de la navegación y
 * {@code PropertyChangeEvent}.
 * <p>
 * {@code @RunWith(JUnitPlatform.class)} permite ejecutarlo desde NetBeans
 * (Alt+F6): los proyectos Ant de NetBeans solo lanzan tests con el runner de
 * JUnit 4, y este puente ejecuta por debajo los tests de JUnit 5.
 * </p>
 */
@RunWith(JUnitPlatform.class)
@DisplayName("BotonBymetoo · eventos")
public class BotonBymetooEventosTest {

    private BotonBymetoo boton;
    private ListenerContador contador;
    private JFrame ventana;

    @BeforeEach
    void preparar() {
        boton = enEdt(BotonSalir::new);
        contador = new ListenerContador();
        enEdt(() -> {
            boton.setNavegacionAutomatica(false);
            boton.addBotonBymetooListener(contador);
            boton.addPropertyChangeListener(contador);
        });
    }

    @AfterEach
    void cerrarVentana() {
        if (ventana != null) {
            enEdt(ventana::dispose);
        }
    }

    @Test
    @DisplayName("EV-01 setActivo(true) lanza estadoCambiado NORMAL→ACTIVO")
    void ev01_estadoCambiado() {
        enEdt(() -> boton.setActivo(true));
        assertEquals(1, contador.estados.size());
        var e = contador.estados.get(0);
        assertEquals(BotonBymetooEvent.Tipo.CAMBIO_ESTADO, e.getTipo());
        assertEquals(EstadoBoton.NORMAL, e.getEstadoAnterior());
        assertEquals(EstadoBoton.ACTIVO, e.getEstadoNuevo());
        assertSame(boton, e.getBoton());
        assertSame(boton, e.getSource());
    }

    @Test
    @DisplayName("EV-02 setActivo(true) dos veces avisa una sola vez")
    void ev02_mismoEstadoNoRepite() {
        enEdt(() -> {
            boton.setActivo(true);
            boton.setActivo(true);
        });
        assertEquals(1, contador.estados.size());
    }

    @Test
    @DisplayName("EV-03 entrar y salir con el ratón: NORMAL→HOVER y HOVER→NORMAL")
    void ev03_hover() {
        entrarRaton(boton);
        salirRaton(boton);
        assertEquals(List.of("EC:NORMAL->HOVER", "EC:HOVER->NORMAL"), contador.orden);
    }

    @Test
    @DisplayName("EV-04 pulsar() lanza botonPulsado con la ventana destino")
    void ev04_botonPulsado() {
        enEdt(boton::pulsar);
        assertEquals(1, contador.pulsados.size());
        var e = contador.pulsados.get(0);
        assertEquals(BotonBymetooEvent.Tipo.PULSADO, e.getTipo());
        assertEquals(boton.getVentanaDestino(), e.getVentanaDestino());
    }

    @Test
    @DisplayName("EV-05 orden de eventos al pulsar dentro de una ventana")
    void ev05_ordenDentroDeVentana() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Necesita pantalla");
        enEdt(() -> {
            ventana = new JFrame("Prueba EV-05");
            ventana.add(boton);
            ventana.pack();
            ventana.setVisible(true);
        });
        enEdt(boton::pulsar);
        assertEquals(List.of("EC:NORMAL->ACTIVO", "BP", "EC:ACTIVO->NORMAL"), contador.orden);
        assertEquals(EstadoBoton.NORMAL, boton.getEstado());
    }

    @Test
    @DisplayName("EV-05b fuera de una ventana el botón se queda ACTIVO tras pulsar")
    void ev05b_fueraDeVentanaSeQuedaActivo() {
        enEdt(boton::pulsar);
        // Comportamiento actual, anotado en el plan para decidirlo con Roberto.
        assertEquals(EstadoBoton.ACTIVO, boton.getEstado());
    }

    @Test
    @DisplayName("EV-06 un botón deshabilitado no lanza botonPulsado")
    void ev06_deshabilitadoNoPulsa() {
        enEdt(() -> {
            boton.setEnabled(false);
            boton.pulsar();
        });
        assertEquals(0, contador.pulsados.size());
    }

    @Test
    @DisplayName("EV-07 dos listeners reciben el evento")
    void ev07_dosListeners() {
        var otro = new ListenerContador();
        enEdt(() -> {
            boton.addBotonBymetooListener(otro);
            boton.pulsar();
        });
        assertEquals(1, contador.pulsados.size());
        assertEquals(1, otro.pulsados.size());
    }

    @Test
    @DisplayName("EV-08 un listener eliminado no recibe nada")
    void ev08_removeListener() {
        enEdt(() -> {
            boton.removeBotonBymetooListener(contador);
            boton.pulsar();
        });
        assertEquals(0, contador.pulsados.size());
        assertEquals(0, contador.estados.size());
    }

    @Test
    @DisplayName("EV-09 cancelarNavegacion() marca el evento como cancelado")
    void ev09_cancelarNavegacion() {
        contador.cancelarNavegacion = true;
        enEdt(() -> {
            boton.setNavegacionAutomatica(true);
            boton.setVentanaDestino("vista.NoExiste");
            boton.pulsar(); // si no se cancelara, lanzaría IllegalStateException
        });
        assertEquals(1, contador.pulsados.size());
        assertTrue(contador.pulsados.get(0).isNavegacionCancelada());
    }

    @Test
    @DisplayName("EV-11 setTexto lanza PropertyChangeEvent con valores correctos")
    void ev11_propertyChangeTexto() {
        enEdt(() -> boton.setTexto("X"));
        var cambios = contador.propiedades.stream()
                .filter(p -> BotonBymetoo.PROP_TEXTO.equals(p.getPropertyName())).toList();
        assertEquals(1, cambios.size());
        assertEquals("Salir", cambios.get(0).getOldValue());
        assertEquals("X", cambios.get(0).getNewValue());
    }

    @Test
    @DisplayName("EV-12 el mismo valor dos veces avisa una sola vez")
    void ev12_mismoValor() {
        enEdt(() -> {
            boton.setTexto("X");
            boton.setTexto("X");
        });
        assertEquals(1, contador.cambiosDe(BotonBymetoo.PROP_TEXTO));
    }

    @Test
    @DisplayName("EV-13 PROP_ESTADO avisa NORMAL→ACTIVO")
    void ev13_propEstado() {
        enEdt(() -> boton.setActivo(true));
        var cambio = contador.propiedades.stream()
                .filter(p -> BotonBymetoo.PROP_ESTADO.equals(p.getPropertyName()))
                .findFirst().orElseThrow();
        assertEquals(EstadoBoton.NORMAL, cambio.getOldValue());
        assertEquals(EstadoBoton.ACTIVO, cambio.getNewValue());
    }

    @Test
    @DisplayName("EV-14 un PropertyChangeListener recibe cada cambio una sola vez")
    void ev14_sinDuplicados() {
        enEdt(() -> {
            boton.setTexto("X");
            boton.setRadioBorde(4);
            boton.setActivo(true);
        });
        assertEquals(1, contador.cambiosDe(BotonBymetoo.PROP_TEXTO));
        assertEquals(1, contador.cambiosDe(BotonBymetoo.PROP_RADIO_BORDE));
        assertEquals(1, contador.cambiosDe(BotonBymetoo.PROP_ACTIVO));
        assertEquals(1, contador.cambiosDe(BotonBymetoo.PROP_ESTADO));
    }

    @Test
    @DisplayName("CI-38 un listener puede darse de baja dentro de botonPulsado")
    void ci38_bajaDentroDelEvento() {
        var seDaDeBaja = new ListenerContador() {
            @Override
            public void botonPulsado(BotonBymetooEvent e) {
                super.botonPulsado(e);
                e.getBoton().removeBotonBymetooListener(this);
            }
        };
        enEdt(() -> {
            boton.addBotonBymetooListener(seDaDeBaja);
            boton.pulsar();
            boton.pulsar();
        });
        assertEquals(1, seDaDeBaja.pulsados.size());
        assertEquals(2, contador.pulsados.size());
    }
}
