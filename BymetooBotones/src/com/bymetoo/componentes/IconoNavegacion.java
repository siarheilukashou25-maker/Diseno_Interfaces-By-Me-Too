package com.bymetoo.componentes;

// Java 25: importa todo java.desktop (java.awt, java.awt.geom, javax.swing...)
import module java.desktop;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase que dibuja los iconos de los botones con Java2D, así no hace falta
 * tener imágenes. El icono se pinta del mismo color que el texto.
 *
 * <pre>{@code
 * Icon carrito = new IconoNavegacion(TipoIcono.NUEVA_COMPRA, Color.BLACK);
 * JLabel etiqueta = new JLabel("Nueva compra", carrito, JLabel.LEFT);
 * }</pre>
 *
 * @author Equipo Bymetoo
 */
public class IconoNavegacion implements Icon, Serializable {

    private static final long serialVersionUID = 1L;

    /** Tamaño del icono en píxeles. */
    public static final int TAMANO = 16;

    /** Qué icono se dibuja. */
    private final TipoIcono tipo;
    /** Color del icono. */
    private final Color color;

    /**
     * Crea el icono.
     *
     * @param tipo  el icono que se quiere dibujar
     * @param color el color del icono
     */
    public IconoNavegacion(TipoIcono tipo, Color color) {
        this.tipo = Objects.requireNonNull(tipo, "tipo");
        this.color = Objects.requireNonNull(color, "color");
    }

    /**
     * Devuelve qué icono es.
     *
     * @return el tipo de icono
     */
    public TipoIcono getTipo() {
        return tipo;
    }

    /**
     * Devuelve el color del icono.
     *
     * @return el color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Devuelve el ancho del icono.
     *
     * @return 16, o 0 si no hay icono
     */
    @Override
    public int getIconWidth() {
        return tipo == TipoIcono.NINGUNO ? 0 : TAMANO;
    }

    /**
     * Devuelve el alto del icono.
     *
     * @return 16, o 0 si no hay icono
     */
    @Override
    public int getIconHeight() {
        return tipo == TipoIcono.NINGUNO ? 0 : TAMANO;
    }

    /**
     * Dibuja el icono.
     *
     * @param c el componente donde se pinta
     * @param g el objeto Graphics para dibujar
     * @param x posición x
     * @param y posición y
     */
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (tipo == TipoIcono.NINGUNO) {
            return;
        }
        var g2 = (Graphics2D) g.create();
        try {
            g2.translate(x, y);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            dibujar(g2);
        } finally {
            g2.dispose();
        }
    }

    private void dibujar(Graphics2D g2) {
        // switch con flechas: no hace falta poner break en cada caso
        switch (tipo) {
            case USUARIOS -> dibujarUsuarios(g2);
            case PERFIL -> dibujarPerfil(g2);
            case COMPARTIR -> dibujarCompartir(g2);
            case GASTOS -> dibujarGastos(g2);
            case REPARTOS -> dibujarRepartos(g2);
            case NUEVA_COMPRA -> dibujarCarrito(g2);
            case RESUMEN -> dibujarResumen(g2);
            case SALIR -> dibujarSalir(g2);
            case NINGUNO -> { } // si es NINGUNO no se dibuja nada
        }
    }

    private void dibujarUsuarios(Graphics2D g2) {
        g2.draw(new Ellipse2D.Double(3, 2, 5, 5));
        g2.draw(new Arc2D.Double(0.8, 9.2, 9.4, 9.6, 0, 180, Arc2D.OPEN));
        g2.draw(new Arc2D.Double(9, 2, 5, 5, -90, 180, Arc2D.OPEN));
        g2.draw(new Arc2D.Double(10.5, 9.2, 4.7, 9.6, 0, 90, Arc2D.OPEN));
    }

    private void dibujarPerfil(Graphics2D g2) {
        g2.draw(new Ellipse2D.Double(5.5, 2.5, 5, 5));
        g2.draw(new Arc2D.Double(3.5, 10, 9, 8.5, 0, 180, Arc2D.OPEN));
    }

    private void dibujarCompartir(Graphics2D g2) {
        g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(new Line2D.Double(4, 8, 12, 3.5));
        g2.draw(new Line2D.Double(4, 8, 12, 12.5));
        var r = 2.4;
        g2.fill(new Ellipse2D.Double(12 - r, 3.5 - r, 2 * r, 2 * r));
        g2.fill(new Ellipse2D.Double(4 - r, 8 - r, 2 * r, 2 * r));
        g2.fill(new Ellipse2D.Double(12 - r, 12.5 - r, 2 * r, 2 * r));
    }

    private void dibujarGastos(Graphics2D g2) {
        g2.draw(new RoundRectangle2D.Double(1, 5.5, 12, 7.5, 1.5, 1.5));
        var trasero = new Path2D.Double();
        trasero.moveTo(3, 3.5);
        trasero.lineTo(15, 3.5);
        trasero.lineTo(15, 11);
        g2.draw(trasero);
        g2.draw(new Ellipse2D.Double(5, 7.3, 4, 4));
    }

    private void dibujarRepartos(Graphics2D g2) {
        var corazon = new Path2D.Double();
        corazon.moveTo(8, 14.2);
        corazon.curveTo(-1.5, 8, 2, 0.5, 8, 4.6);
        corazon.curveTo(14, 0.5, 17.5, 8, 8, 14.2);
        g2.draw(corazon);
        var manos = new Path2D.Double();
        manos.moveTo(3.2, 7.6);
        manos.lineTo(6.6, 5.6);
        manos.lineTo(9.6, 8.4);
        manos.lineTo(11, 7.4);
        manos.moveTo(6.4, 9.2);
        manos.lineTo(8.2, 10.8);
        manos.moveTo(8, 8.2);
        manos.lineTo(9.8, 9.8);
        g2.draw(manos);
    }

    private void dibujarCarrito(Graphics2D g2) {
        var carro = new Path2D.Double();
        carro.moveTo(1, 2.5);
        carro.lineTo(3.3, 2.5);
        carro.lineTo(5, 11);
        carro.lineTo(13, 11);
        carro.moveTo(3.8, 5);
        carro.lineTo(14.8, 5);
        carro.lineTo(13.4, 9);
        carro.lineTo(4.6, 9);
        g2.draw(carro);
        g2.fill(new Ellipse2D.Double(5, 12.6, 2.2, 2.2));
        g2.fill(new Ellipse2D.Double(11, 12.6, 2.2, 2.2));
    }

    private void dibujarResumen(Graphics2D g2) {
        var ejes = new Path2D.Double();
        ejes.moveTo(2, 1.5);
        ejes.lineTo(2, 14.5);
        ejes.lineTo(15, 14.5);
        g2.draw(ejes);
        g2.draw(new Line2D.Double(5.5, 8, 5.5, 12.5));
        g2.draw(new Line2D.Double(8.5, 5, 8.5, 12.5));
        g2.draw(new Line2D.Double(11.5, 9, 11.5, 12.5));
    }

    private void dibujarSalir(Graphics2D g2) {
        var puerta = new Path2D.Double();
        puerta.moveTo(9, 2);
        puerta.lineTo(14, 2);
        puerta.lineTo(14, 14);
        puerta.lineTo(9, 14);
        g2.draw(puerta);
        g2.draw(new Line2D.Double(2, 8, 10, 8));
        var flecha = new Path2D.Double();
        flecha.moveTo(7, 5);
        flecha.lineTo(10, 8);
        flecha.lineTo(7, 11);
        g2.draw(flecha);
    }
}
