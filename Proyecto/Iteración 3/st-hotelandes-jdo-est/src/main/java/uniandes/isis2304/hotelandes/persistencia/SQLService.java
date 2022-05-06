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
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.hotelandes.negocio.Service;
import uniandes.isis2304.hotelandes.negocio.ServiceInvoice;
import uniandes.isis2304.hotelandes.negocio.VOService;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto Service de HotelAndes
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 */
class SQLService {
	/*
	 * ****************************************************************
	 * Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra acá para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaHotelAndes.SQL;

	/*
	 * ****************************************************************
	 * Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia general de la aplicación
	 */
	private PersistenciaHotelAndes pp;

	/*
	 * ****************************************************************
	 * Métodos
	 *****************************************************************/
	/**
	 * Constructor
	 * 
	 * @param pp
	 *        - El Manejador de persistencia de la aplicación
	 */
	public SQLService(PersistenciaHotelAndes pp) {
		this.pp = pp;
	}

	/**
	 * Crea y ejecuta la sentencia SQL para registrar un consumo de un servicio del hotel por parte de un cliente o sus acompañantes
	 * 
	 * @param pm
	 *        - El manejador de persistencia
	 * @param id
	 *        - El identificador de ServiceInvoice
	 * @param idService
	 *        - El identificador de Service
	 * @param value
	 *        - El valor del consumo
	 * @param createdDate
	 *        - La fecha de creación
	 * @return EL número de tuplas insertadas
	 */

	public long adicionarConsumo(PersistenceManager pm, long id, long idService, Double value, Timestamp createdDate) {
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaServiceInvoice()
				+ "(id, idService, value, createdDate) values (?, ?, ?, ?)");
		q.setParameters(id, idService, value, createdDate);
		return (long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de los servicios populares de la
	 * base de datos de Parranderos
	 * 
	 * @param pm
	 *        - El manejador de persistencia
	 * @return Una lista de objetos SERVICE
	 */
	public List<Object[]> darServiciosPopulares(PersistenceManager pm, Timestamp fechaInicio, Timestamp fechaFin) {
		String sql = "SELECT IDSERVICE, CONSUMO FROM( " +
				"SELECT IDSERVICE, CREATEDDATE, COUNT(*) CONSUMO " +
				"FROM A_SERVICEINVOICE " +
				"WHERE CREATEDDATE BETWEEN TO_TIMESTAMP('" + fechaInicio
				+ "', 'yyyy/mm/dd HH24:MI:SS.FF') AND TO_TIMESTAMP('" + fechaFin + "', 'yyyy/mm/dd HH24:MI:SS.FF') " +
				"GROUP BY IDSERVICE, CREATEDDATE) " +
				"WHERE ROWNUM <=20";
		Query q = pm.newQuery(SQL, sql);
		List<Object[]> results = (List<Object[]>) q.executeList();
		return results;
	}

	public List<List<Service>> darServiciosCaracteristica(PersistenceManager pm, String tipo,
			String categoria, long document) {
		List<List<Service>> services3 = new ArrayList<List<Service>>();		
		String sql = "SELECT * FROM A_SERVICE WHERE EMPLOYEETHATREGISTER = '"+document+"'";
		Query q = pm.newQuery(SQL, sql);
		q.setResultClass(Service.class);
		services3.add((List<Service>) q.executeList());

		String sql2 = "SELECT * FROM A_SERVICE WHERE \"TYPE\" = '"+tipo+"'";
		Query q2= pm.newQuery(SQL, sql2);
		q2.setResultClass(Service.class);
		services3.add((List<Service>) q2.executeList());

		String sql3 = "SELECT * FROM A_SERVICE WHERE \"CATEGORY\" = '"+categoria+"'";
		Query q3 = pm.newQuery(SQL, sql3);
		q3.setResultClass(Service.class);
		services3.add((List<Service>) q3.executeList());
		return services3;
	}

}
