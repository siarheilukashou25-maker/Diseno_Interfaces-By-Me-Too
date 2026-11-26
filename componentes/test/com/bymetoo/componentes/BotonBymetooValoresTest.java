package com.bymetoo.componentes;

import static com.bymetoo.componentes.Utilidades.enEdt;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.swing.ImageIcon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * DI-3.3.1 · Valores por defecto (casos D-*) y getters/setters con valores
 * límite (casos L-*) del plan de pruebas DI-3.1.3.
 * <p>
 * {@code @RunWith(JUnitPlatform.class)} permite ejecutarlo desde NetBeans
 * (Alt+F6): los proyectos Ant de NetBeans solo lanzan tests con el runner de
 * JUnit 4, y este puente ejecuta por debajo los tests de JUnit 5.
 * </p>
 */
@RunWith(JUnitPlatform.class)
@DisplayName("BotonBymetoo · valores por defecto y propiedades")
public class BotonBymetooValoresTest {

    private static final Color FONDO = new Color(0xFA, 0xFA, 0xFA);
    private static final Color ACTIVO = new Color(0x81, 0xDA, 0xAB);
    private static final Color BORDE_SUAVE = new Color(0xE2, 0xE8, 0xE5);
    private static final Color TEXTO = new Color(0x1A, 0x20, 0x2C);
    /** Texto deshabilitado: el color del texto con alpha 100 (corrección de #190). */
    private static final Color TEXTO_DESHABILITADO = new Color(0x1A, 0x20, 0x2C, 100);
    /** Hover del menú: rgba(26, 32, 44, 0.08) (corrección de #189). */
    private static final Color HOVER_MENU = new Color(26, 32, 44, 20);
    /** Activo del menú: rgba(26, 32, 44, 0.14) (corrección de #189). */
    private static final Color ACTIVO_MENU = new Color(26, 32, 44, 36);

    private BotonBymetoo boton;

    @BeforeEach
    void crearBoton() {
        boton = enEdt(BotonSalir::new);
        enEdt(() -> boton.setNavegacionAutomatica(false));
    }

    // ------------------------------------------------------------------
    // 4.1 Valores por defecto
    // ------------------------------------------------------------------

    /** Datos esperados de cada uno de los 8 botones (tabla del apartado 1 del plan). */
    static Stream<Arguments> especificacion() {
        return Stream.of(
                datos(BotonInicio::new, "Inicio", EstiloBoton.MENU, "vista.MainView"),
                datos(BotonPerfil::new, "Perfil", EstiloBoton.MENU, "vista.UserView"),
                datos(BotonCompartir::new, "Compartir", EstiloBoton.CONTORNO_SUAVE, "vista.GroupView"),
                datos(BotonGastos::new, "Gastos", EstiloBoton.CONTORNO_SUAVE, "vista.GroupView"),
                datos(BotonRepartos::new, "Repartos", EstiloBoton.CONTORNO, "vista.RepartosView"),
                datos(BotonNuevaCompra::new, "Nueva compra", EstiloBoton.CONTORNO, "vista.CompraWindow"),
                datos(BotonResumen::new, "Resumen", EstiloBoton.CONTORNO, "vista.ResumenView"),
                datos(BotonSalir::new, "Salir", EstiloBoton.CONTORNO, "vista.LoginView"));
    }

    private static Arguments datos(Supplier<BotonBymetoo> fabrica, String texto,
            EstiloBoton estilo, String ventana) {
        BotonBymetoo b = enEdt(fabrica::get);
        return Arguments.of(b.getClass().getSimpleName(), b, texto, estilo, ventana);
    }

    @ParameterizedTest(name = "D-01..D-05 {0}")
    @MethodSource("especificacion")
    void d01aD05_estadoTextoEstiloYVentana(String nombre, BotonBymetoo b, String texto,
            EstiloBoton estilo, String ventana) {
        assertEquals(EstadoBoton.NORMAL, b.getEstado(), "D-01 estado");
        assertTrue(b.isEnabled(), "D-02 habilitado");
        assertFalse(b.isActivo(), "D-02 activo");
        assertEquals(texto, b.getTexto(), "D-03 texto");
        assertEquals(estilo, b.getEstilo(), "D-05 estilo");
        assertEquals(ventana, b.getVentanaDestino(), "D-04 ventana destino");
    }

