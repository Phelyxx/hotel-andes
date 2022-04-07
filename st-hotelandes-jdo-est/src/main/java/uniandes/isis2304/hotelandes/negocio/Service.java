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
 * Clase para modelar el concepto SERVICE¿del negocio de HotelAndes
 *
 */
public class Service implements VOService
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El identificador ÚNICO del Service
	 */
	private long id;
	
	private String name;

	private String description;

	private int startWorkHour;

	private int endWorkHour;

	private String type;

	private String category;

	private int available;

	private int availableCapacity;

	private long employeeThatRegister;


	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Constructor por defecto
	 */
	public Service() {
		this.id = 0;
		this.name = "";
		this.description = "";
		this.startWorkHour = 0;
		this.endWorkHour = 0;
		this.type = "SPA";
		this.category = "RESERVABLE";
		this.available = 0;
		this.availableCapacity = 0;
		this.employeeThatRegister = 0;
	}
	/**
	 * Constructor con valores
	 */
	public Service(long id, String name, String description, int startWorkHour, int endWorkHour, String type,
			String category, int available, int availableCapacity, long employeeThatRegister) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.startWorkHour = startWorkHour;
		this.endWorkHour = endWorkHour;
		this.type = type;
		this.category = category;
		this.available = available;
		this.availableCapacity = availableCapacity;
		this.employeeThatRegister = employeeThatRegister;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getStartWorkHour() {
		return startWorkHour;
	}
	public void setStartWorkHour(int startWorkHour) {
		this.startWorkHour = startWorkHour;
	}
	public int getEndWorkHour() {
		return endWorkHour;
	}
	public void setEndWorkHour(int endWorkHour) {
		this.endWorkHour = endWorkHour;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getAvailable() {
		return available;
	}
	public void setAvailable(int available) {
		this.available = available;
	}
	public int getAvailableCapacity() {
		return availableCapacity;
	}
	public void setAvailableCapacity(int availableCapacity) {
		this.availableCapacity = availableCapacity;
	}
	public long getEmployeeThatRegister() {
		return employeeThatRegister;
	}
	public void setEmployeeThatRegister(long employeeThatRegister) {
		this.employeeThatRegister = employeeThatRegister;
	}
	@Override
	public String toString() {
		return "Service [available=" + available + ", availableCapacity=" + availableCapacity + ", category=" + category
				+ ", description=" + description + ", employeeThatRegister=" + employeeThatRegister + ", endWorkHour="
				+ endWorkHour + ", id=" + id + ", name=" + name + ", startWorkHour=" + startWorkHour + ", type=" + type
				+ "]";
	}

	
	
}
