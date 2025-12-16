package com.example.tardiness_report.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.tardiness_report.dao.DbConnect;

@Service
public class ReportService {
        private DbConnect dbConnect;

    public ReportService(DbConnect dbConnect){
        this.dbConnect = dbConnect;
    }

    /*
     * DB接続用定数
     */
    static final String URL = "jdbc:postgresql://160.16.197.189:5432/postgres";
    static final String USER = "postgres";
    static final String CONNECTIONPASSWORD = "postgres";
    // 現在日時を取得
    LocalDateTime now = LocalDateTime.now();
    // フォーマットを指定
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    // 文字列に変換
    String formattedDate = now.format(formatter);
    // SQL:当日日付の遅刻理由レコード取得用SQL
    String todayTardinessSql = "SELECT late_reason_id, late_reason_cd, register_content_cd, line_id, detail FROM \"TardinessReport\".late_reason";
    
    /*
     * 社員IDをキーに当日日付の遅刻理由レコードを取得
     * return true:取得成功
     *        false:取得失敗
     */
    public void getTodayTardinessRecord(Model model){
        Connection conn = null;
        StringBuilder setSql = new StringBuilder(todayTardinessSql);
        String empIdSql = ("emp_id =" + model.getAttribute("empId"));
        String resisterSql = ("AND register_date >=" + formattedDate);
        setSql.append(empIdSql);
        setSql.append(resisterSql);
        String sql = setSql.toString();
        try {
            System.out.println("接続開始");
            conn = DriverManager.getConnection(URL, USER, CONNECTIONPASSWORD);
            System.out.println("接続成功");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println(rs.getInt("late_reason_id"));
                System.out.println(rs.getInt("late_reason_cd"));
                System.out.println(rs.getInt("register_content_cd"));
                System.out.println(rs.getInt("line_id"));
                System.out.println(rs.getInt("detail"));
                model.addAttribute("lateReasonId", rs.getInt("late_reason_id"));
                model.addAttribute("lateReasonId", rs.getInt("late_reason_cd"));
                model.addAttribute("lateReasonId", rs.getInt("register_content_cd"));
                model.addAttribute("lateReasonId", rs.getInt("line_id"));
                model.addAttribute("lateReasonId", rs.getInt("detail"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                // 例外処理
            }
        }
    }
}
