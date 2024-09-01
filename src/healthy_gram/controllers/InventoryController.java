package healthy_gram.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import healthy_gram.DatabaseModel;

public class InventoryController {

    private static DatabaseModel dbm = new DatabaseModel();

    public static boolean isNotExpired(LocalDateTime expDate) {
        LocalDateTime currDate = LocalDateTime.now();     
        return currDate.isBefore(expDate);
    }
    
    public List<Map<String, Object>> fetchItems() {
        List<Map<String, Object>> items = new ArrayList<>();
        String[] columnNames = {"Item_id", "Item_name", "Description", "Price", "Weight", "Expiration_date"};
        Object[][] data = dbm.table("item").select().getAll();

        // Assuming `data` is structured as rows of columns
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME; // Adjust if necessary

        for (Object[] row : data) {
            Map<String, Object> item = new HashMap<>();
            boolean addItem = true; // Flag to determine if item should be added

            for (int j = 0; j < columnNames.length; j++) {
                Object value = row[j];
//                System.out.println(columnNames[j]);
//                System.out.println(value);
                
                if (columnNames[j].equals("Expiration_date")) {
                    if (value instanceof String) {
                        try {
                            LocalDateTime expDate = LocalDateTime.parse((String) value, formatter);
                            if (!isNotExpired(expDate)) {
                                addItem = false; // Item is expired, do not add
                                break; // Exit the inner loop
                            }
                        } catch (Exception e) {
                            e.printStackTrace(); // Handle parsing exception
                        }
                    } else if (value instanceof LocalDateTime) {
                        if (!isNotExpired((LocalDateTime) value)) {
                            addItem = false; // Item is expired, do not add
                            break; // Exit the inner loop
                        }
                    }
                }
                item.put(columnNames[j], value);
            }
            System.out.println();
            if (addItem) items.add(item);
        }
        return items;
    }
}
