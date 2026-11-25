package com.bymetoo.componentes;

import static com.bymetoo.componentes.Utilidades.enEdt;
import static com.bymetoo.componentes.Utilidades.entrarRaton;
import static com.bymetoo.componentes.Utilidades.salirRaton;
import static com.bymetoo.componentes.Utilidades.apretarRaton;
import static com.bymetoo.componentes.Utilidades.soltarRatonFuera;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import org.junit.jupiter.api.DisplayName;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * DI-3.3.1 · Estados visuales del plan de pruebas DI-3.1.3: lógica de estados
 * (casos E-*) y aspecto pintado (casos P-*).
 * <p>
 * El aspecto se comprueba pintando el botón en un {@link BufferedImage} y
 * leyendo píxeles, así que funciona también sin pantalla.
 * </p>
 * <p>
 * {@code @RunWith(JUnitPlatform.class)} permite ejecutarlo desde NetBeans
 * (Alt+F6): los proyectos Ant de NetBeans solo lanzan tests con el runner de
 * JUnit 4, y este puente ejecuta por debajo los tests de JUnit 5.
 * </p>
 */
@RunWith(JUnitPlatform.class)
@DisplayName("BotonBymetoo · estados visuales")
public class BotonBymetooEstadosTest {

    private static final Color FONDO = new Color(0xFA, 0xFA, 0xFA);
    private static final Color ACTIVO = new Color(0x81, 0xDA, 0xAB);
    private static final Color BORDE_SUAVE = new Color(0xE2, 0xE8, 0xE5);
    /** Tolerancia por canal en las comparaciones de píxeles (antialiasing). */
    private static final int TOLERANCIA = 2;

    // ------------------------------------------------------------------
    // Lógica de estados (E-*)
    // ------------------------------------------------------------------

    @ParameterizedTest(name = "E-01/E-02 hover {0}")
    @MethodSource("com.bymetoo.componentes.Utilidades#unoDeCadaEstilo")
    void e01e02_hover(BotonBymetoo b) {
        entrarRaton(b);
        assertEquals(EstadoBoton.HOVER, b.getEstado(), "E-01");
        salirRaton(b);
        assertEquals(EstadoBoton.NORMAL, b.getEstado(), "E-02");
    }

    @ParameterizedTest(name = "E-03/E-04 activo {0}")
    @MethodSource("com.bymetoo.componentes.Utilidades#unoDeCadaEstilo")
    void e03e04_activo(BotonBymetoo b) {
        enEdt(() -> b.setActivo(true));
        assertEquals(EstadoBoton.ACTIVO, b.getEstado(), "E-03");
        enEdt(() -> b.setActivo(false));
        assertEquals(EstadoBoton.NORMAL, b.getEstado(), "E-04");
    }

    @ParameterizedTest(name = "E-05/E-06 deshabilitado {0}")
    @MethodSource("com.bymetoo.componentes.Utilidades#unoDeCadaEstilo")
    void e05e06_deshabilitado(BotonBymetoo b) {
        enEdt(() -> b.setEnabled(false));
        assertEquals(EstadoBoton.DESHABILITADO, b.getEstado(), "E-05");
        enEdt(() -> b.setEnabled(true));
        assertEquals(EstadoBoton.NORMAL, b.getEstado(), "E-06");
    }

    @Test
    @DisplayName("E-07 / E-08 deshabilitado gana a activo y la selección se conserva")
    void e07e08_deshabilitadoGanaAActivo() {
        var b = enEdt(BotonSalir::new);
        enEdt(() -> b.setActivo(true));
        enEdt(() -> b.setEnabled(false));
        assertEquals(EstadoBoton.DESHABILITADO, b.getEstado(), "E-07");
        enEdt(() -> b.setEnabled(true));
        assertEquals(EstadoBoton.ACTIVO, b.getEstado(), "E-08");
    }

    @Test
    @DisplayName("E-09 activo gana a hover")
    void e09_activoGanaAHover() {
        var b = enEdt(BotonInicio::new);
        entrarRaton(b);
        enEdt(() -> b.setActivo(true));
        assertEquals(EstadoBoton.ACTIVO, b.getEstado());
    }

