/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad	de	los	Andes	(Bogotá	- Colombia)
 * Departamento	de	Ingeniería	de	Sistemas	y	Computación
 * Licenciado	bajo	el	esquema	Academic Free License versión 2.1
 * 		
 * Curso: isis2304 - Sistemas Transaccionales
 * Proyecto: HotelAndes
 * @version 1.0
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package uniandes.isis2304.hotelandes.negocio;

import java.sql.Timestamp;

/**
 * Clase para modelar el concepto SERVICEINVOICE del negocio de HotelAndes
 *
 */
public class Reservation implements VOReservation
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El identificador ÚNICO del Reservation
	 */
	private long id;
	
	/**
	 * El identificador del Service de Reservation. Debe existir en la tabla SERVICE.
	 */
	private long idService;

	/**
	 * El numero de documento del User de Reservation. Debe existir en la tabla USER.
	 */
	private long documentUser;
	
	/**
	 * El número de invitados
	 */
	private int numberofGuests;
	
	
	/**
	 * La duración de la reserva
	 */
	private int duration;
	
	/**
	 * La fecha de la reserva
	 */
	private Timestamp date;

	

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Constructor por defecto
	 */
	public Reservation() 
	{
		this.id = 0;
		this.idService = 0;
		this.numberofGuests = 0;
		this.documentUser = 0;
		this.duration = 0;
		this.date = new Timestamp (0);
	}


	/**
	 * Constructor con valores
	 */

	public Reservation(long id, long idService, long documentUser, int numberofGuests, int duration, Timestamp date) {
		this.id = id;
		this.idService = idService;
		this.documentUser = documentUser;
		this.numberofGuests = numberofGuests;
		this.duration = duration;
		this.date = date;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public long getIdService() {
		return idService;
	}


	public void setIdService(long idService) {
		this.idService = idService;
	}


	public long getDocumentUser() {
		return documentUser;
	}


	public void setDocumentUser(long documentUser) {
		this.documentUser = documentUser;
	}


	public int getNumberofGuests() {
		return numberofGuests;
	}


	public void setNumberofGuests(int numberofGuests) {
		this.numberofGuests = numberofGuests;
	}


	public int getDuration() {
		return duration;
	}


	public void setDuration(int duration) {
		this.duration = duration;
	}


	public Timestamp getDate() {
		return date;
	}


	public void setDate(Timestamp date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Reservation [date=" + date + ", documentUser=" + documentUser + ", duration=" + duration + ", id=" + id
				+ ", idService=" + idService + ", numberofGuests=" + numberofGuests + "]";
	}

	
}