    @ParameterizedTest(name = "D-06..D-11 {0}")
    @MethodSource("especificacion")
    void d06aD11_propiedadesPorDefecto(String nombre, BotonBymetoo b, String texto,
            EstiloBoton estilo, String ventana) {
        assertTrue(b.isNavegacionAutomatica(), "D-06 navegación automática");
        assertTrue(b.isCerrarVentanaActual(), "D-06 cerrar ventana actual");
        var menu = estilo == EstiloBoton.MENU;
        assertEquals(FONDO, b.getColorFondo(), "D-07 color fondo");
        assertEquals(menu ? HOVER_MENU : ACTIVO, b.getColorHover(), "D-07 color hover");
        assertEquals(menu ? ACTIVO_MENU : ACTIVO, b.getColorActivo(), "D-07 color activo");
        assertEquals(BORDE_SUAVE, b.getColorBorde(), "D-08 color borde");
        assertEquals(BORDE_SUAVE, b.getColorBordeHover(), "D-08 color borde hover");
        assertEquals(TEXTO, b.getColorTexto(), "D-09 color texto");
        assertEquals(TEXTO_DESHABILITADO, b.getColorTextoDeshabilitado(), "D-09 color texto deshabilitado");
        assertEquals(6, b.getRadioBorde(), "D-10 radio");
        assertEquals(12, b.getSeparacionIcono(), "D-10 separación icono-texto");
        assertNull(b.getIconoPersonalizado(), "D-11 icono personalizado");
    }

    @ParameterizedTest(name = "D-12 tamaño mínimo {0}")
    @MethodSource("especificacion")
    void d12_tamanoPreferidoMinimo(String nombre, BotonBymetoo b, String texto,
            EstiloBoton estilo, String ventana) {
        var tam = enEdt(b::getPreferredSize);
        if (estilo == EstiloBoton.MENU) {
            assertTrue(tam.width >= 120 && tam.height >= 50, "MENU >= 120x50, es " + tam);
        } else {
            assertTrue(tam.width >= 140 && tam.height >= 38, "CONTORNO >= 140x38, es " + tam);
        }
    }

    @Test
    @DisplayName("D-13 / D-14 foco, opacidad y sin listeners")
    void d13d14_focoOpacidadListeners() {
        assertTrue(boton.isFocusable(), "D-13 focusable");
        assertFalse(boton.isOpaque(), "D-13 opaco");
        assertEquals(0, boton.getBotonBymetooListeners().length, "D-14 listeners");
    }

    // ------------------------------------------------------------------
    // 4.2 Getters/setters y valores límite
    // ------------------------------------------------------------------

    @Test
    @DisplayName("L-01 setTexto guarda el texto")
    void l01_texto() {
        enEdt(() -> boton.setTexto("Mis gastos"));
        assertEquals("Mis gastos", boton.getTexto());
    }

    @Test
    @DisplayName("L-02 texto vacío se pinta sin error")
    void l02_textoVacio() {
        enEdt(() -> boton.setTexto(""));
        assertEquals("", boton.getTexto());
        assertDoesNotThrow(() -> pintar(boton));
    }

    @Test
    @DisplayName("L-03 texto null pasa a cadena vacía")
    void l03_textoNull() {
        assertDoesNotThrow(() -> enEdt(() -> boton.setTexto(null)));
        assertEquals("", boton.getTexto());
    }

    @Test
    @DisplayName("L-04 texto de 500 caracteres agranda el botón")
    void l04_textoLargo() {
        var largo = "x".repeat(500);
        enEdt(() -> boton.setTexto(largo));
        assertEquals(largo, boton.getTexto());
        assertTrue(enEdt(boton::getPreferredSize).width > 140);
        assertDoesNotThrow(() -> pintar(boton));
    }

