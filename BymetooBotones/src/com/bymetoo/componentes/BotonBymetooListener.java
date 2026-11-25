package com.bymetoo.componentes;

import java.util.EventListener;

/**
 * Listener para escuchar los eventos de los botones de Bymetoo.
 * <p>
 * Para suscribirse se usa addBotonBymetooListener y para darse de baja
 * removeBotonBymetooListener. Si solo hace falta uno de los dos métodos se
 * puede usar BotonBymetooAdapter.
 * </p>
 *
 * <pre>{@code
 * BotonBymetooListener oyente = new BotonBymetooListener() {
 *     @Override
 *     public void estadoCambiado(BotonBymetooEvent e) {
 *         System.out.println(e.getEstadoAnterior() + " -> " + e.getEstadoNuevo());
 *     }
 *     @Override
 *     public void botonPulsado(BotonBymetooEvent e) {
 *         System.out.println("Voy a " + e.getVentanaDestino());
 *     }
 * };
 * botonGastos1.addBotonBymetooListener(oyente);
 * }</pre>
 *
 * @author Equipo Bymetoo
 */
public interface BotonBymetooListener extends EventListener {

    /**
     * Se llama cuando cambia el estado del botón.
     *
     * @param e el evento con el estado de antes y el nuevo
     */
    void estadoCambiado(BotonBymetooEvent e);

    /**
     * Se llama cuando se pulsa el botón, antes de cambiar de ventana.
     *
     * @param e el evento (se puede llamar a cancelarNavegacion())
     */
    void botonPulsado(BotonBymetooEvent e);
}
