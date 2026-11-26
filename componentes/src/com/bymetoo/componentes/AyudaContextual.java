package com.bymetoo.componentes;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Objects;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 * Utilidad para asociar ayuda contextual a un componente Swing mediante F1.
 * <p>
 * La ayuda se muestra únicamente cuando el componente registrado tiene el
 * foco. La clase no modifica la jerarquía de los botones ni depende de las
 * ventanas de la aplicación que los utilice.
 * </p>
 *
 * <pre>{@code
 * BotonGastos boton = new BotonGastos();
 * AyudaContextual.instalar(boton, "Ayuda - Gastos",
 *         "Abre la pantalla de gastos. Pulsa el botón para continuar.");
 * }</pre>
 *
 * @author Equipo Bymetoo
 */
public final class AyudaContextual {

    /** Nombre de la acción registrada en el mapa de acciones Swing. */
    private static final String ACCION_AYUDA = "mostrarAyudaContextual";

    /**
     * Constructor privado para evitar crear objetos de esta clase de utilidad.
     */
    private AyudaContextual() {
    }

    /**
     * Registra la tecla F1 para mostrar una ventana con la ayuda indicada.
     * <p>
     * El atajo se registra con {@link JComponent#WHEN_FOCUSED}; por tanto,
     * solo se ejecuta cuando el componente tiene el foco. Si se vuelve a
     * llamar a este método sobre el mismo componente, se actualiza el texto
     * de ayuda asociado.
     * </p>
     *
     * @param componente componente Swing que recibirá la ayuda
     * @param titulo título de la ventana de ayuda
     * @param mensaje texto explicativo que se mostrará
     * @throws NullPointerException si algún argumento es {@code null}
     */
    public static void instalar(JComponent componente, String titulo, String mensaje) {
        Objects.requireNonNull(componente, "El componente no puede ser null");
        Objects.requireNonNull(titulo, "El título no puede ser null");
        Objects.requireNonNull(mensaje, "El mensaje no puede ser null");

        InputMap entrada = componente.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap acciones = componente.getActionMap();

        entrada.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), ACCION_AYUDA);
        acciones.put(ACCION_AYUDA, new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                        componente,
                        mensaje,
                        titulo,
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
    }
}
