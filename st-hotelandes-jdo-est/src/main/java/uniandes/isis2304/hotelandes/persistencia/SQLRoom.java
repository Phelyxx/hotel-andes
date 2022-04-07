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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.hotelandes.negocio.ServiceInvoice;
import uniandes.isis2304.hotelandes.negocio.VOService;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto ROOM de Parranderos
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 */
class SQLRoom
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
	public SQLRoom (PersistenciaHotelAndes pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para registrar un consumo de un servicio del hotel por parte de un cliente o sus acompañantes
	 * @param pm - El manejador de persistencia
	 * @param idBebida - El identificador de la bebida
	 * @param nombre - El nombre de la bebida
	 * @param idTipoBebida - El identificador del tipo de bebida de la bebida
	 * @param gradoAlcohol - El grado de alcohol de la bebida (Mayor que 0)
	 * @return EL número de tuplas insertadas
	 * 
	 * 
	 */
	
	public long adicionarConsumo (PersistenceManager pm, long id, long idService, Double value, Timestamp createdDate) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaBebida () + "(id, idService, value, createdDate) values (?, ?, ?, ?)");
        q.setParameters(id, idService, value, createdDate);
        return (long) q.executeUnique();            
	}


	public List<Object[]> darIndiceOcupacion (PersistenceManager pm, Timestamp fechaInicio, Timestamp fechaFin) 
	{
		String sql =  "SELECT IDROOM, ROUND(((total*100)/dias),2) AS OCUPACION FROM ( ";
        sql += "SELECT IDROOM, COUNT(*) AS total, ROUND((EXTRACT(DAY FROM (TO_TIMESTAMP('"+fechaFin+"', 'yyyy/mm/dd HH24:MI:SS.FF')) - (TO_TIMESTAMP('"+fechaInicio+"', 'yyyy/mm/dd HH24:MI:SS.FF'))))) DIAS ";
        sql += "FROM (SELECT * FROM A_BOOKING ";
        sql += "WHERE PAIDDATE BETWEEN TO_TIMESTAMP('"+fechaInicio+"', 'yyyy/mm/dd HH24:MI:SS.FF') AND TO_TIMESTAMP('"+fechaFin+"', 'yyyy/mm/dd HH24:MI:SS.FF')), DUAL ";
        sql += "GROUP BY IDROOM) ";
		Query q = pm.newQuery(SQL, sql);
		List<Object[]> results = (List<Object[]>) q.executeList();
		return results;
	}

	public List<Object[]> consultarDineroHabitaciones(PersistenceManager pm, Timestamp fechaInicio, Timestamp fechaFin) {
		String sql =  "SELECT B.IDROOM, A.TOTAL  FROM ("
		+	" SELECT IDROOM, SUM(TOTALVALUE) TOTAL FROM A_BOOKING"
		+	" WHERE PAIDDATE BETWEEN TO_TIMESTAMP('"+fechaInicio+"', 'yyyy/mm/dd HH24:MI:SS.FF')"+" AND TO_TIMESTAMP('"+fechaFin+"', 'yyyy/mm/dd HH24:MI:SS.FF')"
		+	" GROUP BY IDROOM) A, A_BOOKING B"
		+ " WHERE A.IDROOM = B.IDROOM";
		Query q = pm.newQuery(SQL, sql);
		List<Object[]> results = (List<Object[]>) q.executeList();
		return results;
	}

}
