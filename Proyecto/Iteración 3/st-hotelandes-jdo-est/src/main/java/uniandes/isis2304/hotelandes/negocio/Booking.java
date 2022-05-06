package uniandes.isis2304.hotelandes.negocio;

import java.util.Date;

public class Booking implements VOBooking {

	public long id;
	
	public long idRoom;
	
	public int numberOfAdults;
	
	public int numberOfChildren;
	
	public String type;
	
	public double basePrice;
	
	public int minRequiredNights;
	
	public double accommodationPrice;
	
	public double accomodationDiscount;
	
	public String paymentPlan;
	
	public double totalValue;
	
	public Date paidDate;
	
	public boolean paid;
	
	
	public Booking(long id, long idRoom, int numberOfAdults, int numberOfChildren, String type, double basePrice,
			int minRequiredNights, double accommodationPrice, double accomodationDiscount, String paymentPlan,
			double totalValue, Date paidDate, boolean paid) {		
		this.id = id;
		this.idRoom = idRoom;
		this.numberOfAdults = numberOfAdults;
		this.numberOfChildren = numberOfChildren;
		this.type = type;
		this.basePrice = basePrice;
		this.minRequiredNights = minRequiredNights;
		this.accommodationPrice = accommodationPrice;
		this.accomodationDiscount = accomodationDiscount;
		this.paymentPlan = paymentPlan;
		this.totalValue = totalValue;
		this.paidDate = paidDate;
		this.paid = paid;
	}
	
	public long getId() {
		return id;
	}

	public long getIdRoom() {
		return idRoom;
	}

	public int getNumberOfAdults() {
		return numberOfAdults;
	}

	public int getNumberOfChildren() {
		return numberOfChildren;
	}

	public String getType() {
		return type;
	}

	public double getBasePrice() {
		return basePrice;
	}

	public int getMinRequiredNights() {
		return minRequiredNights;
	}

	public double getAccommodationPrice() {
		return accommodationPrice;
	}

	public double getAccomodationDiscount() {
		return accomodationDiscount;
	}

	public String getPaymentPlan() {
		return paymentPlan;
	}

	public double getTotalValue() {
		return totalValue;
	}

	public Date getPaidDate() {
		return paidDate;
	}

	public boolean getPaid() {
		return paid;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setIdRoom(int idRoom) {
		this.idRoom = idRoom;
	}

	public void setNumberOfAdults(int numberOfAdults) {
		this.numberOfAdults = numberOfAdults;
	}

	public void setNumberOfChildren(int numberOfChildren) {
		this.numberOfChildren = numberOfChildren;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}

	public void setMinRequiredNights(int minRequiredNights) {
		this.minRequiredNights = minRequiredNights;
	}

	public void setAccommodationPrice(double accommodationPrice) {
		this.accommodationPrice = accommodationPrice;
	}

	public void setAccomodationDiscount(double accomodationDiscount) {
		this.accomodationDiscount = accomodationDiscount;
	}

	public void setPaymentPlan(String paymentPlan) {
		this.paymentPlan = paymentPlan;
	}

	public void setTotalValue(double totalValue) {
		this.totalValue = totalValue;
	}

	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	@Override
	public String toString() {
		return "Booking [idRoom=" + idRoom + "numberOfAdults=" + numberOfAdults + ", numberOfChildren=" + numberOfChildren
				+ "paymentPlan=" + paymentPlan + ", type=" + type + "]";
	}

}
