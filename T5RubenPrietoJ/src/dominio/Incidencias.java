package dominio;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.*;

@XmlRootElement(name="incidencias")
@XmlType(propOrder = {"fechaIncidencia","identificador","estado","puesto","descripcion","fechaRegistro","fechaResolucion","fechaEliminacion","causaEliminacion", "resolucion"})
@XmlAccessorType (XmlAccessType.FIELD)

/**
 * Clase principal para crear la incidencia o representar una incidencia
 */
public class Incidencias implements Serializable{
	/**
	 * ID Serializable
	 */
	private static final long serialVersionUID = 1L;
	/**
     * Estado de la incidencia.
     */
	@XmlAttribute (name = "estado")
    private Estado estado;
	/**
     * Fecha de la incidencia.
     */
	@XmlAttribute (name = "fechaIncidencia")
    private Date fechaIncidencia;
	/**
     * Identificador de la incidencia.
     */
	@XmlAttribute (name = "identificador")
    private String identificador;
	/**
     * Descripción de la incidencia.
     */
	@XmlAttribute (name = "descripcion")
    private String descripcion;
	/**
     * Puesto relacionado con la incidencia.
     */
	@XmlAttribute (name = "puesto")
    private int puesto;
	/**
     * Fecha de registro de la incidencia.
     */
	@XmlAttribute (name = "fechaRegistro")
    private Date fechaRegistro;
	/**
     * Fecha de eliminación de la incidencia.
     */
	@XmlAttribute (name = "fechaEliminacion")
    private Date fechaEliminacion;
	/**
     * Fecha de resolución de la incidencia.
     */
	@XmlAttribute (name = "fechaResolucion")
    private Date fechaResolucion;
	/**
     * Causa de eliminación de la incidencia.
     */
	@XmlAttribute (name = "causaEliminacion")
    private String causaEliminacion;
	/**
     * Resolución de la incidencia.
     */
	@XmlAttribute (name = "resolucion")
    private String resolucion;

