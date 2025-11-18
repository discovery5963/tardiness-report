package com.example.tardiness_report.service;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import com.example.tardiness_report.dao.DbConnect;
import com.example.tardiness_report.dto.UserDataDto;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import java.sql.Statement;

@Service
public class LoginService {

    private DbConnect dbConnect;

    public LoginService(DbConnect dbConnect){
        this.dbConnect = dbConnect;
    }

    @Data // Lombokでgetterやsetterを自動生成
    public class Neko {
        private String empId;
        private String password;
        private int age;
    }

    private String ERRORMESSAGE = "errorMessage";

    private String ERROR = "ユーザIDとパスワードが一致しません";
    private String USER_ID_ERROR = "ユーザIDが空です";
    private String PASSWORD_ERROR = "パスワードが空です";

    public boolean getLoginMethod(Map<String, String> loginFormat, HttpSession session,
            Model model) {

        //db接続の確認用に作ったもの
        boolean result = true; 
        // result = dbConnect.dbCheck();
        List<String> users = new ArrayList<>();
        users = loginAccess();

        // userDataをDBから取得してきたあたいとして一旦作成(この後削除する記述)
        UserDataDto userData = new UserDataDto();
        userData.setPassword("pass");

        // 画面から取得してきた社員IDとパスワードを格納
        String empID = loginFormat.get("empID");
        String password = loginFormat.get("password");

        // nullや空のチェック
        if (empID == null || empID.isEmpty()) {
            model.addAttribute(ERRORMESSAGE, USER_ID_ERROR);
            return false;
        }
        if (password == null || password.isEmpty()) {
            model.addAttribute(ERRORMESSAGE, PASSWORD_ERROR);
            return false;
        }

        // 社員IDに一致するパスワードが存在するかチェック
        if (!userData.getPassword().equals(password)) {
            model.addAttribute(ERRORMESSAGE, ERROR);
            return false;
        }

        // DBから取得してきたユーザー情報をセッションに格納する
        String name = "dummy";
        session.setAttribute("empID", name);
        session.setAttribute("bushoId", name);
        session.setAttribute("teamId", name);
        session.setAttribute("position", name);
        session.setAttribute("lastName", name);
        session.setAttribute("firstName", name);
        session.setAttribute("affiliation", name);
        session.setAttribute("bushoName", name);
        session.setAttribute("teamName", name);

        return true;
    }

    //後々「DbConnect.java」に移行予定だが、競合などの理由でこちらに置いている。
        public List<String> loginAccess() {
             String url = "jdbc:postgresql://160.16.197.189:5432/postgres";
            String user = "postgres";
            String conectionPassword = "postgres";
            System.out.println("接続開始");
            List<String> loginList = new ArrayList<>();
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(url, user, conectionPassword);
                System.out.println("接続成功");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM team_mst");
                while (rs.next()) {
                    System.out.println(rs.getInt("id") + "：" + rs.getString("name"));
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
                return loginList;
            }finally{
                try{
                    if (conn != null){
                    conn.close();
                    }
                }catch (SQLException e){
                    // 例外処理
                }
            }
            return loginList;
        }


        //ほぼいらないがControllerクラスでまだ使っているので…
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




