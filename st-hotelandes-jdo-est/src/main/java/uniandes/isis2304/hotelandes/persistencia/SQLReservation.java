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

import uniandes.isis2304.hotelandes.negocio.Reservation;
import uniandes.isis2304.hotelandes.negocio.ServiceInvoice;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto RESERVATION de HotelAndes
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 */
class SQLReservation
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
	public SQLReservation (PersistenciaHotelAndes pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para agregar una reserva para un servicio, siempre que esté disponible
	 * @throws Exception
	 */
	
	public long adicionarReserva (PersistenceManager pm, long id, long idService, long documentUser, int numberofGuests, int duration, Timestamp date) throws Exception 
	{
		// Adiciona los minutos de acuerdo a la duracion
		Timestamp date2 = new Timestamp(date.getTime() + (1000 * 60 * duration));
		String sql =  "SELECT * FROM " + pp.darTablaReservation() + " WHERE IDSERVICE = "+idService;
		// Verifica que no exista una fecha que se cruce
		sql+= " AND TO_TIMESTAMP('"+date+"', 'yyyy/mm/dd HH24:MI:SS.FF') BETWEEN \"DATE\" AND (\"DATE\"+ INTERVAL '"+duration+"' MINUTE)";
		sql+= " OR TO_TIMESTAMP('"+date2+"', 'yyyy/mm/dd HH24:MI:SS.FF') BETWEEN \"DATE\" AND (\"DATE\"+ INTERVAL '"+duration+"' MINUTE)";
		Query d = pm.newQuery(SQL, sql);
        List<Reservation> reservations = (List<Reservation>) d.executeList();
		if(reservations.size()>0){
			throw new Exception("El servicio no está disponible");
		}
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaReservation() + "(ID, IDSERVICE, DOCUMENTUSER, NUMBEROFGUESTS, \"DURATION\", \"DATE\") values (?, ?, ?, ?, ?, ?)");
        q.setParameters(id, idService, documentUser, numberofGuests, duration, date);
        return (long) q.executeUnique();            
	}
}