	/**
	 * Constructor de la clase Incidencias
	 * @param codigo		Identificador unico de la incidencia
	 * @param estado		Estado de la incidencia
	 * @param puesto		Puesto relacionado con la incidencia
	 * @param descripcion	Descripcion de la incidencia
	 */
    public Incidencias(String codigo, Estado estado, int puesto, String descripcion) {
        this.identificador = codigo;
        this.estado = Estado.Pendiente;
        this.puesto = puesto;
        this.descripcion = descripcion;
        this.fechaRegistro = new Date();

        // Extraemos y parseamos la fecha del código
        String[] partes = codigo.split("-");
        String fechaStr = partes[0] + "-" + partes[1];
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm");
        try {
            this.fechaIncidencia = sdf.parse(fechaStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // Getters y Setters

    /**
     * Getter de estado
     * @return	Estado
     */
    public Estado getEstado() {
        return estado;
    }

    /**
     * Setter de estado
     * @param estado Estado
     */
    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    /**
     * Getter de fecha de incidencia
     * @return Fecha de incidencia
     */
    public Date getFechaIncidencia() {
        return fechaIncidencia;
    }

    /**
     * Setter fecha de incidencia
     * @param fechaIncidencia Fecha de incidencia
     */
    public void setFechaIncidencia(Date fechaIncidencia) {
        this.fechaIncidencia = fechaIncidencia;
    }

    /**
     * Getter de identificador
     * @return Identificador
     */
    public String getIdentificador() {
        return identificador;
    }

    /**
     * Setter de identificador
     * @param identificador	Identificador
     */
    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    /**
     * Getter de descripcion
     * @return	Descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Setter de descripcion
     * @param descripcion	Descripcion
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Getter de fecha de registro
     * @return	Fecha de registro
     */
    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    /**
     * Setter de fecha de registro
     * @param fechaRegistro	Fecha de registro
     */
    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    /**
     * Getter de puesto
     * @return	puesto
     */
    public int getPuesto() {
        return puesto;
    }

    /**
     * Setter de puesto
     * @param puesto	Puesto
     */
    public void setPuesto(int puesto) {
        this.puesto = puesto;
    }

    /**
     * Getter de fecha de eliminacion
     * @return	Fecha de eliminacion
     */
    public Date getFechaEliminacion() {
        return fechaEliminacion;
    }

    /**
     * Setter de fecha de eliminacion
     * @param fechaEliminacion	Fecha de eliminacion
     */
    public void setFechaEliminacion(Date fechaEliminacion) {
        this.fechaEliminacion = fechaEliminacion;
    }

    /**
     * Getter de fecha de resolucion
     * @return	Fecha de resolucion
     */
    public Date getFechaResolucion() {
        return fechaResolucion;
    }

    /**
     * Setter de fecha de resolucion
     * @param fechaResolucion	Fecha de resolucion
     */
    public void setFechaResolucion(Date fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    /**
     * Getter de causa de eliminacion
     * @return	Causa de eliminacion
     */
    public String getCausaEliminacion() {
        return causaEliminacion;
    }

    /**
     * Setter de causa de eliminacion
     * @param causaEliminacion	Causa de eliminacion
     */
    public void setCausaEliminacion(String causaEliminacion) {
        this.causaEliminacion = causaEliminacion;
    }

    /**
     * Getter de resolucion
     * @return	Resolucion
     */
    public String getResolucion() {
        return resolucion;
    }

    /**
     * Setter de resolucion
     * @param resolucion	Resolucion
     */
    public void setResolucion(String resolucion) {
        this.resolucion = resolucion;
    }

    /**
     * Elimina la incidencia
     * @param fechaEliminacion	Fecha de eliminacion de la incidencia.
     * @param causaEliminacion	Causa de eliminacion de la incidencia.
     */
    public void eliminarIncidencia(Date fechaEliminacion, String causaEliminacion) {
        this.estado = Estado.Eliminada;
        this.fechaEliminacion = fechaEliminacion;
        this.causaEliminacion = causaEliminacion;
    }

    /**
     * Resuelve la incidencia
     * @param fechaResolucion	Fecha de resolucion de la incidencia.
     * @param resolucion		Resolucion de la incidencia.
     */
    public void resolverIncidencia(Date fechaResolucion, String resolucion) {
        this.estado = Estado.Resuelta;
        this.fechaResolucion = fechaResolucion;
        this.resolucion = resolucion;
    }

    /**
     * Modifica la incidencia resuelta.
     * @param nuevaDescripcion		Nueva descripcion de la incidencia.
     * @param nuevaFechaResolucion	Nueva fecha de resolucion de la incidencia.
     * @param nuevaResolucion		Nueva resolucion de la incidencia.
     */
    public void modificarIncidenciaResuelta(String nuevaDescripcion, Date nuevaFechaResolucion, String nuevaResolucion) {
        if (this.estado == Estado.Resuelta) {
            this.descripcion = nuevaDescripcion;
            this.fechaResolucion = nuevaFechaResolucion;
            this.resolucion = nuevaResolucion;
        }
    }

    /**
     * Modifica una incidencia
     * @param identificador	Identificador de la incidencia a modificar
     * @param nuevaDescripcion	Nueva descripcion de la incidencia.
     */
    public void modificarIncidencia(String identificador, String nuevaDescripcion) {
        if (this.estado == Estado.Pendiente) {
            this.descripcion = nuevaDescripcion;
        }
    }

    /**
     * Representacion en forma de cadena de la incidencia
     * @return Una cadena que representa la incidencia.
     */
    @Override
    public String toString() {
    	 SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    	    String fechaResolucion2 = (fechaResolucion != null) ? dateFormat.format(fechaResolucion) : "No disponible";
    	    String fechaEliminacion2 = (fechaEliminacion != null) ? dateFormat.format(fechaEliminacion) : "No disponible";
        return "Código: " + identificador + "\n" +
               "Estado: " + estado + "\n" +
               "Puesto: " + puesto + "\n" +
               "Problema: " + descripcion + "\n" +
               (estado == Estado.Eliminada ? "Fecha de eliminación: " + fechaEliminacion2 + "\n" + "Causa de eliminación: " + causaEliminacion + "\n" : "") +
               (estado == Estado.Resuelta ? "Fecha de resolución: " + fechaResolucion2 + "\n" + "Resolución: " + resolucion + "\n" : "");
    }
}
