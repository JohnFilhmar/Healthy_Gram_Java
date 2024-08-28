package healthy_gram;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class Dbm {
    private String url = "jdbc:mysql://localhost:3306/healthy_gram";
    private String username = "root";
    private String password = "";
    private Connection con;

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            System.err.println("Failed to Connect");
            e.printStackTrace();
        }
    }

    // Modified getQuery method to accept parameters
    public Object[][] getQuery(String query, Object[] parameters) {
        ResultSet rs = null;
        List<Object[]> dataList = new ArrayList<>();
        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            for (int i = 0; i < parameters.length; i++) {
                pstmt.setObject(i + 1, parameters[i]);
            }
            rs = pstmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                dataList.add(row);
            }
        } catch (Exception e) {
            System.err.println("Failed to execute and process the query");
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Object[][] dataArray = new Object[dataList.size()][];
        for (int i = 0; i < dataList.size(); i++) {
            dataArray[i] = dataList.get(i);
        }
        return dataArray;
    }

    public int insertQuery(String query, Object[] payload) {
        int rowsAffected = 0;
        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            for (int i = 0; i < payload.length; i++) {
                pstmt.setObject(i + 1, payload[i]);
            }
            rowsAffected = pstmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("Failed to execute insert query");
            e.printStackTrace();
        }
        return rowsAffected;
    }
    
    public void close() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (Exception e) {
            System.err.println("Failed to close connection");
            e.printStackTrace();
        }
    }
}
