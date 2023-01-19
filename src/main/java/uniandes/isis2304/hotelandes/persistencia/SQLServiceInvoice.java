/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad	de	los	Andes	(Bogotá	- Colombia)
 * Departamento	de	Ingeniería	de	Sistemas	y	Computación
 * Licenciado	bajo	el	esquema	Academic Free License versión 2.1
 * 		
 * Curso: isis2304 - Sistemas Transaccionales
 * Proyecto: Parranderos Uniandes
 * @version 1.0
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package uniandes.isis2304.hotelandes.persistencia;

import java.sql.Timestamp;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.hotelandes.negocio.ServiceInvoice;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto SERVICEINVOICE de HotelAndes
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 */
class SQLServiceInvoice
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra acá para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaHotelAndes.SQL;

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia general de la aplicación
	 */
	private PersistenciaHotelAndes pp;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Constructor
	 * @param pp - El Manejador de persistencia de la aplicación
	 */
	public SQLServiceInvoice (PersistenciaHotelAndes pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para registrar un consumo de un servicio del hotel por parte de un cliente o sus acompañantes
	 * @param pm - El manejador de persistencia
	 * @param id - El identificador de ServiceInvoice 
	 * @param idService - El identificador de Service
	 * @param value - El valor del consumo
	 * @param createdDate - La fecha de creación
	 * @return EL número de tuplas insertadas
	 * 
	 * 
	 */
	
	public long adicionarConsumo (PersistenceManager pm, long id, long id2, long idService, long idBooking, Double value, Timestamp createdDate, Boolean cuenta) 
	{		
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaServiceInvoice () + "(id, idService, value, createdDate) values (?, ?, ?, ?)");
        q.setParameters(id, idService, value, createdDate);
		Query q2 = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaConsumedServices() + "(idBooking, idServiceInvoice) values (?, ?)");
		q2.setParameters(idBooking, id);
		q2.executeUnique();
		if(cuenta){
			Query q3 = pm.newQuery(SQL, "UPDATE " + pp.darTablaBooking() + "SET TOTALVALUE = TOTALVALUE + "+value+" WHERE ID = "+idBooking);
			q3.executeUnique();
		}
        return (long) q.executeUnique();            
	}
}
