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
 * Clase para modelar el concepto USER del negocio de HotelAndes
 *
 */
public class User implements VOUser
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El número de identificación ÚNICO del USER
	 */
	private long document;

    /**
	 * La contraseña del USER
	 */
	private String password;
	
    /**
	 * El tipo de documento del USER
	 */
	private String documentType;
	
    /**
	 * El nombre del USER
	 */
	private String name;

    /**
	 * El email del USER
	 */
	private String email;


	/**
	 * La fecha de nacimiento del User
	 */
	private Timestamp bornDate;

    /**
	 * El rol del user ('CLIENT', 'RECEPTIONIST', 'EMPLOYEE', 'DATAADMIN', 'HOTELMANAGER')
	 */
	private String role; 

	

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Constructor por defecto
	 */
	public User() {
        this.document = 0;
        this.password = "";
        this.documentType = "";
        this.name = "";
        this.bornDate = new Timestamp (0);
        this.role = "CLIENT";
        this.email = "";
    }

	/**
	 * Constructor con valores
	 */

	public User(long document, String password, String documentType, String name, Timestamp bornDate, String role, String email) {
        this.document = document;
        this.password = password;
        this.documentType = documentType;
        this.name = name;
        this.bornDate = bornDate;
        this.role = role;
        this.email = email;
    }

    public long getDocument() {
        return document;
    }

    public void setDocument(long document) {
        this.document = document;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getBornDate() {
        return bornDate;
    }

    public void setBornDate(Timestamp bornDate) {
        this.bornDate = bornDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User [bornDate=" + bornDate + ", document=" + document + ", documentType=" + documentType + ", name="
                + name + ", password=" + password + ", role=" + role + "]";
    }

	
}