    @Test
    @DisplayName("E-10 al deshabilitar se borra el hover")
    void e10_deshabilitarBorraHover() {
        var b = enEdt(BotonInicio::new);
        entrarRaton(b);
        enEdt(() -> b.setEnabled(false));
        enEdt(() -> b.setEnabled(true));
        assertEquals(EstadoBoton.NORMAL, b.getEstado());
    }

    @Test
    @DisplayName("E-11 un botón deshabilitado no pasa a hover")
    void e11_deshabilitadoSinHover() {
        var b = enEdt(BotonInicio::new);
        enEdt(() -> b.setEnabled(false));
        entrarRaton(b);
        assertEquals(EstadoBoton.DESHABILITADO, b.getEstado());
    }

    // ------------------------------------------------------------------
    // Aspecto pintado (P-*)
    // ------------------------------------------------------------------

    @Test
    @DisplayName("P-01 CONTORNO normal: fondo #FAFAFA y borde #E2E8E5")
    void p01_contornoNormal() {
        var img = BotonBymetooValoresTest.pintar(enEdt(BotonSalir::new));
        assertColor(FONDO, fondo(img), "fondo");
        assertColor(BORDE_SUAVE, borde(img), "borde");
    }

    @Test
    @DisplayName("P-02 CONTORNO_SUAVE normal: fondo #FAFAFA y borde #E2E8E5")
    void p02_contornoSuaveNormal() {
        var img = BotonBymetooValoresTest.pintar(enEdt(BotonCompartir::new));
        assertColor(FONDO, fondo(img), "fondo");
        assertColor(BORDE_SUAVE, borde(img), "borde");
    }

    @Test
    @DisplayName("P-03 CONTORNO activo: fondo verde #81DAAB")
    void p03_contornoActivo() {
        var b = enEdt(BotonSalir::new);
        enEdt(() -> b.setActivo(true));
        assertColor(ACTIVO, fondo(BotonBymetooValoresTest.pintar(b)), "fondo activo");
    }

    @Test
    @DisplayName("P-04 la esquina (0,0) queda fuera del redondeo")
    void p04_esquinaTransparente() {
        var img = BotonBymetooValoresTest.pintar(enEdt(BotonSalir::new));
        assertEquals(0, new Color(img.getRGB(0, 0), true).getAlpha());
    }

    @Test
    @DisplayName("P-05 MENU normal: sin fondo ni borde")
    void p05_menuNormal() {
        var img = BotonBymetooValoresTest.pintar(enEdt(BotonInicio::new));
        assertEquals(0, fondo(img).getAlpha(), "fondo transparente");
        assertEquals(0, borde(img).getAlpha(), "sin borde");
    }

    @Test
    @DisplayName("P-06 MENU: hover pinta colorHover y activo pinta colorActivo")
    void p06_menuHoverActivo() {
        var b = enEdt(BotonInicio::new);
        entrarRaton(b);
        assertColor(sobreBlanco(b.getColorHover()), fondo(pintarSobreBlanco(b)), "hover");
        salirRaton(b);
        enEdt(() -> b.setActivo(true));
        assertColor(sobreBlanco(b.getColorActivo()), fondo(pintarSobreBlanco(b)), "activo");
    }

    @ParameterizedTest(name = "P-10 mantener pulsado pinta colorActivo {0}")
    @MethodSource("com.bymetoo.componentes.Utilidades#unoDeCadaEstilo")
    void p10_pulsadoPintaActivo(BotonBymetoo b) {
        enEdt(() -> b.setNavegacionAutomatica(false));
        entrarRaton(b);
        apretarRaton(b);
        assertColor(sobreBlanco(b.getColorActivo()), fondo(pintarSobreBlanco(b)), "pulsado");
        // Se suelta fuera del botón para que no cuente como pulsación (no se activa)
        soltarRatonFuera(b);
        assertColor(sobreBlanco(b.getColorHover()), fondo(pintarSobreBlanco(b)), "al soltar vuelve a hover");
    }

    @Test
    @DisplayName("P-11 si el ratón sale mientras está pulsado, deja de pintarse pulsado")
    void p11_salirDuranteLaPulsacion() {
        var b = enEdt(BotonSalir::new);
        enEdt(() -> b.setNavegacionAutomatica(false));
        entrarRaton(b);
        apretarRaton(b);
        salirRaton(b);
        assertColor(FONDO, fondo(BotonBymetooValoresTest.pintar(b)), "fondo normal");
    }