    @Test
    @DisplayName("L-05 caracteres especiales y emoji")
    void l05_caracteresEspeciales() {
        var texto = "ñ € 😀 <b>";
        enEdt(() -> boton.setTexto(texto));
        assertEquals(texto, boton.getTexto());
        assertDoesNotThrow(() -> pintar(boton));
    }

    @Test
    @DisplayName("L-06 ventanaDestino se guarda sin espacios")
    void l06_ventanaConEspacios() {
        enEdt(() -> boton.setVentanaDestino("  vista.UserView  "));
        assertEquals("vista.UserView", boton.getVentanaDestino());
    }

    @Test
    @DisplayName("L-07 ventanaDestino null pasa a cadena vacía")
    void l07_ventanaNull() {
        enEdt(() -> boton.setVentanaDestino(null));
        assertEquals("", boton.getVentanaDestino());
    }

    @Test
    @DisplayName("L-08 radio 0")
    void l08_radioCero() {
        enEdt(() -> boton.setRadioBorde(0));
        assertEquals(0, boton.getRadioBorde());
    }

    @Test
    @DisplayName("L-09 radio negativo lanza IllegalArgumentException y no cambia")
    void l09_radioNegativo() {
        var antes = boton.getRadioBorde();
        assertThrows(IllegalArgumentException.class, () -> enEdt(() -> boton.setRadioBorde(-1)));
        assertEquals(antes, boton.getRadioBorde());
    }

    @Test
    @DisplayName("L-10 radio Integer.MAX_VALUE se pinta sin error")
    void l10_radioMaximo() {
        enEdt(() -> boton.setRadioBorde(Integer.MAX_VALUE));
        assertEquals(Integer.MAX_VALUE, boton.getRadioBorde());
        assertDoesNotThrow(() -> pintar(boton));
    }

    @Test
    @DisplayName("L-11 los 7 colores se guardan")
    void l11_colores() {
        var c = new Color(10, 20, 30);
        enEdt(() -> {
            boton.setColorFondo(c);
            boton.setColorHover(c);
            boton.setColorActivo(c);
            boton.setColorBorde(c);
            boton.setColorBordeHover(c);
            boton.setColorTexto(c);
            boton.setColorTextoDeshabilitado(c);
        });
        assertEquals(c, boton.getColorFondo());
        assertEquals(c, boton.getColorHover());
        assertEquals(c, boton.getColorActivo());
        assertEquals(c, boton.getColorBorde());
        assertEquals(c, boton.getColorBordeHover());
        assertEquals(c, boton.getColorTexto());
        assertEquals(c, boton.getColorTextoDeshabilitado());
    }

    @ParameterizedTest(name = "L-12 {0} = null")
    @ValueSource(strings = {"colorFondo", "colorHover", "colorActivo", "colorBorde",
        "colorBordeHover", "colorTexto", "colorTextoDeshabilitado"})
    void l12_colorNull(String propiedad) {
        var antes = leerColor(propiedad);
        var error = assertThrows(NullPointerException.class,
                () -> enEdt(() -> escribirColor(propiedad, null)));
        assertTrue(String.valueOf(error.getMessage()).contains(propiedad),
                "El mensaje debe nombrar la propiedad: " + error.getMessage());
        assertEquals(antes, leerColor(propiedad), "El valor anterior se conserva");
    }

    @ParameterizedTest(name = "L-13 alpha {0}")
    @ValueSource(ints = {0, 255})
    void l13_alpha(int alpha) {
        var c = new Color(10, 20, 30, alpha);
        enEdt(() -> boton.setColorFondo(c));
        assertEquals(alpha, boton.getColorFondo().getAlpha());
        assertDoesNotThrow(() -> pintar(boton));
    }

