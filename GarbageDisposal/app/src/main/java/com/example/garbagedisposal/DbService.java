package com.example.garbagedisposal;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbService {
    private final Connection con;

    public DbService() {
        con = createConnection();
    }

    public DbService(Connection con) {
        this.con = con;
    }

    public Connection createConnection() {
        String ip = "192.168.1.117", port = "1433", db = "GarbageDisposalDB", username = "sa", password = "Randeer9945";
        StrictMode.ThreadPolicy a = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(a);
        String connectURL = null;

        Connection con;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectURL = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";databasename=" + db + ";user=" + username + ";" + "password=" + password + ";";
            con = DriverManager.getConnection(connectURL);
 //           Log.i("sql connection", "Connected to SQL Server " + ip + ":" + port);
            return con;

        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//            Log.e("error in connection", e.getMessage());
        }
        return null;
    }


    public String getReUseMethodFromDb(String wasteType) {
        if (wasteType == null || wasteType.isEmpty()) {
//            Log.e("validation error", "Invalid waste type");
            return "";
        }
        String sql = "SELECT ReuseIdeas FROM WasteReuseIdea WHERE WasteType='" + wasteType + "';";
        String result = getStringFromDb(sql, 1);
        return result == null ? "" : result;

    }

    public String getStringFromDb(String sqlStatement, int colNumber) {
        String returnValue = "";
        if (con == null) {
//            Log.e("SQL connection error", "SQL connection error in connecting");
            return null;
        } else {
            try {
                Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlStatement);

                if (resultSet.next()) {
                    returnValue = resultSet.getString(colNumber);
                } else {
                    return null;
                }
            } catch (Exception e) {
//                Log.e("error in sql execution", e.getMessage());
                return null;

            }
//            Log.i("sql fetch value", returnValue);
            return returnValue;
        }
    }


    public void destroy() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
//            Log.e("Error in closing sql connection", e.getMessage());
        }
    }
}
