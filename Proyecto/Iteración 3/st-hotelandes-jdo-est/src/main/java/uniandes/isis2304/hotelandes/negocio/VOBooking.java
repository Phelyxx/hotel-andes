package uniandes.isis2304.hotelandes.negocio;

import java.util.Date;

public interface VOBooking {

    public long getId();
	
	public long getIdRoom();
	
	public int getNumberOfAdults();
	
	public int getNumberOfChildren();
	
	public String getType();
	
	public double getBasePrice();
	
	public int getMinRequiredNights();
	
	public double getAccommodationPrice();
	
	public double getAccomodationDiscount();
	
	public String getPaymentPlan();
	
	public double getTotalValue();
	
	public Date getPaidDate();
	
	public boolean getPaid();
	
	@Override
	public String toString();
}