    @Test
    @DisplayName("P-12 deshabilitado: apretar el ratón no pinta pulsado")
    void p12_deshabilitadoNoSePulsa() {
        var b = enEdt(BotonSalir::new);
        enEdt(() -> b.setEnabled(false));
        apretarRaton(b);
        assertColor(FONDO, fondo(BotonBymetooValoresTest.pintar(b)), "fondo");
    }

    @Test
    @DisplayName("P-13 CONTORNO con ratón encima: el borde usa colorBordeHover")
    void p13_bordeHover() {
        var b = enEdt(BotonSalir::new);
        var rojo = new Color(200, 0, 0);
        enEdt(() -> b.setColorBordeHover(rojo));
        assertColor(BORDE_SUAVE, borde(BotonBymetooValoresTest.pintar(b)), "borde normal");
        entrarRaton(b);
        assertColor(rojo, borde(BotonBymetooValoresTest.pintar(b)), "borde hover");
    }

    @ParameterizedTest(name = "P-07/P-08 texto y cursor {0}")
    @MethodSource("com.bymetoo.componentes.Utilidades#unoDeCadaEstilo")
    void p07p08_textoYCursor(BotonBymetoo b) {
        assertEquals(Cursor.HAND_CURSOR, b.getCursor().getType(), "P-08 cursor habilitado");
        assertEquals(b.getColorTexto(), etiqueta(b).getForeground(), "texto habilitado");
        enEdt(() -> b.setEnabled(false));
        assertEquals(Cursor.DEFAULT_CURSOR, b.getCursor().getType(), "P-07 cursor deshabilitado");
        assertEquals(b.getColorTextoDeshabilitado(), etiqueta(b).getForeground(),
                "P-07 texto deshabilitado");
    }

    @Test
    @DisplayName("P-09 deshabilitado y activo no se pinta verde")
    void p09_deshabilitadoActivoNoVerde() {
        var b = enEdt(BotonSalir::new);
        enEdt(() -> {
            b.setActivo(true);
            b.setEnabled(false);
        });
        assertColor(FONDO, fondo(BotonBymetooValoresTest.pintar(b)), "fondo");
    }

    // ------------------------------------------------------------------
    // Ayudas
    // ------------------------------------------------------------------

    /** Píxel de fondo: a la derecha y a media altura, lejos del texto y del icono. */
    static Color fondo(BufferedImage img) {
        return new Color(img.getRGB(img.getWidth() - 6, img.getHeight() / 2), true);
    }

    /** Píxel del borde izquierdo a media altura. */
    static Color borde(BufferedImage img) {
        return new Color(img.getRGB(0, img.getHeight() / 2), true);
    }

    /** Pinta el botón sobre un fondo blanco opaco, para comparar colores con transparencia. */
    static BufferedImage pintarSobreBlanco(BotonBymetoo b) {
        var img = BotonBymetooValoresTest.pintar(b);
        var res = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        var g = res.createGraphics();
        try {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, res.getWidth(), res.getHeight());
            g.drawImage(img, 0, 0, null);
        } finally {
            g.dispose();
        }
        return res;
    }

    /** Color que se ve al pintar {@code c} (con su alpha) encima de blanco. */
    static Color sobreBlanco(Color c) {
        var a = c.getAlpha() / 255.0;
        return new Color(
                (int) Math.round(c.getRed() * a + 255 * (1 - a)),
                (int) Math.round(c.getGreen() * a + 255 * (1 - a)),
                (int) Math.round(c.getBlue() * a + 255 * (1 - a)));
    }

    static JLabel etiqueta(BotonBymetoo b) {
        return enEdt(() -> (JLabel) b.getComponent(0));
    }

    static void assertColor(Color esperado, Color real, String que) {
        var ok = Math.abs(esperado.getRed() - real.getRed()) <= TOLERANCIA
                && Math.abs(esperado.getGreen() - real.getGreen()) <= TOLERANCIA
                && Math.abs(esperado.getBlue() - real.getBlue()) <= TOLERANCIA
                && Math.abs(esperado.getAlpha() - real.getAlpha()) <= TOLERANCIA;
        assertTrue(ok, que + ": esperado " + esperado + " alpha=" + esperado.getAlpha()
                + ", obtenido " + real + " alpha=" + real.getAlpha());
    }
}
