package com.bymetoo.componentes;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.beans.Beans;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Objects;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 * Clase padre de todos los botones de la aplicación Bymetoo.
 * <p>
 * Aquí está todo lo que tienen en común los botones: las propiedades, los
 * cuatro estados del boceto (normal, hover, activo y deshabilitado), cómo se
 * pintan, los eventos y el cambio de ventana. Cada botón (BotonInicio,
 * BotonGastos, BotonSalir...) hereda de esta clase y solo cambia su texto, su
 * icono, su estilo y la ventana a la que va.
 * </p>
 * <p>
 * Cuando se pulsa el botón se lanza el evento botonPulsado y después se abre la
 * ventana que pone en ventanaDestino y se cierra la actual.
 * </p>
 *
 * <pre>{@code
 * BotonRepartos boton = new BotonRepartos();
 * boton.addBotonBymetooListener(new BotonBymetooAdapter() {
 *     @Override
 *     public void botonPulsado(BotonBymetooEvent e) {
 *         System.out.println("Has pulsado Repartos");
 *     }
 * });
 * }</pre>
 *
 * @author Equipo Bymetoo
 */
public abstract class BotonBymetoo extends JPanel implements Serializable {

    private static final long serialVersionUID = 1L;

    // Nombres de las propiedades (para los PropertyChangeListener)

    /** Nombre de la propiedad texto. */
    public static final String PROP_TEXTO = "texto";
    /** Nombre de la propiedad ventanaDestino. */
    public static final String PROP_VENTANA_DESTINO = "ventanaDestino";
    /** Nombre de la propiedad navegacionAutomatica. */
    public static final String PROP_NAVEGACION_AUTOMATICA = "navegacionAutomatica";
    /** Nombre de la propiedad cerrarVentanaActual. */
    public static final String PROP_CERRAR_VENTANA_ACTUAL = "cerrarVentanaActual";
    /** Nombre de la propiedad activo. */
    public static final String PROP_ACTIVO = "activo";
    /** Nombre de la propiedad iconoPersonalizado. */
    public static final String PROP_ICONO_PERSONALIZADO = "iconoPersonalizado";
    /** Nombre de la propiedad colorFondo. */
    public static final String PROP_COLOR_FONDO = "colorFondo";
    /** Nombre de la propiedad colorHover. */
    public static final String PROP_COLOR_HOVER = "colorHover";
    /** Nombre de la propiedad colorActivo. */
    public static final String PROP_COLOR_ACTIVO = "colorActivo";
    /** Nombre de la propiedad colorBorde. */
    public static final String PROP_COLOR_BORDE = "colorBorde";
    /** Nombre de la propiedad colorTexto. */
    public static final String PROP_COLOR_TEXTO = "colorTexto";
    /** Nombre de la propiedad colorTextoDeshabilitado. */
    public static final String PROP_COLOR_TEXTO_DESHABILITADO = "colorTextoDeshabilitado";
    /** Nombre de la propiedad radioBorde. */
    public static final String PROP_RADIO_BORDE = "radioBorde";
    /** Nombre de la propiedad estado (solo se puede leer). */
    public static final String PROP_ESTADO = "estado";

    // Colores y medidas por defecto, sacados del boceto de Elias

    /** Color de fondo normal (blanco). */
    public static final Color COLOR_FONDO_POR_DEFECTO = Color.WHITE;
    /** Color gris clarito cuando pasas el ratón por encima. */
    public static final Color COLOR_HOVER_POR_DEFECTO = new Color(0xF6, 0xF7, 0xF6);
    /** Color verde cuando el botón está activo. */
    public static final Color COLOR_ACTIVO_POR_DEFECTO = new Color(0x80, 0xD9, 0xA8);
    /** Color del borde oscuro. */
    public static final Color COLOR_BORDE_OSCURO = new Color(0x1E, 0x1E, 0x1E);
    /** Color del borde gris (Compartir y Gastos). */
    public static final Color COLOR_BORDE_SUAVE = new Color(0xD6, 0xD6, 0xD6);
    /** Color del texto y del icono. */
    public static final Color COLOR_TEXTO_POR_DEFECTO = new Color(0x11, 0x11, 0x11);
    /** Lo redondeadas que son las esquinas. */
    public static final int RADIO_BORDE_POR_DEFECTO = 4;

