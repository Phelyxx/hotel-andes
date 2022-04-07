package uniandes.isis2304.hotelandes.negocio;

import java.util.Date;

public interface VOCheckInOut {

	public long getIdBooking();
	
	public long getDocumentUser();
	
	public Date getCheckInDate();
	
	public boolean isCheckInMade();
	
	public Date getCheckOutDate();
	
	public boolean isCheckOutMade();
	
}
