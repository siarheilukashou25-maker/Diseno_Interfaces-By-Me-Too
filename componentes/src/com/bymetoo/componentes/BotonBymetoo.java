package com.bymetoo.componentes;

// Java 25: con "import module" se importan de golpe todos los paquetes de
// java.desktop (java.awt, javax.swing, java.beans...) en vez de uno a uno
import module java.desktop;

// Hay dos MouseEvent (el de AWT y uno de XML), así que decimos cuál usamos
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

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
 * <p>
 * Es una clase sellada (sealed): solo los 8 botones de la app pueden heredar
 * de ella.
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
public abstract sealed class BotonBymetoo extends JPanel implements Serializable
        permits BotonInicio, BotonPerfil, BotonCompartir, BotonGastos,
                BotonRepartos, BotonNuevaCompra, BotonResumen, BotonSalir {

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
    /** Nombre de la propiedad colorBordeHover. */
    public static final String PROP_COLOR_BORDE_HOVER = "colorBordeHover";
    /** Nombre de la propiedad colorTexto. */
    public static final String PROP_COLOR_TEXTO = "colorTexto";
    /** Nombre de la propiedad colorTextoDeshabilitado. */
    public static final String PROP_COLOR_TEXTO_DESHABILITADO = "colorTextoDeshabilitado";
    /** Nombre de la propiedad radioBorde. */
    public static final String PROP_RADIO_BORDE = "radioBorde";
    /** Nombre de la propiedad separacionIcono. */
    public static final String PROP_SEPARACION_ICONO = "separacionIcono";
    /** Nombre de la propiedad estado (solo se puede leer). */
    public static final String PROP_ESTADO = "estado";

    // Colores y medidas por defecto (sacados de la tabla de estilos)

    /** Fondo de los botones de grupo: #FAFAFA. */
    public static final Color COLOR_FONDO_POR_DEFECTO = new Color(0xFA, 0xFA, 0xFA);
    /** Hover de Inicio y Perfil: tinte gris rgba(26, 32, 44, 0.08). */
    public static final Color COLOR_HOVER_POR_DEFECTO = new Color(26, 32, 44, 20);
    /** Activo de Inicio y Perfil: tinte gris un poco más marcado rgba(26, 32, 44, 0.14). */
    public static final Color COLOR_ACTIVO_MENU = new Color(26, 32, 44, 36);
    /** Verde menta de los botones de grupo (activo, hover y al pulsar): #81DAAB. */
    public static final Color COLOR_ACTIVO_POR_DEFECTO = new Color(0x81, 0xDA, 0xAB);
    /** Borde de los botones de grupo: #E2E8E5. */
    public static final Color COLOR_BORDE_SUAVE = new Color(0xE2, 0xE8, 0xE5);
    /** Borde gris oscuro, por si se quiere usar en el hover. */
    public static final Color COLOR_BORDE_OSCURO = new Color(0x55, 0x55, 0x55);
    /** Color del texto y del icono: #1A202C. */
    public static final Color COLOR_TEXTO_POR_DEFECTO = new Color(0x1A, 0x20, 0x2C);
    /** Texto e icono cuando está deshabilitado: el mismo color con menos opacidad. */
    public static final Color COLOR_TEXTO_DESHABILITADO = new Color(0x1A, 0x20, 0x2C, 100);
    /** Radio de las esquinas de los botones de grupo (arc = 12). */
    public static final int RADIO_BORDE_POR_DEFECTO = 6;
    /** Separación entre el icono y el texto. */
    public static final int SEPARACION_ICONO_POR_DEFECTO = 12;

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
    /** Color del borde normal. */
    private Color colorBorde;
    /** Color del borde con el ratón encima, al pulsar o activo. */
    private Color colorBordeHover;
    /** Color del texto. */
    private Color colorTexto;
    /** Color del texto cuando está deshabilitado. */
    private Color colorTextoDeshabilitado;
    /** Radio de las esquinas. */
    private int radioBorde;
    /** Si el ratón está encima del botón. */
    private boolean hover;
    /** Si se está manteniendo pulsado el botón con el ratón. */
    private boolean pulsado;
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
        // Java 25: ahora se puede comprobar y guardar cosas antes de llamar a super()
        this.tipoIcono = Objects.requireNonNull(tipoIcono, "tipoIcono");
        this.estilo = Objects.requireNonNull(estilo, "estilo");
        super();

        propertySupport = new PropertyChangeSupport(this);
        etiqueta = new JLabel();

        this.texto = texto == null ? "" : texto;
        this.ventanaDestino = ventanaDestino == null ? "" : ventanaDestino;
        this.navegacionAutomatica = true;
        this.cerrarVentanaActual = true;
        this.activo = false;
        this.iconoPersonalizado = null;
        // Inicio y Perfil (menú) usan tintes grises; los de grupo usan el verde
        var menu = estilo == EstiloBoton.MENU;
        this.colorFondo = COLOR_FONDO_POR_DEFECTO;
        this.colorHover = menu ? COLOR_HOVER_POR_DEFECTO : COLOR_ACTIVO_POR_DEFECTO;
        this.colorActivo = menu ? COLOR_ACTIVO_MENU : COLOR_ACTIVO_POR_DEFECTO;
        this.colorBorde = COLOR_BORDE_SUAVE;
        this.colorBordeHover = COLOR_BORDE_SUAVE;
        this.colorTexto = COLOR_TEXTO_POR_DEFECTO;
        this.colorTextoDeshabilitado = COLOR_TEXTO_DESHABILITADO;
        this.radioBorde = RADIO_BORDE_POR_DEFECTO;
        this.hover = false;
        this.pulsado = false;
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
        etiqueta.setIconTextGap(SEPARACION_ICONO_POR_DEFECTO);
        etiqueta.setBorder(new EmptyBorder(0, 14, 0, 14));
        etiqueta.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        // Para que el texto se vea tal cual y no se interprete como HTML
        // (si no, un texto con <html> podría cambiar el aspecto o cargar imágenes de internet)
        etiqueta.putClientProperty("html.disable", Boolean.TRUE);
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
        var anterior = this.estado;
        if (anterior != nuevo) {
            this.estado = nuevo;
            actualizarAspecto();
            propertySupport.firePropertyChange(PROP_ESTADO, anterior, nuevo);
            fireEstadoCambiado(anterior, nuevo);
        }
    }

    private void actualizarAspecto() {
        var color = estado == EstadoBoton.DESHABILITADO ? colorTextoDeshabilitado : colorTexto;
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
            var clase = Class.forName(ventanaDestino, false, cargadorDeClases());
            return Window.class.isAssignableFrom(clase);
        } catch (ClassNotFoundException | LinkageError _) {
            // Java 22+: "_" porque la excepción no la usamos
            return false;
        }
    }

    private ClassLoader cargadorDeClases() {
        var cargador = Thread.currentThread().getContextClassLoader();
        return cargador != null ? cargador : getClass().getClassLoader();
    }

    /**
     * Dice si el botón está dentro de la ventana a la que lleva.
     *
     * @return true si ya estamos en esa ventana
     */
    public boolean estaEnSuVentanaDestino() {
        var actual = SwingUtilities.getWindowAncestor(this);
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
        var activoAnterior = activo;
        setActivo(true);
        var evento = fireBotonPulsado();
        if (navegacionAutomatica && !evento.isNavegacionCancelada()) {
            try {
                navegar();
            } catch (IllegalStateException e) {
                if (GraphicsEnvironment.isHeadless()) {
                    throw e;
                }
                // Escribimos el error completo en la consola (ventana Output de
                // NetBeans) para ver en qué línea de la vista ha fallado
                e.printStackTrace();
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
        var actual = SwingUtilities.getWindowAncestor(this);
        if (actual != null && actual.getClass().getName().equals(ventanaDestino)) {
            return actual;
        }
        var nueva = crearVentana(ventanaDestino);
        nueva.setLocationRelativeTo(actual);
        // Si es un JDialog modal, setVisible se queda esperando hasta que se cierre
        // el diálogo. En ese caso no cerramos la ventana de atrás, porque si no al
        // cerrar el diálogo la app se quedaría sin ninguna ventana.
        var esModal = nueva instanceof Dialog dialogo && dialogo.isModal();
        nueva.setVisible(true);
        if (cerrarVentanaActual && !esModal && actual != null && actual != nueva) {
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
        Class<?> clase;
        try {
            // false: solo se busca la clase, no se ejecuta nada suyo (como los bloques static)
            // hasta saber que es una ventana. Se inicializa después, al crearla.
            clase = Class.forName(nombreClase, false, cargadorDeClases());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("No se encuentra la ventana '" + nombreClase
                    + "'. Revisa la propiedad ventanaDestino de " + getClass().getSimpleName(), e);
        }
        // Primero comprobamos que es una ventana, para no crear un JPanel u otra cosa
        if (!Window.class.isAssignableFrom(clase)) {
            throw new IllegalStateException(nombreClase + " no es un JFrame/JDialog/Window");
        }
        try {
            Object objeto = clase.getDeclaredConstructor().newInstance();
            // pattern matching: si es una ventana la guardamos directamente en "ventana"
            if (objeto instanceof Window ventana) {
                return ventana;
            }
            throw new IllegalStateException(nombreClase + " no es un JFrame/JDialog/Window");
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("No se pudo crear '" + nombreClase
                    + "'. Necesita un constructor público sin parámetros", e);
        } catch (InvocationTargetException e) {
            // El error ha pasado dentro del constructor de la ventana: enseñamos el error real
            var causa = e.getCause();
            throw new IllegalStateException("Error al abrir '" + nombreClase + "': " + causa, causa);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("No se pudo crear '" + nombreClase + "': " + e, e);
        }
    }

    // Dibujo del botón

    /**
     * Pinta el fondo y el borde dependiendo del estilo y del estado.
     * <ul>
     *   <li>Inicio y Perfil (estilo MENU): rectangulares, sin borde y
     *       transparentes. En hover un tinte gris y activos (o al pulsar) un
     *       tinte gris un poco más marcado.</li>
     *   <li>Botones de grupo: esquinas redondeadas, fondo #FAFAFA y borde
     *       #E2E8E5. En hover, al pulsar y activos se ponen verdes (#81DAAB).</li>
     * </ul>
     * Deshabilitados no cambian el fondo, solo el texto se ve más clarito.
     *
     * @param g el objeto Graphics para dibujar
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        var g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            var w = getWidth();
            var h = getHeight();
            var deshabilitado = estado == EstadoBoton.DESHABILITADO;
            var activoOPulsado = !deshabilitado && (estado == EstadoBoton.ACTIVO || pulsado);
            var encima = !deshabilitado && estado == EstadoBoton.HOVER;

            if (estilo == EstiloBoton.MENU) {
                // Normal y deshabilitado: transparente (no se pinta nada)
                if (activoOPulsado) {
                    g2.setColor(colorActivo);
                    g2.fillRect(0, 0, w, h);
                } else if (encima) {
                    g2.setColor(colorHover);
                    g2.fillRect(0, 0, w, h);
                }
            } else {
                var forma = new RoundRectangle2D.Double(
                        0.5, 0.5, w - 1.0, h - 1.0, radioBorde * 2.0, radioBorde * 2.0);
                if (activoOPulsado) {
                    g2.setColor(colorActivo);
                } else if (encima) {
                    g2.setColor(colorHover);
                } else {
                    g2.setColor(colorFondo);
                }
                g2.fill(forma);
                g2.setColor(activoOPulsado || encima ? colorBordeHover : colorBorde);
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
        var contenido = etiqueta.getPreferredSize();
        var menu = estilo == EstiloBoton.MENU;
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
        var anterior = this.texto;
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
        var anterior = this.ventanaDestino;
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
     * Devuelve el color del borde con el ratón encima.
     *
     * @return el color del borde en hover
     */
    public Color getColorBordeHover() {
        return colorBordeHover;
    }

    /**
     * Cambia el color del borde con el ratón encima (también al pulsar y activo).
     *
     * @param colorBordeHover el nuevo color (no puede ser null)
     */
    public void setColorBordeHover(Color colorBordeHover) {
        Color anterior = this.colorBordeHover;
        this.colorBordeHover = Objects.requireNonNull(colorBordeHover, "colorBordeHover");
        repaint();
        propertySupport.firePropertyChange(PROP_COLOR_BORDE_HOVER, anterior, colorBordeHover);
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
     * Cambia el color del texto cuando está deshabilitado. Por defecto es el
     * color normal con menos opacidad.
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
     * Devuelve la separación entre el icono y el texto.
     *
     * @return la separación en píxeles
     */
    public int getSeparacionIcono() {
        return etiqueta.getIconTextGap();
    }

    /**
     * Cambia la separación entre el icono y el texto (el iconTextGap).
     *
     * @param separacionIcono la separación en píxeles
     * @throws IllegalArgumentException si es negativa
     */
    public void setSeparacionIcono(int separacionIcono) {
        if (separacionIcono < 0) {
            throw new IllegalArgumentException("La separación no puede ser negativa: " + separacionIcono);
        }
        int anterior = etiqueta.getIconTextGap();
        etiqueta.setIconTextGap(separacionIcono);
        revalidate();
        repaint();
        propertySupport.firePropertyChange(PROP_SEPARACION_ICONO, anterior, separacionIcono);
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
        var evento = new BotonBymetooEvent(
                this, BotonBymetooEvent.Tipo.CAMBIO_ESTADO, anterior, nuevo, ventanaDestino);
        for (var l : getBotonBymetooListeners()) {
            l.estadoCambiado(evento);
        }
    }

    /**
     * Avisa a los listeners de que se ha pulsado el botón.
     *
     * @return el evento, para saber si alguien ha cancelado el cambio de ventana
     */
    protected BotonBymetooEvent fireBotonPulsado() {
        var evento = new BotonBymetooEvent(
                this, BotonBymetooEvent.Tipo.PULSADO, estado, estado, ventanaDestino);
        for (var l : getBotonBymetooListeners()) {
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
            pulsado = false;
            actualizarEstado();
            repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (isEnabled() && isRequestFocusEnabled()) {
                requestFocusInWindow();
            }
            // mientras se mantiene pulsado se pinta con el color de pulsar
            if (isEnabled() && e.getButton() == MouseEvent.BUTTON1) {
                pulsado = true;
                repaint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            pulsado = false;
            repaint();
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
