package healthy_gram.controllers;

import healthy_gram.DatabaseModel;

public class PointOfSaleController {

    private static DatabaseModel dbm = new DatabaseModel();
	private double totalWeight;
    private double totalPrice;

	public Object[][] getItems() {
    	return dbm.table("item").select().getAll();
    }
    
    public double getTotalWeight() {
		return totalWeight;
	}
	public void setTotalWeight(double totalWeight) {
		this.totalWeight = totalWeight;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
}