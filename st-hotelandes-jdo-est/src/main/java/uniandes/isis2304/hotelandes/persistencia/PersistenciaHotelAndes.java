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

package uniandes.isis2304.hotelandes.persistencia;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.apache.log4j.Logger;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import uniandes.isis2304.hotelandes.negocio.Booking;
import uniandes.isis2304.hotelandes.negocio.CheckInOut;
import uniandes.isis2304.hotelandes.negocio.OccupancyDate;
import uniandes.isis2304.hotelandes.negocio.Reservation;
import uniandes.isis2304.hotelandes.negocio.Service;
import uniandes.isis2304.hotelandes.negocio.ServiceInvoice;
import uniandes.isis2304.hotelandes.negocio.VOService;

/**
 * Clase para el manejador de persistencia del proyecto HotelAndes
 * Traduce la información entre objetos Java y tuplas de la base de datos, en
 * ambos sentidos
 * Sigue un patrón SINGLETON (Sólo puede haber UN objeto de esta clase) para
 * comunicarse de manera correcta
 * con la base de datos
 */
public class PersistenciaHotelAndes {
	/*
	 * ****************************************************************
	 * Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(PersistenciaHotelAndes.class.getName());

	/**
	 * Cadena para indicar el tipo de sentencias que se va a utilizar en una
	 * consulta
	 */
	public final static String SQL = "javax.jdo.query.SQL";

	/*
	 * ****************************************************************
	 * Atributos
	 *****************************************************************/
	/**
	 * Atributo privado que es el único objeto de la clase - Patrón SINGLETON
	 */
	private static PersistenciaHotelAndes instance;

	/**
	 * Fábrica de Manejadores de persistencia, para el manejo correcto de las
	 * transacciones
	 */
	private PersistenceManagerFactory pmf;

	/**
	 * Arreglo de cadenas con los nombres de las tablas de la base de datos, en su
	 * orden
	 */
	private List<String> tablas;

	/**
	 * Atributo para el acceso a las sentencias SQL propias a PersistenciaHotelAndes
	 */
	private SQLUtil sqlUtil;

	/**
	 * Atributo para el acceso a la tabla BOOKING de la base de datos
	 */
	private SQLBooking sqlBooking;

	/**
	 * Atributo para el acceso a la tabla CHECKINOUT de la base de datos
	 */
	private SQLCheckInOut sqlCheckInOut;

	/**
	 * Atributo para el acceso a la tabla SERVICEINVOICE de la base de datos
	 */
	private SQLServiceInvoice sqlServiceInvoice;

	/**
	 * Atributo para el acceso a la tabla RESERVATION de la base de datos
	 */

	private SQLRoom sqlRoom;

	private SQLReservation sqlReservation;

	private SQLService sqlService;

	private SQLUser sqlUser;

	private SQLOccupancyDate sqlOccupancyDate;


	/*
	 * ****************************************************************
	 * Métodos del MANEJADOR DE PERSISTENCIA
	 *****************************************************************/

	/**
	 * Constructor privado con valores por defecto - Patrón SINGLETON
	 */
	private PersistenciaHotelAndes() {
		pmf = JDOHelper.getPersistenceManagerFactory("HotelAndes");
		crearClasesSQL();

		// Define los nombres por defecto de las tablas de la base de datos
		tablas = new LinkedList<String>();
		tablas.add("A_HotelAndes_sequence");
		tablas.add("A_AMENITY");
		tablas.add("A_OCCUPIEDDATE");
		tablas.add("A_BOOKING");
		tablas.add("A_CHECKINOUT");
		tablas.add("A_FOODPRODUCT");
		tablas.add("A_HOTEL");
		tablas.add("A_INCLUDEDFOOD");
		tablas.add("A_CONSUMEDSERVICES");
		tablas.add("A_OCCUPANCYDATE");
		tablas.add("A_OFFERS");
		tablas.add("A_RESERVATION");
		tablas.add("A_RESTAURANT");
		tablas.add("A_ROOM");
		tablas.add("A_ROOMTYPE");
		tablas.add("A_SERVICE");
		tablas.add("A_SERVICEINVOICE");
		tablas.add("A_USER");

	}

