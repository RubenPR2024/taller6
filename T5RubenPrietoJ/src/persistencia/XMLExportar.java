package persistencia;

import java.io.File;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import dominio.Incidencias;

/**
 * Clase que se encarga de exportar ficheros a formato XML
 */
public class XMLExportar {

		/**
		 * Método encargado de exportar 
		 * @param listaIncidencias	Listas de Incidencias
		 * @param nombreFichero		Nombre del fichero
		 */
	    public static void exportarAXML(ArrayList<Incidencias> listaIncidencias, String nombreFichero) {
	        try {
	            JAXBContext context = JAXBContext.newInstance(listaIncidenciasWrapper.class);
	            Marshaller marshaller = context.createMarshaller();
	            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	            // Envolver la lista de incidencias en un objeto de contenedor
	            listaIncidenciasWrapper wrapper = new listaIncidenciasWrapper();
	            wrapper.setlistaIncidencias(listaIncidencias);

	            // Marshalling y escritura en el archivo XML
	            marshaller.marshal(wrapper, new File(nombreFichero));
	        } catch (JAXBException e) {
	            e.printStackTrace();
	        }
	    }
	    /**
	     * Clase envoltorio que necesitaremos para la lista de incidencias.
	     */
	    @XmlRootElement(name = "listaIncidencias")
	    static class listaIncidenciasWrapper {
	        private ArrayList<Incidencias> listaIncidencias;

	        @XmlElement(name = "Incidencias")
	        public ArrayList<Incidencias> getlistaIncidencias() {
	            return listaIncidencias;
	        }

	        public void setlistaIncidencias(ArrayList<Incidencias> listaIncidencias) {
	            this.listaIncidencias = listaIncidencias;
	        }
	    }
}