    @Test
    @DisplayName("L-14 / L-15 icono personalizado y vuelta al de TipoIcono")
    void l14l15_iconoPersonalizado() {
        var icono = new ImageIcon(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB));
        enEdt(() -> boton.setIconoPersonalizado(icono));
        assertSame(icono, boton.getIconoPersonalizado());
        enEdt(() -> boton.setIconoPersonalizado(null));
        assertNull(boton.getIconoPersonalizado());
        assertDoesNotThrow(() -> pintar(boton));
    }

    @Test
    @DisplayName("L-16 cambiar la fuente")
    void l16_fuente() {
        var fuente = new Font(Font.SANS_SERIF, Font.BOLD, 20);
        enEdt(() -> boton.setFont(fuente));
        assertEquals(fuente, enEdt(boton::getFont));
    }

    @Test
    @DisplayName("L-17 fuente null conserva la anterior")
    void l17_fuenteNull() {
        var antes = enEdt(boton::getFont);
        assertDoesNotThrow(() -> enEdt(() -> boton.setFont(null)));
        assertEquals(antes, enEdt(boton::getFont));
    }

    @Test
    @DisplayName("L-19 separacionIcono se guarda y cambia el hueco icono-texto")
    void l19_separacionIcono() {
        enEdt(() -> boton.setSeparacionIcono(20));
        assertEquals(20, boton.getSeparacionIcono());
        enEdt(() -> boton.setSeparacionIcono(0));
        assertEquals(0, boton.getSeparacionIcono());
        assertDoesNotThrow(() -> pintar(boton));
    }

    @Test
    @DisplayName("L-20 separacionIcono negativa lanza IllegalArgumentException y no cambia")
    void l20_separacionNegativa() {
        var antes = boton.getSeparacionIcono();
        assertThrows(IllegalArgumentException.class, () -> enEdt(() -> boton.setSeparacionIcono(-1)));
        assertEquals(antes, boton.getSeparacionIcono());
    }

    @Test
    @DisplayName("L-18 añadir un listener null se ignora")
    void l18_listenerNull() {
        enEdt(() -> boton.addBotonBymetooListener(null));
        assertArrayEquals(new BotonBymetooListener[0], boton.getBotonBymetooListeners());
    }

    // ------------------------------------------------------------------
    // Ayudas
    // ------------------------------------------------------------------

    /** Pinta el botón en una imagen con su tamaño preferido. */
    static BufferedImage pintar(BotonBymetoo b) {
        return enEdt(() -> {
            var tam = b.getPreferredSize();
            var ancho = Math.min(tam.width, 4000);
            b.setSize(ancho, tam.height);
            b.doLayout();
            var img = new BufferedImage(ancho, tam.height, BufferedImage.TYPE_INT_ARGB);
            var g = img.createGraphics();
            try {
                b.paint(g);
            } finally {
                g.dispose();
            }
            return img;
        });
    }

    private Color leerColor(String propiedad) {
        return switch (propiedad) {
            case "colorFondo" -> boton.getColorFondo();
            case "colorHover" -> boton.getColorHover();
            case "colorActivo" -> boton.getColorActivo();
            case "colorBorde" -> boton.getColorBorde();
            case "colorBordeHover" -> boton.getColorBordeHover();
            case "colorTexto" -> boton.getColorTexto();
            case "colorTextoDeshabilitado" -> boton.getColorTextoDeshabilitado();
            default -> throw new IllegalArgumentException(propiedad);
        };
    }

    private void escribirColor(String propiedad, Color c) {
        switch (propiedad) {
            case "colorFondo" -> boton.setColorFondo(c);
            case "colorHover" -> boton.setColorHover(c);
            case "colorActivo" -> boton.setColorActivo(c);
            case "colorBorde" -> boton.setColorBorde(c);
            case "colorBordeHover" -> boton.setColorBordeHover(c);
            case "colorTexto" -> boton.setColorTexto(c);
            case "colorTextoDeshabilitado" -> boton.setColorTextoDeshabilitado(c);
            default -> throw new IllegalArgumentException(propiedad);
        }
    }
}