	/**
	 * Constructor privado, que recibe los nombres de las tablas en un objeto Json -
	 * Patrón SINGLETON
	 * 
	 * @param tableConfig - Objeto Json que contiene los nombres de las tablas y de
	 *                    la unidad de persistencia a manejar
	 */
	private PersistenciaHotelAndes(JsonObject tableConfig) {
		crearClasesSQL();
		tablas = leerNombresTablas(tableConfig);

		String unidadPersistencia = tableConfig.get("unidadPersistencia").getAsString();
		log.trace("Accediendo unidad de persistencia: " + unidadPersistencia);
		pmf = JDOHelper.getPersistenceManagerFactory(unidadPersistencia);
	}

	/**
	 * @return Retorna el único objeto PersistenciaHotelAndes existente - Patrón
	 *         SINGLETON
	 */
	public static PersistenciaHotelAndes getInstance() {
		if (instance == null) {
			instance = new PersistenciaHotelAndes();
		}
		return instance;
	}

	/**
	 * Constructor que toma los nombres de las tablas de la base de datos del objeto
	 * tableConfig
	 * 
	 * @param tableConfig - El objeto JSON con los nombres de las tablas
	 * @return Retorna el único objeto PersistenciaHotelAndes existente - Patrón
	 *         SINGLETON
	 */
	public static PersistenciaHotelAndes getInstance(JsonObject tableConfig) {
		if (instance == null) {
			instance = new PersistenciaHotelAndes(tableConfig);
		}
		return instance;
	}

	/**
	 * Cierra la conexión con la base de datos
	 */
	public void cerrarUnidadPersistencia() {
		pmf.close();
		instance = null;
	}

	/**
	 * Genera una lista con los nombres de las tablas de la base de datos
	 * 
	 * @param tableConfig - El objeto Json con los nombres de las tablas
	 * @return La lista con los nombres del secuenciador y de las tablas
	 */
	private List<String> leerNombresTablas(JsonObject tableConfig) {
		JsonArray nombres = tableConfig.getAsJsonArray("tablas");

		List<String> resp = new LinkedList<String>();
		for (JsonElement nom : nombres) {
			resp.add(nom.getAsString());
		}

		return resp;
	}

	/**
	 * Crea los atributos de clases de apoyo SQL
	 */
	private void crearClasesSQL() {
		sqlServiceInvoice = new SQLServiceInvoice(this);
		sqlRoom = new SQLRoom(this);
		sqlUtil = new SQLUtil(this);
		sqlUser = new SQLUser(this);
		sqlBooking = new SQLBooking(this);
		sqlOccupancyDate = new SQLOccupancyDate(this);
		sqlReservation = new SQLReservation(this);
		sqlCheckInOut = new SQLCheckInOut(this);
		sqlService = new SQLService(this);
	}

