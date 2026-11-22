package com.bymetoo.componentes;

/**
 * Clase con los métodos del listener vacíos, para poder sobrescribir solo el
 * que haga falta (igual que el MouseAdapter de Swing).
 *
 * <pre>{@code
 * botonNuevaCompra1.addBotonBymetooListener(new BotonBymetooAdapter() {
 *     @Override
 *     public void botonPulsado(BotonBymetooEvent e) {
 *         System.out.println("Nueva compra");
 *     }
 * });
 * }</pre>
 *
 * @author Equipo Bymetoo
 */
public abstract class BotonBymetooAdapter implements BotonBymetooListener {

    /**
     * Constructor vacío.
     */
    protected BotonBymetooAdapter() {
    }

    /**
     * No hace nada.
     *
     * @param e el evento
     */
    @Override
    public void estadoCambiado(BotonBymetooEvent e) {
    }

    /**
     * No hace nada.
     *
     * @param e el evento
     */
    @Override
    public void botonPulsado(BotonBymetooEvent e) {
    }
}
