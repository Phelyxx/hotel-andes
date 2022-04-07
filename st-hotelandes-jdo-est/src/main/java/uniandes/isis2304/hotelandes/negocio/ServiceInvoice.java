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
public class ServiceInvoice implements VOServiceInvoice
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El identificador ÚNICO del ServiceInvoice
	 */
	private long id;
	
	/**
	 * El identificador del Service de ServiceInvoice. Debe existir en la tabla SERVICE.
	 */
	private long idService;
	
	/**
	 * El valor de ServiceInvoice
	 */
	private Double value;
	
	/**
	 * La fecha de creación de ServiceInvoice
	 */
	private Timestamp createdDate;
	
	
	/**
	 * La fecha de pago de ServiceInvoice
	 */
	private Timestamp paidDate;

	

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Constructor por defecto
	 */
	public ServiceInvoice() 
	{
		this.id = 0;
		this.idService = 0;
		this.value = (double) 0;
		this.createdDate = new Timestamp (0);
		this.paidDate = new Timestamp (0);
	}

	/**
	 * Constructor con valores
	 */

	public ServiceInvoice(long id, long idService, Double value, Timestamp createdDate, Timestamp paidDate) {
		super();
		this.id = id;
		this.idService = idService;
		this.value = value;
		this.createdDate = createdDate;
		this.paidDate = paidDate;
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

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(Timestamp paidDate) {
		this.paidDate = paidDate;
	}

	@Override
	public String toString() {
		return "ServiceInvoice [id=" + id + ", idService=" + idService + ", value=" + value + ", createdDate="
				+ createdDate + ", paidDate=" + paidDate + "]";
	}
}
