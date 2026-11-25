package com.bymetoo.componentes;

/**
 * Los iconos que pueden llevar los botones. Se dibujan en la clase
 * IconoNavegacion.
 *
 * <pre>{@code
 * TipoIcono icono = new BotonNuevaCompra().getTipoIcono(); // NUEVA_COMPRA
 * }</pre>
 *
 * @author Equipo Bymetoo
 */
public enum TipoIcono {

    /** Sin icono. */
    NINGUNO,

    /** Dos personas (Inicio). */
    USUARIOS,

    /** Una persona (Perfil). */
    PERFIL,

    /** Icono de compartir (Compartir). */
    COMPARTIR,

    /** Billete (Gastos). */
    GASTOS,

    /** Corazón con manos (Repartos). */
    REPARTOS,

    /** Carrito (Nueva compra). */
    NUEVA_COMPRA,

    /** Gráfico de barras (Resumen). */
    RESUMEN,

    /** Flecha de salir (Salir). */
    SALIR
}
