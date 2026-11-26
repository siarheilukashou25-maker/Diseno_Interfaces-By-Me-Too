package com.bymetoo.componentes;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Listener de prueba: guarda todos los eventos que recibe, en orden, para
 * poder contar cuántos han llegado y comprobar sus datos.
 */
class ListenerContador implements BotonBymetooListener, PropertyChangeListener {

    /** Eventos {@code estadoCambiado} recibidos. */
    final List<BotonBymetooEvent> estados = new ArrayList<>();
    /** Eventos {@code botonPulsado} recibidos. */
    final List<BotonBymetooEvent> pulsados = new ArrayList<>();
    /** Eventos {@code propertyChange} recibidos. */
    final List<PropertyChangeEvent> propiedades = new ArrayList<>();
    /** Todos los eventos propios en el orden en que llegaron ("EC:A->B" o "BP"). */
    final List<String> orden = new ArrayList<>();

    /** Si es true, cancela la navegación en cada botonPulsado. */
    boolean cancelarNavegacion;

    @Override
    public void estadoCambiado(BotonBymetooEvent e) {
        estados.add(e);
        orden.add("EC:" + e.getEstadoAnterior() + "->" + e.getEstadoNuevo());
    }

    @Override
    public void botonPulsado(BotonBymetooEvent e) {
        if (cancelarNavegacion) {
            e.cancelarNavegacion();
        }
        pulsados.add(e);
        orden.add("BP");
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        propiedades.add(e);
    }

    /**
     * Cuántos PropertyChangeEvent han llegado con ese nombre de propiedad.
     *
     * @param nombre el nombre de la propiedad, por ejemplo {@code PROP_TEXTO}
     * @return el número de eventos
     */
    long cambiosDe(String nombre) {
        return propiedades.stream().filter(p -> nombre.equals(p.getPropertyName())).count();
    }
}
