package uniandes.isis2304.hotelandes.negocio;

import java.util.Date;

public class CheckInOut implements VOCheckInOut {

	public long idBooking;
	
	public long documentUser;
	
	public Date checkInDate;
	
	public boolean checkInMade;
	
	public Date checkOutDate;
	
	public boolean checkOutMade;
	
	
	public CheckInOut(long idBooking, long documentUser, Date checkInDate, boolean checkInMade, Date checkOutDate,
			boolean checkOutMade) {
		super();
		this.idBooking = idBooking;
		this.documentUser = documentUser;
		this.checkInDate = checkInDate;
		this.checkInMade = checkInMade;
		this.checkOutDate = checkOutDate;
		this.checkOutMade = checkOutMade;
	}


	public long getIdBooking() {
		return idBooking;
	}


	public long getDocumentUser() {
		return documentUser;
	}


	public Date getCheckInDate() {
		return checkInDate;
	}


	public boolean isCheckInMade() {
		return checkInMade;
	}


	public Date getCheckOutDate() {
		return checkOutDate;
	}


	public boolean isCheckOutMade() {
		return checkOutMade;
	}


	public void setIdBooking(long idBooking) {
		this.idBooking = idBooking;
	}


	public void setDocumentUser(long documentUser) {
		this.documentUser = documentUser;
	}


	public void setCheckInDate(Date checkInDate) {
		this.checkInDate = checkInDate;
	}


	public void setCheckInMade(boolean checkInMade) {
		this.checkInMade = checkInMade;
	}


	public void setCheckOutDate(Date checkOutDate) {
		this.checkOutDate = checkOutDate;
	}


	public void setCheckOutMade(boolean checkOutMade) {
		this.checkOutMade = checkOutMade;
	}

	@Override
	public String toString() {
		return "CheckInOut [checkInDate=" + checkInDate + ", checkInMade=" + checkInMade + ", checkOutDate="
				+ checkOutDate + ", checkOutMade=" + checkOutMade + ", documentUser=" + documentUser + ", idBooking="
				+ idBooking + "]";
	}
	
	
}
