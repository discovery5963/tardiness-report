package com.example.tardiness_report.dao;

import java.sql.DriverManager;
import java.sql.SQLException;
import org.springframework.stereotype.Repository;


@Repository
public class DbConnect {
    public boolean dbCheck() {
            String url = "jdbc:postgresql://160.16.197.189:5432/postgres";
            String user = "postgres";
            String conectionPassword = "lg82o7o9";
            System.out.println("接続開始");
            try {
                DriverManager.getConnection(url, user, conectionPassword);
                System.out.println("接続成功！");
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
}
