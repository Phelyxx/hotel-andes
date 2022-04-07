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


/**
 * Interfaz para los métodos get de SERVICE.
 * Sirve para proteger la información del negocio de posibles manipulaciones desde la interfaz 
 * 
 */
public interface VOService
{	
    public long getId();
    
	public String getName();

	public String getDescription();

	public int getStartWorkHour();

	public int getEndWorkHour();

	public String getType();

	public String getCategory();

	public int getAvailable();

	public int getAvailableCapacity();

	public long getEmployeeThatRegister();

	@Override
	public String toString();

}
