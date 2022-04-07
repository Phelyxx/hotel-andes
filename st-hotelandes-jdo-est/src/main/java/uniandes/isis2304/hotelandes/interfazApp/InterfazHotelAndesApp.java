/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad	de	los	Andes	(Bogotá	- Colombia)
 * Departamento	de	Ingeniería	de	Sistemas	y	Computación
 * Licenciado	bajo	el	esquema	Academic Free License versión 2.1
 * 		
 * Curso: isis2304 - Sistemas Transaccionales
 * Proyecto: HotelAndes
 * @version 1.0
 */

package uniandes.isis2304.hotelandes.interfazApp;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import uniandes.isis2304.hotelandes.negocio.HotelAndes;
import uniandes.isis2304.hotelandes.negocio.VOBooking;
import uniandes.isis2304.hotelandes.negocio.VOCheckInOut;
import uniandes.isis2304.hotelandes.negocio.VOOccupancyDate;
import uniandes.isis2304.hotelandes.negocio.VOReservation;
import uniandes.isis2304.hotelandes.negocio.VOService;
import uniandes.isis2304.hotelandes.negocio.VOServiceInvoice;

@SuppressWarnings("serial")

public class InterfazHotelAndesApp extends JFrame implements ActionListener {
	/*
	 * ****************************************************************
	 * Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(InterfazHotelAndesApp.class.getName());

	/**
	 * Ruta al archivo de configuración de la interfaz
	 */
	private static final String CONFIG_INTERFAZ = "./src/main/resources/config/interfaceConfigApp.json";

	/**
	 * Ruta al archivo de configuración de los nombres de tablas de la base de datos
	 */
	private static final String CONFIG_TABLAS = "./src/main/resources/config/TablasBD_A.json";

	/*
	 * ****************************************************************
	 * Atributos
	 *****************************************************************/
	/**
	 * Objeto JSON con los nombres de las tablas de la base de datos que se quieren
	 * utilizar
	 */
	private JsonObject tableConfig;

	/**
	 * Asociación a la clase principal del negocio.
	 */
	private HotelAndes hotelAndes;

	/*
	 * ****************************************************************
	 * Atributos de interfaz
	 *****************************************************************/
	/**
	 * Objeto JSON con la configuración de interfaz de la app.
	 */
	private JsonObject guiConfig;

	/**
	 * Panel de despliegue de interacción para los requerimientos
	 */
	private PanelDatos panelDatos;

	/**
	 * Menú de la aplicación
	 */
	private JMenuBar menuBar;

	/**
	 * Rol del usuario que inicia sesión
	 */
	private String role;

	/**
	 * Documento del que inicia sesión
	 */
	private long documentUser;

	/*
	 * ****************************************************************
	 * Métodos
	 *****************************************************************/
	/**
	 * Construye la ventana principal de la aplicación. <br>
	 * <b>post:</b> Todos los componentes de la interfaz fueron inicializados.
	 */
	public InterfazHotelAndesApp() {
		// Carga la configuración de la interfaz desde un archivo JSON
		guiConfig = openConfig("Interfaz", CONFIG_INTERFAZ);

		// Configura la apariencia del frame que contiene la interfaz gráfica
		configurarFrame();
		if (guiConfig != null) {
			crearMenu(guiConfig.getAsJsonArray("menuBar"));
		}

		tableConfig = openConfig("Tablas BD", CONFIG_TABLAS);
		hotelAndes = new HotelAndes(tableConfig);

		String path = guiConfig.get("bannerPath").getAsString();
		panelDatos = new PanelDatos();

		setLayout(new BorderLayout());
		add(new JLabel(new ImageIcon(path)), BorderLayout.NORTH);
		add(panelDatos, BorderLayout.CENTER);
	}

	/*
	 * ****************************************************************
	 * Métodos de configuración de la interfaz
	 *****************************************************************/
	/**
	 * Lee datos de configuración para la aplicació, a partir de un archivo JSON o
	 * con valores por defecto si hay errores.
	 * 
	 * @param tipo
	 *        - El tipo de configuración deseada
	 * @param archConfig
	 *        - Archivo Json que contiene la configuración
	 * @return Un objeto JSON con la configuración del tipo especificado
	 *         NULL si hay un error en el archivo.
	 */
	private JsonObject openConfig(String tipo, String archConfig) {
		JsonObject config = null;
		try {
			Gson gson = new Gson();
			FileReader file = new FileReader(archConfig);
			JsonReader reader = new JsonReader(file);
			config = gson.fromJson(reader, JsonObject.class);
			log.info("Se encontró un archivo de configuración válido: " + tipo);
		} catch (Exception e) {
			// e.printStackTrace ();
			log.info("NO se encontró un archivo de configuración válido");
			JOptionPane.showMessageDialog(null,
					"No se encontró un archivo de configuración de interfaz válido: " + tipo, "HotelAndes App",
					JOptionPane.ERROR_MESSAGE);
		}
		return config;
	}

