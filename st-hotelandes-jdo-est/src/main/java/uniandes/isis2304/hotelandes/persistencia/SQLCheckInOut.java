package uniandes.isis2304.hotelandes.persistencia;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto CheckInOut de HotelAndes
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 */
class SQLCheckInOut 
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
	public SQLCheckInOut (PersistenciaHotelAndes pp)
	{
		this.pp = pp;
	}
	
	public long checkIn (PersistenceManager pm, long idBooking, String today) 
	{
		String sql = "UPDATE " + pp.darTablaCheckInOut() + " SET CHECKINDATE = ";
		sql+= "TO_TIMESTAMP('"+today+"', 'yyyy/mm/dd HH24:MI:SS.FF'), CHECKINMADE = 1";
		sql+= " WHERE IDBOOKING = "+idBooking;
        Query q = pm.newQuery(SQL, sql);
        return (long) q.executeUnique();
	}
	
	
	public long checkOut (PersistenceManager pm, long idBooking, String today) 
	{
		String sql = "UPDATE " + pp.darTablaCheckInOut() + " SET CHECKOUTDATE = ";
		sql+= "TO_TIMESTAMP('"+today+"', 'yyyy/mm/dd HH24:MI:SS.FF'), CHECKOUTMADE = 1, PAID = 1";
		sql+= " WHERE IDBOOKING = "+idBooking;
        Query q = pm.newQuery(SQL, sql);
        return (long) q.executeUnique();
	}

}