    private static final int ALTO_CONTORNO = 38;
    private static final int ALTO_MENU = 50;
    private static final int ANCHO_CONTORNO = 140;
    private static final int ANCHO_MENU = 120;

    // Atributos

    /** Sirve para avisar cuando cambia una propiedad. */
    private final PropertyChangeSupport propertySupport;
    /** Etiqueta donde se ponen el icono y el texto. */
    private final JLabel etiqueta;
    /** Icono del botón. */
    private final TipoIcono tipoIcono;
    /** Estilo del botón (menú o con borde). */
    private final EstiloBoton estilo;

    /** Texto que se ve en el botón. */
    private String texto;
    /** Clase de la ventana a la que va el botón, por ejemplo "vista.MainView". */
    private String ventanaDestino;
    /** Si es true, al pulsar el botón cambia de ventana solo. */
    private boolean navegacionAutomatica;
    /** Si es true, al cambiar de ventana se cierra la de antes. */
    private boolean cerrarVentanaActual;
    /** Si el botón está seleccionado o no. */
    private boolean activo;
    /** Icono propio por si no se quiere usar el que viene. */
    private Icon iconoPersonalizado;
    /** Color de fondo normal. */
    private Color colorFondo;
    /** Color de fondo con el ratón encima. */
    private Color colorHover;
    /** Color cuando está activo. */
    private Color colorActivo;
    /** Color del borde. */
    private Color colorBorde;
    /** Color del texto. */
    private Color colorTexto;
    /** Color del texto cuando está deshabilitado. */
    private Color colorTextoDeshabilitado;
    /** Radio de las esquinas. */
    private int radioBorde;
    /** Si el ratón está encima del botón. */
    private boolean hover;
    /** Estado en el que está el botón ahora mismo. */
    private EstadoBoton estado;

    /**
     * Constructor que usan los botones hijos para decir cómo son.
     *
     * @param texto          el texto del botón
     * @param tipoIcono      el icono que lleva
     * @param estilo         el estilo (MENU, CONTORNO_SUAVE o CONTORNO)
     * @param ventanaDestino la ventana a la que va, por ejemplo "vista.MainView"
     */
    protected BotonBymetoo(String texto, TipoIcono tipoIcono, EstiloBoton estilo, String ventanaDestino) {
        propertySupport = new PropertyChangeSupport(this);
        etiqueta = new JLabel();

        this.tipoIcono = Objects.requireNonNull(tipoIcono, "tipoIcono");
        this.estilo = Objects.requireNonNull(estilo, "estilo");
        this.texto = texto == null ? "" : texto;
        this.ventanaDestino = ventanaDestino == null ? "" : ventanaDestino;
        this.navegacionAutomatica = true;
        this.cerrarVentanaActual = true;
        this.activo = false;
        this.iconoPersonalizado = null;
        this.colorFondo = COLOR_FONDO_POR_DEFECTO;
        this.colorHover = COLOR_HOVER_POR_DEFECTO;
        this.colorActivo = COLOR_ACTIVO_POR_DEFECTO;
        this.colorBorde = estilo == EstiloBoton.CONTORNO_SUAVE ? COLOR_BORDE_SUAVE : COLOR_BORDE_OSCURO;
        this.colorTexto = COLOR_TEXTO_POR_DEFECTO;
        this.colorTextoDeshabilitado = COLOR_TEXTO_POR_DEFECTO;
        this.radioBorde = RADIO_BORDE_POR_DEFECTO;
        this.hover = false;
        this.estado = EstadoBoton.NORMAL;

        inicializarInterfaz();
        configurarEscuchadores();
        actualizarAspecto();
    }

    private void inicializarInterfaz() {
        setOpaque(false);
        setLayout(new BorderLayout());
        setFocusable(true);
        etiqueta.setHorizontalAlignment(SwingConstants.LEFT);
        etiqueta.setIconTextGap(12);
        etiqueta.setBorder(new EmptyBorder(0, 14, 0, 14));
        etiqueta.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        add(etiqueta, BorderLayout.CENTER);
    }

