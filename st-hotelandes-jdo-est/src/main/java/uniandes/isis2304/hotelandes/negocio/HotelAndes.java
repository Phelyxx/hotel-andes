/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad	de	los	Andes	(Bogotá	- Colombia)
 * Departamento	de	Ingeniería	de	Sistemas	y	Computación
 * Licenciado	bajo	el	esquema	Academic Free License versión 2.1
 * 		
 * Curso: isis2304 - Sistemas Transaccionales
 * Proyecto: HotelAndes
 * @version 1.0
 */

package uniandes.isis2304.hotelandes.negocio;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import com.google.gson.JsonObject;
import uniandes.isis2304.hotelandes.persistencia.PersistenciaHotelAndes;

/**
 * Clase principal del negocio
 * Sarisface todos los requerimientos funcionales del negocio
 *
 */
public class HotelAndes {
	/*
	 * ****************************************************************
	 * Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(HotelAndes.class.getName());

	/*
	 * ****************************************************************
	 * Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia
	 */
	private PersistenciaHotelAndes pp;

	/*
	 * ****************************************************************
	 * Métodos
	 *****************************************************************/
	/**
	 * El constructor por defecto
	 */
	public HotelAndes() {
		pp = PersistenciaHotelAndes.getInstance();
	}

	/**
	 * El constructor qye recibe los nombres de las tablas en tableConfig
	 * 
	 * @param tableConfig - Objeto Json con los nombres de las tablas y de la unidad
	 *                    de persistencia
	 */
	public HotelAndes(JsonObject tableConfig) {
		pp = PersistenciaHotelAndes.getInstance(tableConfig);
	}

	/**
	 * Cierra la conexión con la base de datos (Unidad de persistencia)
	 */
	public void cerrarUnidadPersistencia() {
		pp.cerrarUnidadPersistencia();
	}

	/*
	 * ****************************************************************
	 * Métodos para manejar los BOOKING
	 *****************************************************************/

	public VOBooking registrarBooking(long idDocumentUser, long idRoom, int numberOfAdults,
			int numberOfChildren, String type, double basePrice,
			int minRequiredNights, double accommodationPrice, double accomodationDiscount, String paymentPlan,
			double totalValue, Timestamp paidDate, boolean paid, Timestamp fechaEntrada, Timestamp fechaSalida) {
		log.info("Registrando booking: " + idRoom);
		System.out.println(idDocumentUser);
		Booking booking = pp.registrarBooking(idDocumentUser, idRoom, numberOfAdults, numberOfChildren, type, basePrice,
				minRequiredNights, accommodationPrice, accomodationDiscount, paymentPlan, totalValue, paidDate, paid, fechaEntrada, fechaSalida);
		log.info("Booking realizado: " + booking);
		return booking;
	}

	public long checkIn(long idBooking) {
		log.info("Registrando check in: " + idBooking);
		long cambios = pp.checkIn(idBooking);
		return cambios;
	}

	public long checkOut(long idBooking) {
		log.info("Registrando check out: " + idBooking);
		long checkInOut = pp.checkOut(idBooking);
		log.info("Check out realizado: " + checkInOut);
		return checkInOut;
	}

	/*
	 * ****************************************************************
	 * Métodos para manejar los SERVICES
	 *****************************************************************/
	/**
	 * Adiciona de manera persistente un consumo
	 * Adiciona entradas al log de la aplicación
	 * 
	 * @param idService - El identificador de Service
	 * @param value     - El valor del consumo
	 * @return El objeto ServiceInvoice adicionado. null si ocurre alguna Excepción
	 */

	public VOServiceInvoice adicionarConsumo(Long idService, Long idBooking, Double value, Boolean cuenta) {
		log.info("Adicionando consumo: " + idService);
		ServiceInvoice serviceInvoice = pp.adicionarConsumo(idService, idBooking, value, cuenta);
		log.info("Adicionando consumo: " + serviceInvoice);
		return serviceInvoice;
	}

	public VOReservation adicionarReserva(long idService, long documentUser, int numberofGuests, int duration,
			Timestamp date) {
		log.info("Adicionando reserva: " + documentUser);
		Reservation reservation = pp.adicionarReserva(idService, documentUser, numberofGuests, duration, date);
		log.info("Adicionando reserva: " + documentUser);
		return reservation;
	}

	/**
	 * Encuentra todos los 20 servicios más populares
	 * 
	 */
	public List<Object[]>  darServiciosPopulares(Timestamp fechaInicio, Timestamp fechaFin) {
		log.info("Consultando Servicios Populares");
		List<Object[]> services = pp.darServiciosPopulares(fechaInicio, fechaFin );
		log.info("Consultando Servicios Populares: " + services.size() + " existentes");
		return services;
	}

	/**
	 * Encuentra el índice de ocupación de cada una de las habitaciones del hotel
	 * En un período de tiempo dado.
	 */
	public List<Object[]> darIndiceOcupacion(Timestamp fechaInicio, Timestamp fechaFin) {
		log.info("Consultando Indice Ocupacion");
		List<Object[]>indices = pp.darIndiceOcupacion(fechaInicio, fechaFin);
		log.info("Consultando Indice Ocupacion: " + indices.size() + " existentes");
		return indices;
	}

	public List<Object[]> darConsumoUsuario(Timestamp fechaInicio, Timestamp fechaFin, long documentUser) {
		log.info("Consultando Consumo Usuario");
		List<Object[]> indices = pp.darConsumoUsuario(fechaInicio, fechaFin, documentUser);
		log.info("Consultando Consumo Usuario: " + indices.size() + " existentes");
		return indices;
	}

	
	public List<List<VOService>> darServiciosCaracteristica(String tipo, String categoria, long document) {
		log.info("Consultando Servicios Caracteristica");
		List<List<VOService>> services = pp.darServiciosCaracteristica(tipo, categoria, document);
		log.info("Consultando Consumo Usuario: " + services.size() + " existentes");
		return services;
	}

	public Boolean consultarDisponibilidad(long idRoom, Timestamp fechaEntrada, Timestamp fechaSalida) {
		log.info("Consultando disponibilidad");
		Boolean res = pp.consultarDisponibilidad(idRoom, fechaEntrada, fechaSalida);
		return res;
	}

	public List<Object[]> consultarDineroHabitaciones(Timestamp fechaInicio, Timestamp fechaFin) {
		log.info("Consultando dinero");
		List<Object[]> res = pp.consultarDineroHabitaciones(fechaInicio, fechaFin);
		return res;
	}

	/**
	* Encuentra si la clave y documento del usuario existen
	* Retorna un boolean
	*/
    public String iniciarSesion(long document, String password) {
		log.info ("Iniciando sesión");
		String role = pp.iniciarSesion(document, password);	
		log.info ("Iniciando sesión: " + role + " existentes");
        return role;
    }

	/*
	 * ****************************************************************
	 * Métodos para administración
	 *****************************************************************/

	/**
	 * Elimina todas las tuplas de todas las tablas de la base de datos de
	 * HotelAndes
	 */
	public long[] limpiarHotelAndes() {
		log.info("Limpiando la BD de HotelAndes");
		long[] borrrados = pp.limpiarHotelAndes();
		log.info("Limpiando la BD de HotelAndes: Listo!");
		return borrrados;
	}







}
