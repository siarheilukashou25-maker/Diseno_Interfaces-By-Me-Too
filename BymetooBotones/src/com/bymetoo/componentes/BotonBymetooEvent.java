package com.bymetoo.componentes;

import java.util.EventObject;

/**
 * Evento propio de los botones de Bymetoo.
 * <p>
 * Hay dos tipos: CAMBIO_ESTADO, cuando el botón cambia de estado (por ejemplo
 * de NORMAL a HOVER), y PULSADO, cuando se pulsa el botón. Si en el evento
 * PULSADO se llama a cancelarNavegacion() el botón no cambia de ventana.
 * </p>
 *
 * <pre>{@code
 * botonSalir1.addBotonBymetooListener(new BotonBymetooAdapter() {
 *     @Override
 *     public void botonPulsado(BotonBymetooEvent e) {
 *         int r = JOptionPane.showConfirmDialog(null, "¿Seguro que quieres salir?");
 *         if (r != JOptionPane.YES_OPTION) {
 *             e.cancelarNavegacion();
 *         }
 *     }
 * });
 * }</pre>
 *
 * @author Equipo Bymetoo
 */
public class BotonBymetooEvent extends EventObject {

    private static final long serialVersionUID = 1L;

    /** Los tipos de evento que hay. */
    public enum Tipo {
        /** Ha cambiado el estado del botón. */
        CAMBIO_ESTADO,
        /** Se ha pulsado el botón. */
        PULSADO
    }

    /** Tipo de evento. */
    private final Tipo tipo;
    /** Estado que tenía antes. */
    private final EstadoBoton estadoAnterior;
    /** Estado que tiene ahora. */
    private final EstadoBoton estadoNuevo;
    /** Ventana a la que va el botón. */
    private final String ventanaDestino;
    /** Si alguien ha cancelado el cambio de ventana. */
    private boolean navegacionCancelada;

    /**
     * Crea el evento.
     *
     * @param origen         el botón que lanza el evento
     * @param tipo           el tipo de evento
     * @param estadoAnterior el estado de antes
     * @param estadoNuevo    el estado nuevo
     * @param ventanaDestino la ventana a la que va el botón
     */
    public BotonBymetooEvent(BotonBymetoo origen, Tipo tipo, EstadoBoton estadoAnterior,
            EstadoBoton estadoNuevo, String ventanaDestino) {
        super(origen);
        this.tipo = tipo;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.ventanaDestino = ventanaDestino;
    }

    /**
     * Devuelve el botón que ha lanzado el evento.
     *
     * @return el botón
     */
    public BotonBymetoo getBoton() {
        return (BotonBymetoo) getSource();
    }

    /**
     * Devuelve el tipo de evento.
     *
     * @return CAMBIO_ESTADO o PULSADO
     */
    public Tipo getTipo() {
        return tipo;
    }

    /**
     * Devuelve el estado de antes.
     *
     * @return el estado anterior
     */
    public EstadoBoton getEstadoAnterior() {
        return estadoAnterior;
    }

    /**
     * Devuelve el estado nuevo.
     *
     * @return el estado nuevo
     */
    public EstadoBoton getEstadoNuevo() {
        return estadoNuevo;
    }

    /**
     * Devuelve la ventana a la que va el botón.
     *
     * @return por ejemplo "vista.RepartosView"
     */
    public String getVentanaDestino() {
        return ventanaDestino;
    }

    /**
     * Hace que el botón no cambie de ventana.
     */
    public void cancelarNavegacion() {
        this.navegacionCancelada = true;
    }

    /**
     * Dice si se ha cancelado el cambio de ventana.
     *
     * @return true si se ha cancelado
     */
    public boolean isNavegacionCancelada() {
        return navegacionCancelada;
    }

    /**
     * Devuelve el evento en texto (para hacer pruebas).
     *
     * @return el evento en texto
     */
    @Override
    public String toString() {
        return getSource().getClass().getSimpleName() + "Event[tipo=" + tipo + ", " + estadoAnterior
                + " -> " + estadoNuevo + ", destino=" + ventanaDestino + "]";
    }
}
