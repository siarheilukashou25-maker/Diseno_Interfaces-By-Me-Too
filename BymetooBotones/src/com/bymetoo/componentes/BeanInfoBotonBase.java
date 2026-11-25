package com.bymetoo.componentes;

// Java 25: importa todo java.desktop (java.beans, java.awt...) de una vez
import module java.desktop;

/**
 * Clase con lo que tienen en común los BeanInfo de los 8 botones.
 * <p>
 * Sirve para que en NetBeans solo salgan nuestras propiedades en la ventana
 * Properties (y no todas las de JPanel), para que salga nuestro evento en la
 * pestaña Events y para poner el icono del botón en la paleta.
 * </p>
 *
 * @author Equipo Bymetoo
 */
public abstract class BeanInfoBotonBase extends SimpleBeanInfo {

    /** Clase del botón. */
    private final Class<? extends BotonBymetoo> clase;
    /** Nombre que sale en la paleta. */
    private final String nombre;
    /** Nombre del icono en la carpeta iconos. */
    private final String icono;

    /**
     * Constructor.
     *
     * @param clase  la clase del botón
     * @param nombre el nombre que sale en la paleta
     * @param icono  el nombre del icono, por ejemplo "boton_inicio"
     */
    protected BeanInfoBotonBase(Class<? extends BotonBymetoo> clase, String nombre, String icono) {
        this.clase = clase;
        this.nombre = nombre;
        this.icono = icono;
    }

    /**
     * Devuelve el nombre y la descripción del botón.
     *
     * @return el descriptor del bean
     */
    @Override
    public BeanDescriptor getBeanDescriptor() {
        var bd = new BeanDescriptor(clase);
        bd.setDisplayName(nombre);
        bd.setShortDescription("Botón " + nombre + " de Bymetoo. Cambia de ventana al pulsarlo.");
        return bd;
    }

    /**
     * Devuelve las propiedades que salen en NetBeans.
     *
     * @return las propiedades
     */
    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            return new PropertyDescriptor[] {
                prop("texto", "getTexto", "setTexto", "Texto del botón.", true),
                prop("ventanaDestino", "getVentanaDestino", "setVentanaDestino",
                        "Ventana a la que va (paquete.Clase).", true),
                prop("navegacionAutomatica", "isNavegacionAutomatica", "setNavegacionAutomatica",
                        "Si es true cambia de ventana solo.", true),
                prop("cerrarVentanaActual", "isCerrarVentanaActual", "setCerrarVentanaActual",
                        "Si es true cierra la ventana actual.", true),
                prop("activo", "isActivo", "setActivo", "Pone el botón activo.", false),
                prop("iconoPersonalizado", "getIconoPersonalizado", "setIconoPersonalizado",
                        "Icono propio.", false),
                prop("colorFondo", "getColorFondo", "setColorFondo", "Color de fondo.", false),
                prop("colorHover", "getColorHover", "setColorHover", "Color con el ratón encima (tinte gris en menú, verde en grupo).", false),
                prop("colorActivo", "getColorActivo", "setColorActivo", "Color cuando está activo.", false),
                prop("colorBorde", "getColorBorde", "setColorBorde", "Color del borde.", false),
                prop("colorBordeHover", "getColorBordeHover", "setColorBordeHover",
                        "Color del borde con el ratón encima.", false),
                prop("colorTexto", "getColorTexto", "setColorTexto", "Color del texto.", false),
                prop("colorTextoDeshabilitado", "getColorTextoDeshabilitado", "setColorTextoDeshabilitado",
                        "Color del texto deshabilitado.", false),
                prop("radioBorde", "getRadioBorde", "setRadioBorde", "Radio de las esquinas.", false),
                prop("separacionIcono", "getSeparacionIcono", "setSeparacionIcono",
                        "Separación entre el icono y el texto.", false),
                prop("estado", "getEstado", null, "Estado del botón (solo lectura).", false),
                prop("tipoIcono", "getTipoIcono", null, "Icono del botón (solo lectura).", false),
                prop("estilo", "getEstilo", null, "Estilo del botón (solo lectura).", false),
                heredada("enabled", "isEnabled", "setEnabled"),
                heredada("font", "getFont", "setFont"),
                heredada("toolTipText", "getToolTipText", "setToolTipText"),
                heredada("preferredSize", "getPreferredSize", "setPreferredSize")
            };
        } catch (IntrospectionException e) {
            return super.getPropertyDescriptors();
        }
    }

    /**
     * Devuelve los eventos que salen en la pestaña Events.
     *
     * @return los eventos
     */
    @Override
    public EventSetDescriptor[] getEventSetDescriptors() {
        try {
            var propio = new EventSetDescriptor(clase, "botonBymetoo",
                    BotonBymetooListener.class, new String[] {"estadoCambiado", "botonPulsado"},
                    "addBotonBymetooListener", "removeBotonBymetooListener");
            propio.setPreferred(true);
            var cambios = new EventSetDescriptor(clase, "propertyChange",
                    PropertyChangeListener.class, new String[] {"propertyChange"},
                    "addPropertyChangeListener", "removePropertyChangeListener");
            return new EventSetDescriptor[] {propio, cambios};
        } catch (IntrospectionException e) {
            return super.getEventSetDescriptors();
        }
    }

    /**
     * Dice cuál es el evento que se crea al hacer doble clic en el botón en NetBeans.
     *
     * @return 0 (el evento botonBymetoo)
     */
    @Override
    public int getDefaultEventIndex() {
        return 0;
    }

    /**
     * Devuelve el icono del botón para la paleta.
     *
     * @param iconKind el tamaño de icono que pide NetBeans
     * @return el icono de 16x16 o 32x32
     */
    @Override
    public Image getIcon(int iconKind) {
        // switch que devuelve un valor (switch expression)
        return switch (iconKind) {
            case ICON_COLOR_16x16, ICON_MONO_16x16 -> loadImage("/iconos/" + icono + "_16.png");
            case ICON_COLOR_32x32, ICON_MONO_32x32 -> loadImage("/iconos/" + icono + "_32.png");
            default -> null;
        };
    }

    // Método para no repetir tanto código al crear cada propiedad
    private PropertyDescriptor prop(String n, String get, String set, String desc, boolean pref)
            throws IntrospectionException {
        var pd = new PropertyDescriptor(n, clase, get, set);
        pd.setBound(true);
        pd.setPreferred(pref);
        pd.setShortDescription(desc);
        return pd;
    }

    private PropertyDescriptor heredada(String n, String get, String set) throws IntrospectionException {
        return new PropertyDescriptor(n, clase, get, set);
    }
}
