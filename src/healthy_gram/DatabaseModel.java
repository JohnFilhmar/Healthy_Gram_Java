package healthy_gram;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class DatabaseModel {

    private static Dbm db = new Dbm();
    private String tableName;
    private StringBuilder query;
    private List<Object> parameters;

    public DatabaseModel() {
        parameters = new ArrayList<>();
    }

    // Set the table name
    public DatabaseModel table(String tableName) {
        this.tableName = tableName;
        return this;
    }
    
    // Execute a select query from a table
    public DatabaseModel select() {
        DatabaseModel model = new DatabaseModel();
        model.query = new StringBuilder("SELECT * FROM " + this.tableName);
        return model;
    }

    // Add a WHERE clause
    public DatabaseModel where(String column, Object value) {
        if (query == null) {
            query = new StringBuilder();
        }
        if (query.toString().contains("WHERE")) {
            query.append(" AND ").append(column).append(" = ? ");
        } else {
            query.append(" WHERE ").append(column).append(" = ? ");
        }
        parameters.add(value);
        return this;
    }

    // Add a LIKE clause for a string search
    public DatabaseModel contains(String column, String value) {
        if (query == null) {
            query = new StringBuilder();
        }
        if (query.toString().contains("WHERE")) {
            query.append(" AND LOWER(").append(column).append(") LIKE ?");
        } else {
            query.append(" WHERE LOWER(").append(column).append(") LIKE ?");
        }
        parameters.add(value.toLowerCase());
        return this;
    }

    // Add a NOT LIKE clause for a string search
    public DatabaseModel not_contains(String column, String value) {
        if (query == null) {
            query = new StringBuilder();
        }
        if (query.toString().contains("WHERE")) {
            query.append(" AND LOWER(").append(column).append(") NOT LIKE ?");
        } else {
            query.append(" WHERE LOWER(").append(column).append(") NOT LIKE ?");
        }
        parameters.add(value.toLowerCase());
        return this;
    }
    
    // Execute an insert query along with key:value pairs using HashMap
    public int insert(HashMap<String, Object> columnValues) {
        db.connect();
        try {
            StringBuilder columns = new StringBuilder();
            StringBuilder placeholders = new StringBuilder();
            Object[] values = new Object[columnValues.size()];
            int i = 0;
            for (Map.Entry<String, Object> entry : columnValues.entrySet()) {
                columns.append(entry.getKey());
                placeholders.append("?");
                values[i++] = entry.getValue();
                if (i < columnValues.size()) {
                    columns.append(", ");
                    placeholders.append(", ");
                }
            }
            String query = "INSERT INTO " + tableName + " (" + columns.toString() + ") VALUES (" + placeholders.toString() + ")";
            return db.insertQuery(query, values);
        } finally {
            db.close();
        }
    }

    // Execute a DELETE query
    public int delete() {
        db.connect();
        try {
            String finalQuery = "DELETE FROM " + this.tableName + query.toString();
            return db.insertQuery(finalQuery, parameters.toArray());
        } finally {
            db.close();
        }
    }

    // Insert a new record
    public int insert(Object[] values) {
        db.connect();
        try {
            // Construct the insert SQL statement
            StringBuilder placeholders = new StringBuilder();
            for (int i = 0; i < values.length; i++) {
                placeholders.append("?");
                if (i < values.length - 1) {
                    placeholders.append(", ");
                }
            }
            String query = "INSERT INTO " + tableName + " VALUES (" + placeholders.toString() + ")";
            return db.insertQuery(query, values);
        } finally {
            db.close();
        }
    }
    
    // Execute an update query using key:value pairs with HashMap
    public int update(HashMap<String, Object> columnValues) {
        db.connect();
        try {
            StringBuilder setClause = new StringBuilder();
            Object[] values = new Object[columnValues.size() + parameters.size()];
            int i = 0;
            for (Map.Entry<String, Object> entry : columnValues.entrySet()) {
                setClause.append(entry.getKey()).append(" = ?");
                values[i++] = entry.getValue();
                if (i < columnValues.size()) {
                    setClause.append(", ");
                }
            }
            for (int j = 0; j < parameters.size(); j++) {
                values[i++] = parameters.get(j);
            }
            String query = "UPDATE " + tableName + " SET " + setClause.toString() + this.query.toString();
            return db.insertQuery(query, values);
        } finally {
            db.close();
        }
    }

    // Fetch all records
    public Object[][] getAll() {
        db.connect();
        try {
            String finalQuery = query.toString();
            System.out.println(finalQuery);
            return db.getQuery(finalQuery, parameters.toArray());
        } finally {
            db.close();
        }
    }

    // Execute a raw SQL query
    public Object[][] raw(String query) {
        db.connect();
        try {
            return db.getQuery(query, new Object[]{});
        } finally {
            db.close();
        }
    }
}