	/**
	 * @return La cadena de caracteres con el nombre del secuenciador de HotelAndes
	 */
	public String darSeqHotelAndes() {
		return tablas.get(0);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de TipoBebida de
	 *         parranderos
	 */
	public String darTablaTipoBebida() {
		return tablas.get(1);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Bebida de
	 *         parranderos
	 */
	public String darTablaBebida() {
		return tablas.get(2);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Bar de
	 *         parranderos
	 */
	public String darTablaBar() {
		return tablas.get(3);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Bebedor de
	 *         parranderos
	 */
	public String darTablaBebedor() {
		return tablas.get(4);
	}

	public String darTablaBooking() {
		return tablas.get(3);
	}

	public String darTablaCheckInOut() {
		return tablas.get(4);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Gustan de
	 *         parranderos
	 */
	public String darTablaGustan() {
		return tablas.get(5);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Sirven de
	 *         parranderos
	 */
	public String darTablaSirven() {
		return tablas.get(6);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Visitan de
	 *         parranderos
	 */
	public String darTablaVisitan() {
		return tablas.get(7);
	}

	/**
	 * @return La cadena de caracteres con el nombre de la tabla de ServiceInvoice
	 *         de HotelAndes
	 */
	public String darTablaServiceInvoice() {
		return tablas.get(16);
	}

	public String darTablaConsumedServices() {
		return tablas.get(8);
	}


	public String darTablaReservation() {
		return tablas.get(11);
	}

	public String darTablaService() {
		return tablas.get(15);
	}

	public String darTablaUsuario() {
		return tablas.get(17);
	}

	public String darTablaOccupancyDate() {
		return tablas.get(9);
	}
	/**
	 * Transacción para el generador de secuencia de HotelAndes
	 * Adiciona entradas al log de la aplicación
	 * 
	 * @return El siguiente número del secuenciador de HotelAndes
	 */
	private long nextval() {
		long resp = sqlUtil.nextval(pmf.getPersistenceManager());
		log.trace("Generando secuencia: " + resp);
		return resp;
	}

	/**
	 * Extrae el mensaje de la exception JDODataStoreException embebido en la
	 * Exception e, que da el detalle específico del problema encontrado
	 * 
	 * @param e - La excepción que ocurrio
	 * @return El mensaje de la excepción JDO
	 */
	private String darDetalleException(Exception e) {
		String resp = "";
		if (e.getClass().getName().equals("javax.jdo.JDODataStoreException")) {
			JDODataStoreException je = (javax.jdo.JDODataStoreException) e;
			return je.getNestedExceptions()[0].getMessage();
		}
		return resp;
	}

	/*
	 * ****************************************************************
	 * Métodos para manejar los BOOKING
	 *****************************************************************/

	public Booking registrarBooking(long idDocumentUser, long idRoom, int numberOfAdults, int numberOfChildren,
			String type, double basePrice,
			int minRequiredNights, double accommodationPrice, double accomodationDiscount, String paymentPlan,
			double totalValue, Date paidDate, boolean paid, Timestamp fechaEntrada, Timestamp fechaSalida) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();

		try {
			tx.begin();
			long id = nextval();
			System.out.println(idDocumentUser);
			long tuplasInsertadas = sqlBooking.registrarBooking(pm, id, idDocumentUser, idRoom, numberOfAdults,
					numberOfChildren, type, basePrice, minRequiredNights, accommodationPrice, accomodationDiscount,
					paymentPlan, totalValue, paidDate, paid, fechaEntrada, fechaSalida);
			tx.commit();

			log.trace("Reserva de alojamiento realizda: " + id + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Booking(id, idRoom, numberOfAdults, numberOfChildren, type, basePrice, minRequiredNights,
					accommodationPrice, accomodationDiscount, paymentPlan, totalValue, paidDate, paid);
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}


	 /**
	 * Método que hace check in, de manera transaccional
	 * @param idBooking - El booking a modificar
	 * @return El número de tuplas modificadas. -1 si ocurre alguna Excepción
	 */
	public long checkIn(long idBooking) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			Timestamp today = new Timestamp(System.currentTimeMillis());
			String todayStr = today.toString();
			long tuplasModificadas = sqlCheckInOut.checkIn(pm, idBooking, todayStr);
			tx.commit();
			log.trace("Check in realizado: " + idBooking + ": " + tuplasModificadas + " tuplas modificadas");
			return tuplasModificadas;
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	public long checkOut(long idBooking) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			Timestamp today = new Timestamp(System.currentTimeMillis());
			String todayStr = today.toString();
			long tuplasModificadas = sqlCheckInOut.checkOut(pm, idBooking, todayStr);
			tx.commit();
			log.trace("Check out realizado: " + idBooking + ": " + tuplasModificadas + " tuplas modificadas");

			return tuplasModificadas;
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/*
	 * ****************************************************************
	 * Métodos para manejar los SERVICEINVOICE
	 *****************************************************************/

	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla
	 * SERVICEINVOICE
	 * Adiciona entradas al log de la aplicación
	 * 
	 * @param idService - El identificador de Service
	 * @param value     - El valor del consumo
	 * @return El objeto SERVICEINVOICE adicionado. null si ocurre alguna Excepción
	 */
	public ServiceInvoice adicionarConsumo(long idService, long idBooking, Double value, Boolean cuenta) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long id = nextval();
			long id2 = nextval();
			Timestamp createdDate = new Timestamp(System.currentTimeMillis());
			long tuplasInsertadas = sqlServiceInvoice.adicionarConsumo(pm, id, id2, idService, idBooking, value, createdDate, cuenta);
			tx.commit();

			log.trace(
					"Inserción de consumo del servicio: " + idService + ": " + tuplasInsertadas + " tuplas insertadas");

			return new ServiceInvoice(id, idService, value, createdDate, null);
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	public Reservation adicionarReserva(long idService, long documentUser, int numberofGuests, int duration,
			Timestamp date) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long id = nextval();
			long tuplasInsertadas = sqlReservation.adicionarReserva(pm, id, idService, documentUser, numberofGuests,
					duration, date);
			tx.commit();

			log.trace("Inserción de reserva: " + documentUser + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Reservation(id, idService, documentUser, numberofGuests, duration, date);
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 * Método que consulta los 20 servicios más populares
	 * 
	 * @return La lista de objetos Service, construidos con base en las tuplas de la
	 *         tabla Service
	 */
	public List<Object[]> darServiciosPopulares(Timestamp fechaInicio, Timestamp fechaFin) {
		return sqlService.darServiciosPopulares(pmf.getPersistenceManager(), fechaInicio, fechaFin);
	}

	public List<Object[]> darIndiceOcupacion(Timestamp fechaInicio, Timestamp fechaFin) {
		return sqlRoom.darIndiceOcupacion(pmf.getPersistenceManager(), fechaInicio, fechaFin);
	}

	public List<Object[]> consultarDineroHabitaciones(Timestamp fechaInicio, Timestamp fechaFin) {
		return sqlRoom.consultarDineroHabitaciones(pmf.getPersistenceManager(), fechaInicio, fechaFin);
	}

	public Boolean consultarDisponibilidad(long idRoom, Timestamp fechaEntrada, Timestamp fechaSalida) {
        return sqlOccupancyDate.consultarDisponibilidad(pmf.getPersistenceManager(), idRoom, fechaEntrada, fechaSalida);
    }

	public List<Object[]> darConsumoUsuario(Timestamp fechaInicio, Timestamp fechaFin, long documentUser) {
		return sqlUser.darConsumoUsuario(pmf.getPersistenceManager(), fechaInicio, fechaFin, documentUser);
	}

	
	public List<List<VOService>> darServiciosCaracteristica(String tipo, String categoria, long document) {
		List<List<Service>>  res = sqlService.darServiciosCaracteristica(pmf.getPersistenceManager(), tipo, categoria, document);
		List<List<VOService>> voRes =  new ArrayList<List<VOService>>();	
		for(List<Service> s : res){
			List<VOService> a = new ArrayList<>();
			for(Service s2 : s){
				VOService s3 = (VOService) s2;
				a.add(s3);
			}
			voRes.add(a);
		}
		return voRes;
	}


	/**
	 * Método que inicia sesión
	 * 
	 * @return La lista de objetos Service, construidos con base en las tuplas de la
	 *         tabla Service
	 */
	public String iniciarSesion(long document, String password) {
		return sqlUser.iniciarSesion(pmf.getPersistenceManager(), document, password);
	}

	/**
	 * Elimina todas las tuplas de todas las tablas de la base de datos de
	 * HotelAndes
	 * Crea y ejecuta las sentencias SQL para cada tabla de la base de datos - EL
	 * ORDEN ES IMPORTANTE
	 * 
	 * @return Un arreglo con 7 números que indican el número de tuplas borradas en
	 *         las tablas GUSTAN, SIRVEN, VISITAN, BEBIDA,
	 *         TIPOBEBIDA, BEBEDOR y BAR, respectivamente
	 */
	public long[] limpiarHotelAndes() {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			long[] resp = sqlUtil.limpiarParranderos(pm);
			tx.commit();
			log.info("Borrada la base de datos");
			return resp;
		} catch (Exception e) {
			// e.printStackTrace();
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return new long[] { -1, -1, -1, -1, -1, -1, -1 };
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}

	}





}
