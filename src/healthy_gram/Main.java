package healthy_gram;

import java.sql.Timestamp;
import java.util.HashMap;

public class Main {
	
	public static void DoTry() {
		DatabaseModel dbm = new DatabaseModel();
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
	
	public static void main(String[] args) {
		DoTry();
	}
}
