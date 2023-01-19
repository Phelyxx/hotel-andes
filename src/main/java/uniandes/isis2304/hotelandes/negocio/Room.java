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
 * Clase para modelar el concepto ROOM del negocio de HotelAndes
 *
 */
public class Room implements VORoom
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El identificador ÚNICO del Room
	 */
	private long id;
	
	/**
	 * El identificador del Hotel de Room.
	 */
	private long idHotel;

	/**
	 * El identificador del RoomType de Room. 
	 */
	private long idRoomType;
	
	/**
	 * El número de la Room
	 */
	private String number;

	/**
	 * El tamaño de la Room
	 */
	private String size;
	
	

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Constructor por defecto
	 */

	public Room() {
		this.id = 0;
		this.idHotel = 0;
		this.idRoomType = 0;
		this.number = "";
		this.size = "";
	}

	/**
	 * Constructor con valores
	 */
	public Room(long id, long idHotel, long idRoomType, String number, String size) {
		this.id = id;
		this.idHotel = idHotel;
		this.idRoomType = idRoomType;
		this.number = number;
		this.size = size;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIdHotel() {
		return idHotel;
	}

	public void setIdHotel(long idHotel) {
		this.idHotel = idHotel;
	}

	public long getIdRoomType() {
		return idRoomType;
	}

	public void setIdRoomType(long idRoomType) {
		this.idRoomType = idRoomType;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "Room [id=" + id + ", idHotel=" + idHotel + ", idRoomType=" + idRoomType + ", number=" + number
				+ ", size=" + size + "]";
	}


}


	 

