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
 * Clase para modelar el concepto OccupancyDate del negocio de HotelAndes
 *
 */
public class OccupancyDate implements VOOccupancyDate
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	private long id;

	private long idRoom;

	private Timestamp dateStart;

	private Timestamp dateFinish;
	

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Constructor por defecto
	 */

	public OccupancyDate(long id, long idRoom, Timestamp dateStart, Timestamp dateFinish) {
		this.id = id;
		this.idRoom = idRoom;
		this.dateStart = dateStart;
		this.dateFinish = dateFinish;
	}

	/**
	 * Constructor con valores
	 */
	public OccupancyDate() {
		this.id = 0;
		this.idRoom = 0;
		this.dateStart = new Timestamp(0);
		this.dateFinish =  new Timestamp(0);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIdRoom() {
		return idRoom;
	}

	public void setIdRoom(long idRoom) {
		this.idRoom = idRoom;
	}

	public Timestamp getDateStart() {
		return dateStart;
	}

	public void setDateStart(Timestamp dateStart) {
		this.dateStart = dateStart;
	}

	public Timestamp getDateFinish() {
		return dateFinish;
	}

	public void setDateFinish(Timestamp dateFinish) {
		this.dateFinish = dateFinish;
	}

	@Override
	public String toString() {
		return "OccupancyDate [dateFinish=" + dateFinish + ", dateStart=" + dateStart + ", id=" + id + ", idRoom="
				+ idRoom + "]";
	}

	
}
