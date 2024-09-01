package healthy_gram;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import healthy_gram.controllers.InventoryController;

public class Main {

	static DatabaseModel dbm = new DatabaseModel();
	
	public static void DoTry() {
		dbm.table("item");
		
		String dateString = "2024-08-28 14:30:00";
		Timestamp timestamp = Timestamp.valueOf(dateString);

	    HashMap<String, Object> insertValues = new HashMap<>();
		    insertValues.put("Item_name", "Almond Chocolate");
		    insertValues.put("Description", "asdf asdf asdf asfsad fasdf ");
		    insertValues.put("Price", 30.61);
		    insertValues.put("Weight", .7);
		    insertValues.put("Expiration_date", timestamp);
		    
		int rowsAffected = dbm.insert(insertValues);
		if(rowsAffected > 0) {
			System.out.println("Successfully added new data!");
		}
		

	    HashMap<String, Object> updateValues = new HashMap<>();
	    	updateValues.put("Item_name", "Updated NEw ITEMNAME");
		    updateValues.put("Description", "NEW DESCRIPTION ");
		    updateValues.put("Price", 50.55);
		    updateValues.put("Weight", 5.5);
		    
		int rowsUpdated = dbm.where("Item_id", "4").update(updateValues);
		if(rowsUpdated > 0) {
			System.out.println("Successfully updated!");
		}
		Object[][] updatedRow = dbm.select().where("Item_id", 4).getAll();
		for(Object[] row: updatedRow) {
		    System.out.println("\nID: " + row[0]);
		    System.out.println("Item Name: " + row[1]);
		    System.out.println("Description: " + row[2]);
		    System.out.println("Price: ₱" + row[3]);
		    System.out.println("Weight (kg): " + row[4] + "kg");
		    System.out.println("Exp. Date: " + row[5]);
		}
		
		Object[][] results = dbm.select().getAll();
		
		int affectedRows = dbm.where("item_id", 3).delete();
		if(affectedRows > 0) {
			System.out.println("Successfully deleted record/s: " + affectedRows);
		} else {
			System.out.println("No record found: [affected rows: " + affectedRows + "]");
		}
		
		System.out.println("Results:");
		for (Object[] row : results) {
		    System.out.println("\nID: " + row[0]);
		    System.out.println("Item Name: " + row[1]);
		    System.out.println("Description: " + row[2]);
		    System.out.println("Price: ₱" + row[3]);
		    System.out.println("Weight (kg): " + row[4] + "kg");
		    System.out.println("Exp. Date: " + row[5]);
		}
	}
	
	public static void getUsers() {
		Object[][] results = dbm.raw("SELECT `name` AS username, `passcode` AS password FROM `preparation` UNION ALL SELECT `name` AS username, `passcode` AS password FROM `cashier`");
		System.out.println("Users:");
		for (Object[] row : results) {
		    System.out.println("\nUsername: " + row[0]);
		    System.out.println("Password: " + row[1]);
		}
	}
	
	public static void tryItems() {
		InventoryController ic = new InventoryController();
        List<Map<String, Object>> items = ic.fetchItems();
        for(Map<String, Object> row : items) {
        	for(Map.Entry<String, Object> entry : row.entrySet()) {
        		System.out.println(entry.getKey() + " : " + entry.getValue());
        	}
        	System.out.println();
        }
	}
	
	public static void main(String[] args) {
//		Login login = new Login();
//		login.setVisible(true);
//		DoTry();
		tryItems();
	}
}
