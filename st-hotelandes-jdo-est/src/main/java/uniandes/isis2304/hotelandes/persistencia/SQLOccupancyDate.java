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

import uniandes.isis2304.hotelandes.negocio.OccupancyDate;
import uniandes.isis2304.hotelandes.negocio.User;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el
 * concepto OccupancyDate de HotelAndes
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 */
class SQLOccupancyDate {
	/*
	 * ****************************************************************
	 * Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las
	 * sentencias de acceso a la base de datos
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
	 * @param pp - El Manejador de persistencia de la aplicación
	 */
	public SQLOccupancyDate(PersistenciaHotelAndes pp) {
		this.pp = pp;
	}

	/**
	 * Ejecuta la sentencia SQL para verificar el inicio sesión de un usuario
	 */

	public String iniciarSesion(PersistenceManager pm, long document, String password) {
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaUsuario() + " WHERE document = "+ document);
		q.setResultClass(User.class);
		User user = (User) q.executeUnique();
		String res = "";
		if (!user.getPassword().equals(password)) {
			res = "La contraseña no es correcta";
		} else {
			res = user.getRole();
		}
		return res;
	}

	public Boolean consultarDisponibilidad(PersistenceManager pm, long idRoom, Timestamp fechaEntrada,
			Timestamp fechaSalida) {
		String sql = "SELECT * FROM " + pp.darTablaOccupancyDate() + " WHERE idRoom = "+ idRoom;
		sql += " AND DATESTART BETWEEN TO_TIMESTAMP('"+fechaEntrada+"', 'yyyy/mm/dd HH24:MI:SS.FF') AND TO_TIMESTAMP('"+fechaSalida+"', 'yyyy/mm/dd HH24:MI:SS.FF')";	
		sql += " OR DATEFINISH BETWEEN TO_TIMESTAMP('"+fechaEntrada+"', 'yyyy/mm/dd HH24:MI:SS.FF') AND TO_TIMESTAMP('"+fechaSalida+"', 'yyyy/mm/dd HH24:MI:SS.FF')";
		Query q = pm.newQuery(SQL, sql);
		q.setResultClass(OccupancyDate.class);
		List<OccupancyDate> occupancies = (List<OccupancyDate>) q.executeList();
		Boolean res = true;
		if (occupancies.size() > 0){
			res = false;
		}
		return res;
	}
}
