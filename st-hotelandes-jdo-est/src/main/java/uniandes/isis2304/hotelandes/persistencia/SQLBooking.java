package uniandes.isis2304.hotelandes.persistencia;

import java.sql.Timestamp;
import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto Booking de HotelAndes
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 */
class SQLBooking 
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
	public SQLBooking (PersistenciaHotelAndes pp)
	{
		this.pp = pp;
	}
	
	public long registrarBooking (PersistenceManager pm, long id, long idDocumentUser, long idRoom, int numberOfAdults, int numberOfChildren, String type, double basePrice,
			int minRequiredNights, double accommodationPrice, double accomodationDiscount, String paymentPlan,
			double totalValue, Date paidDate, boolean paid, Timestamp fechaEntrada, Timestamp fechaSalida) 
	{
        Query qBooking = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaBooking() + 
        		"(ID, IDROOM, NUMBEROFADULTS, NUMBEROFCHILDREN, TYPE, BASEPRICE, MINREQUIREDNIGHTS, ACCOMODATIONPRICE, ACCOMMODATIONDISCOUNT, PAYMENTPLAN, TOTALVALUE, PAIDDATE, PAID) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        qBooking.setParameters(id, idRoom, numberOfAdults, numberOfChildren, type, basePrice, minRequiredNights, accommodationPrice, accomodationDiscount, paymentPlan, totalValue, paidDate, paid);
		long book = (long) qBooking.executeUnique();
		Query qCheckInOut = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaCheckInOut() + 
        		"(IDBOOKING, DOCUMENTUSER, CHECKINDATE, CHECKINMADE, CHECKOUTDATE, CHECKOUTMADE) values (?, ?, ?, ?, ?, ?)");
		System.out.println(idDocumentUser);		
        qCheckInOut.setParameters(id, idDocumentUser, null, 0, null, 0);
		qCheckInOut.executeUnique();
		Query qOccupancyDate = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaOccupancyDate() + 
		"(ID, IDROOM, DATESTART, DATEFINISH) values (?, ?, ?, ?)");
		qOccupancyDate.setParameters(id, idRoom, fechaEntrada, fechaSalida);
		qOccupancyDate.executeUnique();
        return book;
	}

}