    private void configurarEscuchadores() {
        addMouseListener(new ManejadorRaton());
        // Para que también funcione con el teclado (Espacio o Intro)
        InputMap teclas = getInputMap(JComponent.WHEN_FOCUSED);
        teclas.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "pulsar");
        teclas.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "pulsar");
        getActionMap().put("pulsar", new AccionPulsar());
    }

    // Estados del botón

    /**
     * Mira en qué estado tiene que estar el botón y, si ha cambiado, lo repinta
     * y avisa a los listeners.
     */
    private void actualizarEstado() {
        EstadoBoton nuevo;
        if (!isEnabled()) {
            nuevo = EstadoBoton.DESHABILITADO;
        } else if (activo) {
            nuevo = EstadoBoton.ACTIVO;
        } else if (hover) {
            nuevo = EstadoBoton.HOVER;
        } else {
            nuevo = EstadoBoton.NORMAL;
        }
        EstadoBoton anterior = this.estado;
        if (anterior != nuevo) {
            this.estado = nuevo;
            actualizarAspecto();
            propertySupport.firePropertyChange(PROP_ESTADO, anterior, nuevo);
            fireEstadoCambiado(anterior, nuevo);
        }
    }

    private void actualizarAspecto() {
        Color color = estado == EstadoBoton.DESHABILITADO ? colorTextoDeshabilitado : colorTexto;
        etiqueta.setText(texto);
        etiqueta.setForeground(color);
        if (iconoPersonalizado != null) {
            etiqueta.setIcon(iconoPersonalizado);
        } else if (tipoIcono != TipoIcono.NINGUNO) {
            etiqueta.setIcon(new IconoNavegacion(tipoIcono, color));
        } else {
            etiqueta.setIcon(null);
        }
        setCursor(Cursor.getPredefinedCursor(isEnabled() ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
        revalidate();
        repaint();
    }

    /**
     * Se ejecuta cuando el botón se mete en una ventana.
     * Si esa ventana es la suya (por ejemplo BotonRepartos dentro de
     * RepartosView) se pone activo. Si su ventana no existe en el proyecto, se
     * deshabilita para que no dé error al pulsarlo.
     */
    @Override
    public void addNotify() {
        super.addNotify();
        if (Beans.isDesignTime()) {
            return; // en el diseñador de NetBeans no hacemos nada
        }
        if (estaEnSuVentanaDestino()) {
            setActivo(true);
        } else if (navegacionAutomatica && !ventanaDestino.isEmpty() && !existeVentanaDestino()) {
            setEnabled(false);
            if (getToolTipText() == null) {
                setToolTipText("Pantalla no disponible: " + ventanaDestino);
            }
        }
    }

    /**
     * Comprueba si la ventana de ventanaDestino existe en el proyecto y es un
     * JFrame o JDialog.
     *
     * @return true si existe, false si no
     */
    public boolean existeVentanaDestino() {
        if (ventanaDestino.isEmpty()) {
            return false;
        }
        try {
            Class<?> clase = Class.forName(ventanaDestino, false, cargadorDeClases());
            return Window.class.isAssignableFrom(clase);
        } catch (ClassNotFoundException | LinkageError e) {
            return false;
        }
    }

    private ClassLoader cargadorDeClases() {
        ClassLoader cargador = Thread.currentThread().getContextClassLoader();
        return cargador != null ? cargador : getClass().getClassLoader();
    }

    /**
     * Dice si el botón está dentro de la ventana a la que lleva.
     *
     * @return true si ya estamos en esa ventana
     */
    public boolean estaEnSuVentanaDestino() {
        Window actual = SwingUtilities.getWindowAncestor(this);
        return actual != null && actual.getClass().getName().equals(ventanaDestino);
    }

    // Pulsar y cambiar de ventana

    /**
     * Hace lo mismo que cuando el usuario pulsa el botón.
     * <p>
     * Si está deshabilitado no hace nada. Si no, lo pone activo, lanza el
     * evento botonPulsado y cambia de ventana (si nadie lo ha cancelado).
     * </p>
     */
    public void pulsar() {
        if (!isEnabled()) {
            return;
        }
        boolean activoAnterior = activo;
        setActivo(true);
        BotonBymetooEvent evento = fireBotonPulsado();
        if (navegacionAutomatica && !evento.isNavegacionCancelada()) {
            try {
                navegar();
            } catch (IllegalStateException e) {
                if (GraphicsEnvironment.isHeadless()) {
                    throw e;
                }
                JOptionPane.showMessageDialog(this, e.getMessage(),
                        "Pantalla no disponible", JOptionPane.WARNING_MESSAGE);
            }
        }
        // Si no hemos cambiado de ventana, el botón vuelve a como estaba
        if (isDisplayable() && !estaEnSuVentanaDestino()) {
            setActivo(activoAnterior);
        }
    }

    /**
     * Abre la ventana de ventanaDestino y cierra la actual.
     * Si ya estamos en esa ventana no hace nada.
     *
     * @return la ventana que se ha abierto, o null si no hay ventana destino
     * @throws IllegalStateException si la ventana no existe o no se puede crear
     */
    public Window navegar() {
        if (ventanaDestino.isEmpty()) {
            return null;
        }
        Window actual = SwingUtilities.getWindowAncestor(this);
        if (actual != null && actual.getClass().getName().equals(ventanaDestino)) {
            return actual;
        }
        Window nueva = crearVentana(ventanaDestino);
        nueva.setLocationRelativeTo(actual);
        nueva.setVisible(true);
        if (cerrarVentanaActual && actual != null && actual != nueva) {
            actual.dispose();
        }
        return nueva;
    }

    /**
     * Crea la ventana a partir del nombre de su clase. La ventana tiene que
     * tener un constructor sin parámetros.
     *
     * @param nombreClase el nombre de la clase, por ejemplo "vista.UserView"
     * @return la ventana creada (todavía sin mostrar)
     * @throws IllegalStateException si no se encuentra o no se puede crear
     */
    protected Window crearVentana(String nombreClase) {
        try {
            Class<?> clase = Class.forName(nombreClase, true, cargadorDeClases());
            if (!Window.class.isAssignableFrom(clase)) {
                throw new IllegalStateException(nombreClase + " no es un JFrame/JDialog/Window");
            }
            return (Window) clase.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("No se encuentra la ventana '" + nombreClase
                    + "'. Revisa la propiedad ventanaDestino de " + getClass().getSimpleName(), e);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("No se pudo crear '" + nombreClase
                    + "'. Necesita un constructor público sin parámetros", e);
        }
    }

    // Dibujo del botón

    /**
     * Pinta el fondo y el borde dependiendo del estilo y del estado.
     * <p>
     * NOTA: este dibujo es solo un ejemplo para que se vean los estados del
     * boceto. El feedback visual lo hace Elias, que puede cambiar este método
     * o escuchar el evento estadoCambiado y usar los colores (colorHover,
     * colorActivo...) como quiera.
     * </p>
     *
     * @param g el objeto Graphics para dibujar
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            if (estilo == EstiloBoton.MENU) {
                // Los del menú no tienen borde, solo fondo gris en hover y activo
                if (estado == EstadoBoton.HOVER || estado == EstadoBoton.ACTIVO) {
                    g2.setColor(colorHover);
                    g2.fillRect(0, 0, w, h);
                }
            } else {
                RoundRectangle2D forma = new RoundRectangle2D.Double(
                        0.5, 0.5, w - 1.0, h - 1.0, radioBorde * 2.0, radioBorde * 2.0);
                g2.setColor(estado == EstadoBoton.ACTIVO ? colorActivo : colorFondo);
                g2.fill(forma);
                g2.setColor(colorBorde);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(forma);
            }
        } finally {
            g2.dispose();
        }
    }

    /**
     * Devuelve el tamaño del botón: 140x38 los que tienen borde y 120x50 los
     * del menú (o más grande si el texto no cabe).
     *
     * @return el tamaño preferido
     */
    @Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {
            return super.getPreferredSize();
        }
        Dimension contenido = etiqueta.getPreferredSize();
        boolean menu = estilo == EstiloBoton.MENU;
        return new Dimension(Math.max(menu ? ANCHO_MENU : ANCHO_CONTORNO, contenido.width),
                Math.max(menu ? ALTO_MENU : ALTO_CONTORNO, contenido.height));
    }

    // Getters y setters

    /**
     * Devuelve el icono del botón.
     *
     * @return el tipo de icono
     */
    public TipoIcono getTipoIcono() {
        return tipoIcono;
    }

    /**
     * Devuelve el estilo del botón.
     *
     * @return el estilo
     */
    public EstiloBoton getEstilo() {
        return estilo;
    }

    /**
     * Devuelve el texto del botón.
     *
     * @return el texto
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Cambia el texto del botón.
     *
     * @param texto el nuevo texto
     */
    public void setTexto(String texto) {
        String anterior = this.texto;
        this.texto = texto == null ? "" : texto;
        actualizarAspecto();
        propertySupport.firePropertyChange(PROP_TEXTO, anterior, this.texto);
    }

    /**
     * Devuelve la ventana a la que va el botón.
     *
     * @return el nombre de la clase, por ejemplo "vista.MainView"
     */
    public String getVentanaDestino() {
        return ventanaDestino;
    }

    /**
     * Cambia la ventana a la que va el botón. Hay que poner el paquete y la clase.
     *
     * @param ventanaDestino por ejemplo "vista.ResumenView"
     */
    public void setVentanaDestino(String ventanaDestino) {
        String anterior = this.ventanaDestino;
        this.ventanaDestino = ventanaDestino == null ? "" : ventanaDestino.trim();
        propertySupport.firePropertyChange(PROP_VENTANA_DESTINO, anterior, this.ventanaDestino);
    }

    /**
     * Dice si el botón cambia de ventana solo al pulsarlo.
     *
     * @return true si cambia solo
     */
    public boolean isNavegacionAutomatica() {
        return navegacionAutomatica;
    }

    /**
     * Activa o quita el cambio de ventana automático. Si se pone a false, el
     * botón solo lanza el evento y hay que abrir la ventana a mano.
     *
     * @param navegacionAutomatica true para que cambie solo
     */
    public void setNavegacionAutomatica(boolean navegacionAutomatica) {
        boolean anterior = this.navegacionAutomatica;
        this.navegacionAutomatica = navegacionAutomatica;
        propertySupport.firePropertyChange(PROP_NAVEGACION_AUTOMATICA, anterior, navegacionAutomatica);
    }

    /**
     * Dice si se cierra la ventana actual al cambiar de ventana.
     *
     * @return true si se cierra
     */
    public boolean isCerrarVentanaActual() {
        return cerrarVentanaActual;
    }

    /**
     * Cambia si se cierra la ventana actual al cambiar de ventana.
     *
     * @param cerrarVentanaActual true para cerrarla
     */
    public void setCerrarVentanaActual(boolean cerrarVentanaActual) {
        boolean anterior = this.cerrarVentanaActual;
        this.cerrarVentanaActual = cerrarVentanaActual;
        propertySupport.firePropertyChange(PROP_CERRAR_VENTANA_ACTUAL, anterior, cerrarVentanaActual);
    }

    /**
     * Dice si el botón está activo.
     *
     * @return true si está activo
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * Pone el botón activo o no.
     *
     * @param activo true para ponerlo activo (verde o gris)
     */
    public void setActivo(boolean activo) {
        boolean anterior = this.activo;
        this.activo = activo;
        propertySupport.firePropertyChange(PROP_ACTIVO, anterior, activo);
        actualizarEstado();
    }

    /**
     * Devuelve el icono propio si se ha puesto uno.
     *
     * @return el icono o null
     */
    public Icon getIconoPersonalizado() {
        return iconoPersonalizado;
    }

    /**
     * Pone un icono propio en vez del que trae el botón.
     *
     * @param iconoPersonalizado el icono, o null para usar el de siempre
     */
    public void setIconoPersonalizado(Icon iconoPersonalizado) {
        Icon anterior = this.iconoPersonalizado;
        this.iconoPersonalizado = iconoPersonalizado;
        actualizarAspecto();
        propertySupport.firePropertyChange(PROP_ICONO_PERSONALIZADO, anterior, iconoPersonalizado);
    }

    /**
     * Devuelve el color de fondo.
     *
     * @return el color de fondo
     */
    public Color getColorFondo() {
        return colorFondo;
    }

    /**
     * Cambia el color de fondo.
     *
     * @param colorFondo el nuevo color (no puede ser null)
     */
    public void setColorFondo(Color colorFondo) {
        Color anterior = this.colorFondo;
        this.colorFondo = Objects.requireNonNull(colorFondo, "colorFondo");
        repaint();
        propertySupport.firePropertyChange(PROP_COLOR_FONDO, anterior, colorFondo);
    }

    /**
     * Devuelve el color de hover.
     *
     * @return el color de hover
     */
    public Color getColorHover() {
        return colorHover;
    }

    /**
     * Cambia el color de hover.
     *
     * @param colorHover el nuevo color (no puede ser null)
     */
    public void setColorHover(Color colorHover) {
        Color anterior = this.colorHover;
        this.colorHover = Objects.requireNonNull(colorHover, "colorHover");
        repaint();
        propertySupport.firePropertyChange(PROP_COLOR_HOVER, anterior, colorHover);
    }

    /**
     * Devuelve el color de cuando está activo.
     *
     * @return el color activo
     */
    public Color getColorActivo() {
        return colorActivo;
    }

    /**
     * Cambia el color de cuando está activo.
     *
     * @param colorActivo el nuevo color (no puede ser null)
     */
    public void setColorActivo(Color colorActivo) {
        Color anterior = this.colorActivo;
        this.colorActivo = Objects.requireNonNull(colorActivo, "colorActivo");
        repaint();
        propertySupport.firePropertyChange(PROP_COLOR_ACTIVO, anterior, colorActivo);
    }

    /**
     * Devuelve el color del borde.
     *
     * @return el color del borde
     */
    public Color getColorBorde() {
        return colorBorde;
    }

    /**
     * Cambia el color del borde.
     *
     * @param colorBorde el nuevo color (no puede ser null)
     */
    public void setColorBorde(Color colorBorde) {
        Color anterior = this.colorBorde;
        this.colorBorde = Objects.requireNonNull(colorBorde, "colorBorde");
        repaint();
        propertySupport.firePropertyChange(PROP_COLOR_BORDE, anterior, colorBorde);
    }

    /**
     * Devuelve el color del texto.
     *
     * @return el color del texto
     */
    public Color getColorTexto() {
        return colorTexto;
    }

    /**
     * Cambia el color del texto y del icono.
     *
     * @param colorTexto el nuevo color (no puede ser null)
     */
    public void setColorTexto(Color colorTexto) {
        Color anterior = this.colorTexto;
        this.colorTexto = Objects.requireNonNull(colorTexto, "colorTexto");
        actualizarAspecto();
        propertySupport.firePropertyChange(PROP_COLOR_TEXTO, anterior, colorTexto);
    }

    /**
     * Devuelve el color del texto cuando está deshabilitado.
     *
     * @return el color
     */
    public Color getColorTextoDeshabilitado() {
        return colorTextoDeshabilitado;
    }

    /**
     * Cambia el color del texto cuando está deshabilitado. Por defecto es igual
     * que el normal porque así está en el boceto.
     *
     * @param colorTextoDeshabilitado el nuevo color (no puede ser null)
     */
    public void setColorTextoDeshabilitado(Color colorTextoDeshabilitado) {
        Color anterior = this.colorTextoDeshabilitado;
        this.colorTextoDeshabilitado = Objects.requireNonNull(colorTextoDeshabilitado, "colorTextoDeshabilitado");
        actualizarAspecto();
        propertySupport.firePropertyChange(PROP_COLOR_TEXTO_DESHABILITADO, anterior, colorTextoDeshabilitado);
    }

    /**
     * Devuelve el radio de las esquinas.
     *
     * @return el radio en píxeles
     */
    public int getRadioBorde() {
        return radioBorde;
    }

    /**
     * Cambia el radio de las esquinas.
     *
     * @param radioBorde el radio en píxeles (0 son esquinas rectas)
     * @throws IllegalArgumentException si es negativo
     */
    public void setRadioBorde(int radioBorde) {
        if (radioBorde < 0) {
            throw new IllegalArgumentException("El radio no puede ser negativo: " + radioBorde);
        }
        int anterior = this.radioBorde;
        this.radioBorde = radioBorde;
        repaint();
        propertySupport.firePropertyChange(PROP_RADIO_BORDE, anterior, radioBorde);
    }

    /**
     * Devuelve el estado del botón (NORMAL, HOVER, ACTIVO o DESHABILITADO).
     *
     * @return el estado actual
     */
    public EstadoBoton getEstado() {
        return estado;
    }

    /**
     * Habilita o deshabilita el botón.
     *
     * @param enabled false para deshabilitarlo
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            hover = false;
        }
        if (propertySupport != null) {
            actualizarEstado();
        }
    }

    /**
     * Cambia la fuente del texto.
     *
     * @param font la nueva fuente
     */
    @Override
    public void setFont(Font font) {
        super.setFont(font);
        if (etiqueta != null && font != null) {
            etiqueta.setFont(font);
            revalidate();
        }
    }

    /**
     * Devuelve la fuente del texto.
     *
     * @return la fuente
     */
    @Override
    public Font getFont() {
        return etiqueta != null ? etiqueta.getFont() : super.getFont();
    }

    // Listeners

    /**
     * Añade un listener para enterarse de los eventos del botón.
     *
     * @param listener el listener que se quiere añadir
     */
    public void addBotonBymetooListener(BotonBymetooListener listener) {
        if (listener != null) {
            listenerList.add(BotonBymetooListener.class, listener);
        }
    }

    /**
     * Quita un listener que se había añadido antes.
     *
     * @param listener el listener que se quiere quitar
     */
    public void removeBotonBymetooListener(BotonBymetooListener listener) {
        listenerList.remove(BotonBymetooListener.class, listener);
    }

    /**
     * Devuelve todos los listeners que tiene el botón.
     *
     * @return array con los listeners
     */
    public BotonBymetooListener[] getBotonBymetooListeners() {
        return listenerList.getListeners(BotonBymetooListener.class);
    }

    /**
     * Avisa a los listeners de que el estado ha cambiado.
     *
     * @param anterior el estado de antes
     * @param nuevo    el estado nuevo
     */
    protected void fireEstadoCambiado(EstadoBoton anterior, EstadoBoton nuevo) {
        BotonBymetooEvent evento = new BotonBymetooEvent(
                this, BotonBymetooEvent.Tipo.CAMBIO_ESTADO, anterior, nuevo, ventanaDestino);
        for (BotonBymetooListener l : getBotonBymetooListeners()) {
            l.estadoCambiado(evento);
        }
    }

    /**
     * Avisa a los listeners de que se ha pulsado el botón.
     *
     * @return el evento, para saber si alguien ha cancelado el cambio de ventana
     */
    protected BotonBymetooEvent fireBotonPulsado() {
        BotonBymetooEvent evento = new BotonBymetooEvent(
                this, BotonBymetooEvent.Tipo.PULSADO, estado, estado, ventanaDestino);
        for (BotonBymetooListener l : getBotonBymetooListeners()) {
            l.botonPulsado(evento);
        }
        return evento;
    }

    /**
     * Añade un listener de cambios de propiedades.
     *
     * @param listener el listener
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        if (propertySupport != null) {
            propertySupport.addPropertyChangeListener(listener);
        }
    }

    /**
     * Quita un listener de cambios de propiedades.
     *
     * @param listener el listener
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        super.removePropertyChangeListener(listener);
        if (propertySupport != null) {
            propertySupport.removePropertyChangeListener(listener);
        }
    }

    /**
     * Añade un listener solo para una propiedad, por ejemplo PROP_ACTIVO.
     *
     * @param propiedad el nombre de la propiedad
     * @param listener  el listener
     */
    @Override
    public void addPropertyChangeListener(String propiedad, PropertyChangeListener listener) {
        super.addPropertyChangeListener(propiedad, listener);
        if (propertySupport != null) {
            propertySupport.addPropertyChangeListener(propiedad, listener);
        }
    }

    /**
     * Quita un listener de una propiedad.
     *
     * @param propiedad el nombre de la propiedad
     * @param listener  el listener
     */
    @Override
    public void removePropertyChangeListener(String propiedad, PropertyChangeListener listener) {
        super.removePropertyChangeListener(propiedad, listener);
        if (propertySupport != null) {
            propertySupport.removePropertyChangeListener(propiedad, listener);
        }
    }

    // Clases internas para el ratón y el teclado

    /** Controla cuando el ratón entra, sale o hace clic en el botón. */
    private class ManejadorRaton extends MouseAdapter implements Serializable {
        private static final long serialVersionUID = 1L;

        @Override
        public void mouseEntered(MouseEvent e) {
            if (isEnabled()) {
                hover = true;
                actualizarEstado();
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            hover = false;
            actualizarEstado();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (isEnabled() && isRequestFocusEnabled()) {
                requestFocusInWindow();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // solo cuenta si se suelta con el botón izquierdo encima del botón
            if (e.getButton() == MouseEvent.BUTTON1 && contains(e.getPoint())) {
                pulsar();
            }
        }
    }

    /** Lo que pasa al pulsar Espacio o Intro. */
    private class AccionPulsar extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            pulsar();
        }
    }
}