	/**
	 * Método para configurar el frame principal de la aplicación
	 */
	private void configurarFrame() {
		int alto = 0;
		int ancho = 0;
		String titulo = "";

		if (guiConfig == null) {
			log.info("Se aplica configuración por defecto");
			titulo = "HotelAndes APP Default";
			alto = 300;
			ancho = 500;
		} else {
			log.info("Se aplica configuración indicada en el archivo de configuración");
			titulo = guiConfig.get("title").getAsString();
			alto = guiConfig.get("frameH").getAsInt();
			ancho = guiConfig.get("frameW").getAsInt();
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(50, 50);
		setResizable(true);
		setBackground(Color.WHITE);

		setTitle(titulo);
		setSize(ancho, alto);
	}

	/**
	 * Método para crear el menú de la aplicación con base em el objeto JSON leído
	 * Genera una barra de menú y los menús con sus respectivas opciones
	 * 
	 * @param jsonMenu
	 *        - Arreglo Json con los menùs deseados
	 */
	private void crearMenu(JsonArray jsonMenu) {
		// Creación de la barra de menús
		menuBar = new JMenuBar();
		for (JsonElement men : jsonMenu) {
			// Creación de cada uno de los menús
			JsonObject jom = men.getAsJsonObject();

			String menuTitle = jom.get("menuTitle").getAsString();
			JsonArray opciones = jom.getAsJsonArray("options");

			JMenu menu = new JMenu(menuTitle);

			for (JsonElement op : opciones) {
				// Creación de cada una de las opciones del menú
				JsonObject jo = op.getAsJsonObject();
				String lb = jo.get("label").getAsString();
				String event = jo.get("event").getAsString();

				JMenuItem mItem = new JMenuItem(lb);
				mItem.addActionListener(this);
				mItem.setActionCommand(event);

				menu.add(mItem);
			}
			menuBar.add(menu);
		}
		setJMenuBar(menuBar);
	}

	public void iniciarSesion() {
		try {
			String documentStr = JOptionPane.showInputDialog(this, "Número de documento", "Iniciar sesión",
					JOptionPane.QUESTION_MESSAGE);
			String password = JOptionPane.showInputDialog(this, "Contraseña", "Iniciar sesión",
					JOptionPane.QUESTION_MESSAGE);
			if (documentStr != null && password != null) {
				long document = Long.valueOf(documentStr);
				String role = hotelAndes.iniciarSesion(document, password);
				if (role == "La contraseña no es correcta") {
					panelDatos.actualizarInterfaz(role);
				} else {
					this.setRole(role);
					this.setDocumentUser(document);
					String resultado = "Rol de usuario: " + role;
					resultado += "\nSesión iniciada exitosamente: " + documentStr;
					resultado += "\nOperación terminada";
					panelDatos.actualizarInterfaz(resultado);
				}

			} else {
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	public void cerrarSesion() {
		panelDatos.actualizarInterfaz(
				"Sesión cerrada satisfactoriamente \nUsuario " + this.documentUser + "\nRol " + this.role);
		this.setRole(null);
		this.setDocumentUser(0);
	}

	public Boolean verificarRol(String role) {
		return (role.equals(this.role) || this.role.equals("HOTELMANAGER"));
	}

	/*
	 * ****************************************************************
	 * Requerimientos servicios
	 *****************************************************************/
	/**
	 * Registrar un consumo de un servicio del hotel por parte de un cliente o sus
	 * acompañantes. Esta operación es
	 * realizada por un empleado del hotel.
	 * Se crea una nueva tupla de SERVICEINVOICE en la base de datos, si el
	 * IDSERVICE existe
	 */
	public void adicionarConsumo() {
		try {
			if (verificarRol("EMPLOYEE") == false) {
				throw new Exception("No tiene permisos de empleado");
			}
			String idServiceStr = JOptionPane.showInputDialog(this, "ID del servicio?", "Registrar consumo",
					JOptionPane.QUESTION_MESSAGE);
			String idBookingStr = JOptionPane.showInputDialog(this, "ID de la reserva del cliente?",
					"Registrar consumo",
					JOptionPane.QUESTION_MESSAGE);
			String valueStr = JOptionPane.showInputDialog(this, "Valor del servicio?", "Registrar consumo",
					JOptionPane.QUESTION_MESSAGE);
			String cuentaStr = JOptionPane.showInputDialog(this, "Añadir a la cuenta de la habitación? (Si/No)",
					"Registrar consumo",
					JOptionPane.QUESTION_MESSAGE);
			if (idServiceStr != null && valueStr != null && idBookingStr != null && cuentaStr != null) {
				Boolean cuenta = false;
				if (cuentaStr.equals("Si")) {
					cuenta = true;
				}
				long idService = Long.valueOf(idServiceStr);
				Double value = Double.valueOf(valueStr);
				long idBooking = Long.valueOf(idBookingStr);
				VOServiceInvoice tb = hotelAndes.adicionarConsumo(idService, idBooking, value, cuenta);
				if (tb == null) {
					throw new Exception("No se pudo crear un SERVICEINVOICE con servicio: " + idService);
				}
				String resultado = "En adicionarConsumo\n\n";
				resultado += "SERVICEINVOICE adicionado exitosamente: " + tb;
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			} else {
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	/**
	 * Reserva la prestación de un servicio por parte de un cliente, siempre y
	 * cuando haya disponibilidad. Esta
	 * operación es realizada por un cliente.
	 */

	public void adicionarReserva() {
		try {
			if (verificarRol("CLIENT") == false) {
				throw new Exception("No tiene permisos de empleado");
			}
			String idServiceStr = JOptionPane.showInputDialog(this, "ID del servicio?", "Reservar servicio",
					JOptionPane.QUESTION_MESSAGE);
			String numberOfGuestsStr = JOptionPane.showInputDialog(this, "Número de invitados?", "Reservar servicio",
					JOptionPane.QUESTION_MESSAGE);
			String durationStr = JOptionPane.showInputDialog(this, "Duración?", "Reservar servicio",
					JOptionPane.QUESTION_MESSAGE);
			String dateStr = JOptionPane.showInputDialog(this, "Fecha en formato 2022-12-12 01:23:45?",
					"Reservar servicio", JOptionPane.QUESTION_MESSAGE);
			if (idServiceStr != null && numberOfGuestsStr != null && durationStr != null
					&& dateStr != null) {
				long idService = Long.valueOf(idServiceStr);
				int numberofGuests = Integer.valueOf(numberOfGuestsStr);
				int duration = Integer.valueOf(durationStr);
				Timestamp date = Timestamp.valueOf(dateStr);

				VOReservation tb = hotelAndes.adicionarReserva(idService, this.documentUser, numberofGuests, duration,
						date);
				if (tb == null) {
					throw new Exception("No se pudo crear un RESERVATION con número de documento: " + this.documentUser
							+ "\nVerifique la disponibilidad");
				}
				String resultado = "En adicionarReserva\n\n";
				resultado += "RESERVATION adicionado exitosamente: " + tb;
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			} else {
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	public void consultarDineroHabitaciones() {
		try {
			String fechaInicioStr = JOptionPane.showInputDialog(this, "Fecha de inicio? 2022-05-04", "Indice por fecha",
					JOptionPane.QUESTION_MESSAGE);
			String fechaFinStr = JOptionPane.showInputDialog(this, "Fecha fin? 2022-06-04", "Indice por fecha",
					JOptionPane.QUESTION_MESSAGE);
			if (fechaInicioStr != null && fechaFinStr != null) {
				Timestamp fechaInicio = Timestamp.valueOf(fechaInicioStr + " 01:01:00");
				Timestamp fechaFin = Timestamp.valueOf(fechaFinStr + " 01:01:00");
				List<Object[]> habitaciones = hotelAndes.consultarDineroHabitaciones(fechaInicio, fechaFin);
				String resultado = "En consultarDineroHabitaciones \n";
				for (Object[] h : habitaciones) {
					resultado += "Habitacion: " + (BigDecimal) h[0] + "\n";
					resultado += "Dinero recolectado: " + (BigDecimal) h[1] + "\n";
				}
				resultado += "\n En el ultimo año\n";
				LocalDate now = LocalDate.now();
				LocalDate firstDay = now.with(firstDayOfYear());
				LocalDate lastDay = now.with(lastDayOfYear());
				Timestamp anioInicio = Timestamp.valueOf(firstDay.atTime(LocalTime.MIDNIGHT));
				Timestamp anioFin = Timestamp.valueOf(lastDay.atTime(LocalTime.MIDNIGHT));
				List<Object[]> habitaciones2 = hotelAndes.consultarDineroHabitaciones(anioInicio, anioFin);
				for (Object[] h : habitaciones2) {
					resultado += "Habitacion: " + (BigDecimal) h[0] + "\n";
					resultado += "Dinero recolectado: " + (BigDecimal) h[1] + "\n";
				}
				panelDatos.actualizarInterfaz(resultado);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	/**
	 * Consulta en la base de datos los 20 servicios más populares. Los que más fueron consumidos en un período de tiempo dado.
	 */
	public void darServiciosPopulares() {
		try {
			String fechaInicioStr = JOptionPane.showInputDialog(this, "Fecha de inicio? 2022-05-04",
					"Servicios populares",
					JOptionPane.QUESTION_MESSAGE);
			String fechaFinStr = JOptionPane.showInputDialog(this, "Fecha fin? 2022-06-04", "Servicios populares",
					JOptionPane.QUESTION_MESSAGE);
			if (fechaInicioStr != null && fechaFinStr != null) {
				Timestamp fechaInicio = Timestamp.valueOf(fechaInicioStr + " 01:01:00");
				Timestamp fechaFin = Timestamp.valueOf(fechaFinStr + " 01:01:00");
				List<Object[]> servicios = hotelAndes.darServiciosPopulares(fechaInicio, fechaFin);
				String resultado = "En darServiciosPopulares \n";
				for (Object[] h : servicios) {
					resultado += "Servicio: " + (BigDecimal) h[0] + "\n";
					resultado += "Total consumo: " + (BigDecimal) h[1] + "\n";
				}
				resultado += "\n En el ultimo año\n";
				LocalDate now = LocalDate.now();
				LocalDate firstDay = now.with(firstDayOfYear());
				LocalDate lastDay = now.with(lastDayOfYear());
				Timestamp anioInicio = Timestamp.valueOf(firstDay.atTime(LocalTime.MIDNIGHT));
				Timestamp anioFin = Timestamp.valueOf(lastDay.atTime(LocalTime.MIDNIGHT));
				List<Object[]> servicios2 = hotelAndes.darServiciosPopulares(anioInicio, anioFin);
				for (Object[] h : servicios2) {
					resultado += "Servicio: " + (BigDecimal) h[0] + "\n";
					resultado += "Total consumo: " + (BigDecimal) h[1] + "\n";
				}
				panelDatos.actualizarInterfaz(resultado);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	private String listarServicios(List<VOService> lista) {
		String resp = "Los servicios existentes son:\n";
		int i = 1;
		for (VOService se : lista) {
			resp += i++ + ". " + se.toString() + "\n";
		}
		return resp;
	}

	/**
	 * Consulta en la base de datos el índice de ocupación de cada una de las
	 * habitaciones del hotel
	 */
	public void darIndiceOcupacion() {
		try {
			String fechaInicioStr = JOptionPane.showInputDialog(this, "Fecha de inicio? 2022-05-04", "Indice ocupacion",
					JOptionPane.QUESTION_MESSAGE);
			String fechaFinStr = JOptionPane.showInputDialog(this, "Fecha fin? 2022-06-04", "Indice ocupacion",
					JOptionPane.QUESTION_MESSAGE);
			if (fechaInicioStr != null && fechaFinStr != null) {
				Timestamp fechaInicio = Timestamp.valueOf(fechaInicioStr + " 01:01:00");
				Timestamp fechaFin = Timestamp.valueOf(fechaFinStr + " 01:01:00");
				List<Object[]> habitaciones = hotelAndes.darIndiceOcupacion(fechaInicio, fechaFin);
				String resultado = "En consultarDineroHabitaciones \n";
				for (Object[] h : habitaciones) {
					resultado += "Habitacion: " + (BigDecimal) h[0] + "\n";
					resultado += "Porcentaje ocupacion " + (BigDecimal) h[1] + "%" + "\n";
				}
				resultado += "\n En el ultimo año\n";
				LocalDate now = LocalDate.now();
				LocalDate firstDay = now.with(firstDayOfYear());
				LocalDate lastDay = now.with(lastDayOfYear());
				Timestamp anioInicio = Timestamp.valueOf(firstDay.atTime(LocalTime.MIDNIGHT));
				Timestamp anioFin = Timestamp.valueOf(lastDay.atTime(LocalTime.MIDNIGHT));
				List<Object[]> habitaciones2 = hotelAndes.darIndiceOcupacion(anioInicio, anioFin);
				for (Object[] h : habitaciones2) {
					resultado += "Habitacion: " + (BigDecimal) h[0] + "\n";
					resultado += "Porcentaje ocupacion: " + (BigDecimal) h[1] + "%" + "\n";
				}
				panelDatos.actualizarInterfaz(resultado);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	/**
	 * Dar consumo usuario dado en rango de fechas
	 */
	public void darConsumoUsuario() {
		try {
			String fechaInicioStr = JOptionPane.showInputDialog(this, "Fecha de inicio? 2022-05-04", "Consumo usuario",
					JOptionPane.QUESTION_MESSAGE);
			String fechaFinStr = JOptionPane.showInputDialog(this, "Fecha fin? 2022-06-04", "Consumo usuario",
					JOptionPane.QUESTION_MESSAGE);
			String documentStr = JOptionPane.showInputDialog(this, "Número de documento", "Consumo usuario",
					JOptionPane.QUESTION_MESSAGE);
			if (fechaInicioStr != null && fechaFinStr != null && documentStr != null) {
				long document = Long.valueOf(documentStr);
				Timestamp fechaInicio = Timestamp.valueOf(fechaInicioStr + " 01:01:00");
				Timestamp fechaFin = Timestamp.valueOf(fechaFinStr + " 01:01:00");
				List<Object[]> consumo = hotelAndes.darConsumoUsuario(fechaInicio, fechaFin, document);
				String resultado = "En darConsumoUsuario \n";
				for (Object[] h : consumo) {
					resultado += "Usuario: " + (BigDecimal) h[0] + "\n";
					resultado += "Consumo: " + (BigDecimal) h[1] + "\n";
				}
				resultado += "\n En el ultimo año\n";
				LocalDate now = LocalDate.now();
				LocalDate firstDay = now.with(firstDayOfYear());
				LocalDate lastDay = now.with(lastDayOfYear());
				Timestamp anioInicio = Timestamp.valueOf(firstDay.atTime(LocalTime.MIDNIGHT));
				Timestamp anioFin = Timestamp.valueOf(lastDay.atTime(LocalTime.MIDNIGHT));
				List<Object[]> consumo2 = hotelAndes.darConsumoUsuario(anioInicio, anioFin, document);
				for (Object[] h : consumo2) {
					resultado += "Usuario: " + (BigDecimal) h[0] + "\n";
					resultado += "Consumo: " + (BigDecimal) h[1] + "\n";
				}
				panelDatos.actualizarInterfaz(resultado);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	/**
	 * Mostrar los servicios que cumplen con cierta característica
	 */
	public void darServiciosCaracteristica() {
		try {
			String tipo = JOptionPane.showInputDialog(this, "Tipo de servicio", "Dar servicios caracteristicas",
					JOptionPane.QUESTION_MESSAGE);
			String categoria = JOptionPane.showInputDialog(this, "Categoria de servicio",
					"Dar servicios caracteristicas",
					JOptionPane.QUESTION_MESSAGE);
			String documentStr = JOptionPane.showInputDialog(this, "Registrados por", "Dar servicios caracteristicsa",
					JOptionPane.QUESTION_MESSAGE);
			if (tipo != null && categoria != null && documentStr != null) {
				long document = Long.valueOf(documentStr);
				List<List<VOService>> servicios = hotelAndes.darServiciosCaracteristica(tipo, categoria, document);
				String resultado = "En darServiciosCaracteristica \n\n";
				int i = 0;
				for (List<VOService> vs : servicios) {
					if (i == 0) {
						resultado += "Registrados por " + document + "\n\n";
					} else if (i == 1) {
						resultado += "Tipo " + tipo + "\n\n";
					} else {
						resultado += "Categoria " + categoria + "\n\n";
					}
					for (VOService s : vs) {
						resultado += s.toString() + "\n";
					}
					i += 1;
				}
				panelDatos.actualizarInterfaz(resultado);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	/*
	 * ****************************************************************
	 * Requerimientos Booking
	 *****************************************************************/
	/**
	 * Registrar una reserva de alojamiento
	 * Reserva una habitación por un período de tiempo, por parte de un cliente,
	 * siempre y cuando esté disponible.
	 * Esta operación es realizada por un cliente.
	 */
	public void registrarReserva() {
		try {
			if (verificarRol("CLIENT") == false) {
				throw new Exception("No tiene permisos de empleado");
			}
			String idRoomStr = JOptionPane.showInputDialog(this, "ID de la habitación?", "Registrar reserva",
					JOptionPane.QUESTION_MESSAGE);
			String fechaEntradaStr = JOptionPane.showInputDialog(this, "Fecha entrada? (2020-12-12)",
					"Registrar reserva",
					JOptionPane.QUESTION_MESSAGE);
			String fechaSalidaStr = JOptionPane.showInputDialog(this, "Fecha salida? (2020-12-12)", "Registrar reserva",
					JOptionPane.QUESTION_MESSAGE);
			if (idRoomStr != null && fechaEntradaStr != null && fechaSalidaStr != null) {
				long idRoom = Long.valueOf(idRoomStr);
				Timestamp fechaEntrada = Timestamp.valueOf(fechaEntradaStr + " 01:01:00");
				Timestamp fechaSalida = Timestamp.valueOf(fechaSalidaStr + " 01:01:00");
				Boolean tb = hotelAndes.consultarDisponibilidad(idRoom, fechaEntrada, fechaSalida);
				if (tb == false) {
					throw new Exception("No hay disponibilidad de la habitación en la fecha indicada");
				}
			}
			String numberOfChildrenStr = JOptionPane.showInputDialog(this, "Número de niños?", "Registrar reserva",
					JOptionPane.QUESTION_MESSAGE);
			String numberOfAdultsStr = JOptionPane.showInputDialog(this, "Número de adultos?", "Registrar reserva",
					JOptionPane.QUESTION_MESSAGE);
			String type = JOptionPane.showInputDialog(this,
					"Tipo de plan ('LONGSTAY', 'TIMESHARE', 'ALLINCLUSIVE', 'PROMOTION')?", "Registrar reserva",
					JOptionPane.QUESTION_MESSAGE);
			String paymentPlan = JOptionPane.showInputDialog(this, "Plan de pago?", "Registrar reserva",
					JOptionPane.QUESTION_MESSAGE);
			if (numberOfChildrenStr != null && numberOfAdultsStr != null && type != null && paymentPlan != null) {
				long idRoom = Long.valueOf(idRoomStr);
				int numberOfChildren = Integer.valueOf(numberOfChildrenStr);
				int numberOfAdults = Integer.valueOf(numberOfAdultsStr);
				Timestamp fechaEntrada = Timestamp.valueOf(fechaEntradaStr + " 01:01:00");
				Timestamp fechaSalida = Timestamp.valueOf(fechaSalidaStr + " 01:01:00");
				VOBooking tb = hotelAndes.registrarBooking(this.documentUser, idRoom, numberOfAdults, numberOfChildren,
						type, 0.0, 0, 0.0, 0.0, paymentPlan, 0.0, new Timestamp(0), false, fechaEntrada, fechaSalida);
				if (tb == null) {
					throw new Exception("No se pudo crear un BOOKING con habitacion: " + idRoom);
				}
				String resultado = "En adicionarConsumo\n\n";
				resultado += "BOOKING adicionado exitosamente: " + tb;
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			} else {
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	/**
	 * Registra la llegada de un cliente al hotel, correspondiente a una reserva ya registrada. Esta operación es
	 * realizada por un recepcionista del hote
	 */
	public void checkIn() {
		try {
			if (verificarRol("RECEPTIONIST") == false) {
				throw new Exception("No tiene permisos de recepcionista");
			}
			String idBookingStr = JOptionPane.showInputDialog(this, "ID del Booking?", "Registrar llegada",
					JOptionPane.QUESTION_MESSAGE);
			if (idBookingStr != null) {
				long idBooking = Long.valueOf(idBookingStr);
				long tb = hotelAndes.checkIn(idBooking);
				System.out.println(tb);
				if (tb != 1) {
					throw new Exception(
							"No se pudo hacer checkIn en el Booking: " + idBooking);
				}
				String resultado = "En Booking\n\n";
				resultado += "Booking modificado exitosamente: ";
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			} else {
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	public void checkOut() {
		try {
			if (verificarRol("RECEPTIONIST") == false) {
				throw new Exception("No tiene permisos de recepcionista");
			}
			String idBookingStr = JOptionPane.showInputDialog(this, "ID del Booking?", "Registrar salida",
					JOptionPane.QUESTION_MESSAGE);
			String pagaCuenta = JOptionPane.showInputDialog(this, "El cliente paga la cuenta? (Si/No)",
					"Registrar salida",
					JOptionPane.QUESTION_MESSAGE);
			if (idBookingStr != null && pagaCuenta.equals("Si")) {
				long idBooking = Long.valueOf(idBookingStr);
				long tb = hotelAndes.checkOut(idBooking);
				System.out.println(tb);
				if (tb != 1) {
					throw new Exception(
							"No se pudo hacer checkOut en el Booking: " + idBooking);
				}
				String resultado = "En Booking\n\n";
				resultado += "Booking modificado exitosamente: ";
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			} else {
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} catch (Exception e) {
			// e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	/**
	 * Asigna el rol del usuario al iniciar sesión
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * Asigna el número de documento del usuario al iniciar sesión
	 */
	public void setDocumentUser(long documentUser) {
		this.documentUser = documentUser;
	}

	/*
	 * ****************************************************************
	 * Métodos administrativos
	 *****************************************************************/
	/**
	 * Muestra el log de HotelAndes
	 */
	public void mostrarLogHotelAndes() {
		mostrarArchivo("hotelAndes.log");
	}

	/**
	 * Muestra el log de datanucleus
	 */
	public void mostrarLogDatanuecleus() {
		mostrarArchivo("datanucleus.log");
	}

	/**
	 * Limpia el contenido del log de hotelAndes
	 * Muestra en el panel de datos la traza de la ejecución
	 */
	public void limpiarLogHotelAndes() {
		// Ejecución de la operación y recolección de los resultados
		boolean resp = limpiarArchivo("hotelAndes.log");

		// Generación de la cadena de caracteres con la traza de la ejecución de la demo
		String resultado = "\n\n************ Limpiando el log de hotelAndes ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}

	/**
	 * Limpia el contenido del log de datanucleus
	 * Muestra en el panel de datos la traza de la ejecución
	 */
	public void limpiarLogDatanucleus() {
		// Ejecución de la operación y recolección de los resultados
		boolean resp = limpiarArchivo("datanucleus.log");

		// Generación de la cadena de caracteres con la traza de la ejecución de la demo
		String resultado = "\n\n************ Limpiando el log de datanucleus ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}

	/**
	 * Limpia todas las tuplas de todas las tablas de la base de datos de hotelAndes
	 * Muestra en el panel de datos el número de tuplas eliminadas de cada tabla
	 */
	public void limpiarBD() {
		try {
			// Ejecución de la demo y recolección de los resultados
			long eliminados[] = hotelAndes.limpiarHotelAndes();

			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
			String resultado = "\n\n************ Limpiando la base de datos ************ \n";
			resultado += eliminados[0] + " Gustan eliminados\n";
			resultado += eliminados[1] + " Sirven eliminados\n";
			resultado += eliminados[2] + " Visitan eliminados\n";
			resultado += eliminados[3] + " Bebidas eliminadas\n";
			resultado += eliminados[4] + " Tipos de bebida eliminados\n";
			resultado += eliminados[5] + " Bebedores eliminados\n";
			resultado += eliminados[6] + " Bares eliminados\n";
			resultado += "\nLimpieza terminada";

			panelDatos.actualizarInterfaz(resultado);
		} catch (Exception e) {
			// e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	/**
	 * Muestra la presentación general del proyecto
	 */
	public void mostrarPresentacionGeneral() {
		mostrarArchivo("data/00-ST-HotelAndesJDO.pdf");
	}

	/**
	 * Muestra el modelo conceptual de HotelAndes
	 */
	public void mostrarModeloConceptual() {
		mostrarArchivo("data/Modelo Conceptual HotelAndes.pdf");
	}

	/**
	 * Muestra el esquema de la base de datos de HotelAndes
	 */
	public void mostrarEsquemaBD() {
		mostrarArchivo("data/Esquema BD HotelAndes.pdf");
	}

	/**
	 * Muestra el script de creación de la base de datos
	 */
	public void mostrarScriptBD() {
		mostrarArchivo("data/EsquemaHotelAndes.sql");
	}

	/**
	 * Muestra la arquitectura de referencia para HotelAndes
	 */
	public void mostrarArqRef() {
		mostrarArchivo("data/ArquitecturaReferencia.pdf");
	}

	/**
	 * Muestra la documentación Javadoc del proyectp
	 */
	public void mostrarJavadoc() {
		mostrarArchivo("doc/index.html");
	}

	/**
	 * Muestra la información acerca del desarrollo de esta apicación
	 */
	public void acercaDe() {
		String resultado = "\n\n ************************************\n\n";
		resultado += " * Universidad	de	los	Andes	(Bogotá	- Colombia)\n";
		resultado += " * Departamento	de	Ingeniería	de	Sistemas	y	Computación\n";
		resultado += " * Licenciado	bajo	el	esquema	Academic Free License versión 2.1\n";
		resultado += " * \n";
		resultado += " * Curso: isis2304 - Sistemas Transaccionales\n";
		resultado += " * Proyecto: HotelAndes Uniandes\n";
		resultado += " * @version 1.0\n";
		resultado += " * @author Felix Rojas\n";

		panelDatos.actualizarInterfaz(resultado);
	}

	/*
	 * ****************************************************************
	 * Métodos privados para la presentación de resultados y otras operaciones
	 *****************************************************************/

	/**
	 * Genera una cadena de caracteres con la descripción de la excepcion e,
	 * haciendo énfasis en las excepcionsde JDO
	 * 
	 * @param e
	 *        - La excepción recibida
	 * @return La descripción de la excepción, cuando es
	 *         javax.jdo.JDODataStoreException, "" de lo contrario
	 */
	private String darDetalleException(Exception e) {
		String resp = "";
		if (e.getClass().getName().equals("javax.jdo.JDODataStoreException")) {
			JDODataStoreException je = (javax.jdo.JDODataStoreException) e;
			return je.getNestedExceptions()[0].getMessage();
		}
		return resp;
	}

	/**
	 * Genera una cadena para indicar al usuario que hubo un error en la aplicación
	 * 
	 * @param e
	 *        - La excepción generada
	 * @return La cadena con la información de la excepción y detalles adicionales
	 */
	private String generarMensajeError(Exception e) {
		String resultado = "************ Error en la ejecución\n";
		resultado += e.getLocalizedMessage() + ", " + darDetalleException(e);
		resultado += "\n\nRevise datanucleus.log y hotelAndes.log para más detalles";
		return resultado;
	}

	/**
	 * Limpia el contenido de un archivo dado su nombre
	 * 
	 * @param nombreArchivo
	 *        - El nombre del archivo que se quiere borrar
	 * @return true si se pudo limpiar
	 */
	private boolean limpiarArchivo(String nombreArchivo) {
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(new File(nombreArchivo)));
			bw.write("");
			bw.close();
			return true;
		} catch (IOException e) {
			// e.printStackTrace();
			return false;
		}
	}

	/**
	 * Abre el archivo dado como parámetro con la aplicación por defecto del sistema
	 * 
	 * @param nombreArchivo
	 *        - El nombre del archivo que se quiere mostrar
	 */
	private void mostrarArchivo(String nombreArchivo) {
		try {
			Desktop.getDesktop().open(new File(nombreArchivo));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * ****************************************************************
	 * Métodos de la Interacción
	 *****************************************************************/
	/**
	 * Método para la ejecución de los eventos que enlazan el menú con los métodos
	 * de negocio
	 * Invoca al método correspondiente según el evento recibido
	 * 
	 * @param pEvento
	 *        - El evento del usuario
	 */
	@Override
	public void actionPerformed(ActionEvent pEvento) {
		String evento = pEvento.getActionCommand();
		try {
			Method req = InterfazHotelAndesApp.class.getMethod(evento);
			req.invoke(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * ****************************************************************
	 * Programa principal
	 *****************************************************************/
	/**
	 * Este método ejecuta la aplicación, creando una nueva interfaz
	 * 
	 * @param args
	 *        Arreglo de argumentos que se recibe por línea de comandos
	 */
	public static void main(String[] args) {
		try {
			// Unifica la interfaz para Mac y para Windows.
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			InterfazHotelAndesApp interfaz = new InterfazHotelAndesApp();
			interfaz.setVisible(true);
			interfaz.iniciarSesion();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
