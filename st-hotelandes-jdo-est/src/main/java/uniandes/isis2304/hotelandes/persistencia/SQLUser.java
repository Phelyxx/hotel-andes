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

import uniandes.isis2304.hotelandes.negocio.User;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el
 * concepto USER de HotelAndes
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 */
class SQLUser {
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
	public SQLUser(PersistenciaHotelAndes pp) {
		this.pp = pp;
	}

	/**
	 * Ejecuta la sentencia SQL para verificar el inicio sesión de un usuario
	 */

	public String iniciarSesion(PersistenceManager pm, long document, String password) {
		Query q = pm.newQuery(SQL, "SELECT * FROM A_USER WHERE document = "+ document);
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

    public List<Object[]> darConsumoUsuario(PersistenceManager pm, Timestamp fechaInicio,
            Timestamp fechaFin, long documentUser) {
		String sql =  "SELECT DOCUMENT, SUM(\"VALUE\") FROM ( SELECT B.DOCUMENT, D.VALUE FROM A_CONSUMEDSERVICES A, A_USER B, A_CHECKINOUT C, A_SERVICEINVOICE D "+
        "WHERE A.IDBOOKING = C.IDBOOKING AND B.DOCUMENT = "+documentUser+" AND A.IDSERVICEINVOICE = D.ID " +
        "AND D.PAIDDATE BETWEEN TO_TIMESTAMP('"+fechaInicio+"', 'yyyy/mm/dd HH24:MI:SS.FF') AND " +
        "TO_TIMESTAMP('"+fechaFin+"', 'yyyy/mm/dd HH24:MI:SS.FF')) " +
		"GROUP BY DOCUMENT"; 
		Query q = pm.newQuery(SQL, sql);
		List<Object[]> results = (List<Object[]>) q.executeList();
		return results;
	}
}

