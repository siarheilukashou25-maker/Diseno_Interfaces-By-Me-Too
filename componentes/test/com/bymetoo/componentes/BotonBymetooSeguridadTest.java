package com.bymetoo.componentes;

import static com.bymetoo.componentes.Utilidades.enEdt;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JLabel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

/**
 * DI-3.5.6 · Pruebas de seguridad básicas del componente.
 * <p>
 * El botón crea ventanas por reflexión a partir de un texto
 * ({@code ventanaDestino}) y muestra textos en un {@link JLabel}. Estas pruebas
 * comprueban que eso no se puede usar para ejecutar código que no es una
 * ventana, que los textos raros no rompen el botón y que se puede serializar
 * (es un JavaBean).
 * </p>
 * <p>
 * Los casos marcados como «hallazgo» fallan con el código actual y tienen su
 * issue: se dejan así a propósito, igual que pasó con O-1 y O-2.
 * </p>
 */
@RunWith(JUnitPlatform.class)
@DisplayName("BotonBymetoo · seguridad básica")
public class BotonBymetooSeguridadTest {

    private static final String CON_CONSTRUCTOR = ClaseNoVentana.class.getName() + "$ConConstructor";
    private static final String CON_STATIC = ClaseNoVentana.class.getName() + "$ConStatic";

    private BotonBymetoo boton;

    @BeforeEach
    void crearBoton() {
        boton = enEdt(BotonRepartos::new);
    }

    @Test
    @DisplayName("SEG-01 una clase que no es ventana no se llega a crear (constructor)")
    void seg01_noCreaClasesQueNoSonVentanas() {
        var antes = ClaseNoVentana.CREADA.get();
        enEdt(() -> boton.setVentanaDestino(CON_CONSTRUCTOR));
        assertFalse(boton.existeVentanaDestino(), "existeVentanaDestino() debe ser false");
        assertThrows(IllegalStateException.class, () -> enEdt(boton::navegar));
        assertEquals(antes, ClaseNoVentana.CREADA.get(), "No se debe llamar a su constructor");
    }

    @Test
    @DisplayName("SEG-02 (hallazgo) una clase que no es ventana no se inicializa (bloque static)")
    void seg02_noInicializaClasesQueNoSonVentanas() {
        enEdt(() -> boton.setVentanaDestino(CON_STATIC));
        assertThrows(IllegalStateException.class, () -> enEdt(boton::navegar));
        assertEquals(0, ClaseNoVentana.INICIALIZADA.get(),
                "Class.forName(nombre, true, ...) ejecuta el bloque static antes de comprobar "
                + "que es una ventana; debería cargarse con initialize = false");
    }

    @ParameterizedTest(name = "SEG-03 ventanaDestino peligrosa o rara: \"{0}\"")
    @ValueSource(strings = {"java.lang.Runtime", "java.lang.ProcessBuilder", "../../etc/passwd",
        "C:\\Windows\\System32\\cmd.exe", "javax.swing.JPanel", "vista.MainView;rm -rf /", " "})
    void seg03_destinosRaros(String destino) {
        enEdt(() -> boton.setVentanaDestino(destino));
        assertDoesNotThrow(() -> boton.existeVentanaDestino());
        assertFalse(boton.existeVentanaDestino(), "No es una ventana");
        if (!boton.getVentanaDestino().isEmpty()) {
            assertThrows(IllegalStateException.class, () -> enEdt(boton::navegar),
                    "Solo debe fallar con IllegalStateException, sin crear nada");
        }
    }

    @Test
    @DisplayName("SEG-04 (hallazgo) un texto que empieza por <html> se muestra tal cual")
    void seg04_textoHtml() {
        enEdt(() -> boton.setTexto("<html><img src='http://ejemplo.invalid/x.png'>Hola</html>"));
        var etiqueta = enEdt(() -> (JLabel) boton.getComponent(0));
        var html = enEdt(() -> etiqueta.getClientProperty("html"));
        assertEquals(null, html,
                "JLabel interpreta el HTML: si el texto viene del usuario (p. ej. nombre de un "
                + "grupo) puede cambiar el aspecto o cargar imágenes de Internet. "
                + "Solución: etiqueta.putClientProperty(\"html.disable\", Boolean.TRUE)");
    }

    @Test
    @DisplayName("SEG-05 un texto enorme (100 000 caracteres) no cuelga el botón")
    void seg05_textoEnorme() {
        var enorme = "A".repeat(100_000);
        assertDoesNotThrow(() -> enEdt(() -> boton.setTexto(enorme)));
        assertDoesNotThrow(() -> BotonBymetooValoresTest.pintar(boton));
    }

    @Test
    @DisplayName("SEG-06 se puede serializar y deserializar (JavaBean) sin perder datos")
    void seg06_serializacion() throws Exception {
        enEdt(() -> {
            boton.setTexto("Mis repartos");
            boton.setRadioBorde(9);
            boton.addBotonBymetooListener(new BotonBymetooAdapter() { });
        });
        var bytes = new ByteArrayOutputStream();
        try (var out = new ObjectOutputStream(bytes)) {
            enEdt(() -> {
                try {
                    out.writeObject(boton);
                } catch (java.io.IOException e) {
                    throw new IllegalStateException(e);
                }
            });
        }
        BotonBymetoo copia;
        try (var in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            copia = (BotonBymetoo) in.readObject();
        }
        assertEquals("Mis repartos", copia.getTexto());
        assertEquals(9, copia.getRadioBorde());
        assertTrue(copia instanceof BotonRepartos);
    }
}
