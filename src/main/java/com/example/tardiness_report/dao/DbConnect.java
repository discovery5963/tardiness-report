package com.example.tardiness_report.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class DbConnect {
    public boolean dbCheck() {
            String url = "jdbc:postgresql://160.16.197.189:5432/postgres";
            String user = "postgres";
            String conectionPassword = "postgres";
            System.out.println("接続開始");
            try (Connection conn = DriverManager.getConnection(url, user, conectionPassword)) {
                System.out.println("接続成功！");
                // ResultSet rs = conn.getConnection("SELECT * FROM team_mst"); 
                // System.out.println(rs);  
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
}
