package com.example.tardiness_report.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import com.example.tardiness_report.dto.UserDataDto;
import com.example.tardiness_report.repository.EmployeeMstRepository;

import jakarta.servlet.http.HttpSession;
import lombok.Data;

@Service
public class LoginService {

    @Data // Lombokでgetterやsetterを自動生成
    public class Neko {
        private String empId;
        private String password;
        private int age;
    }

    @Autowired
    private EmployeeMstRepository employeeMstRepository;

    private String ERRORMESSAGE = "errorMessage";

    private String ERROR = "ユーザIDとパスワードが一致しません";
    private String USER_ID_ERROR = "ユーザIDが空です";
    private String PASSWORD_ERROR = "パスワードが空です";

    // TODO：ごちゃごちゃしてしまったので後でこのメソッドごと削除する
    public boolean getLoginMethod(Map<String, String> loginFormat, HttpSession session,
            Model model) {

        // userDataをDBから取得してきたあたいとして一旦作成(この後削除する記述)
        List<UserDataDto> userData = new ArrayList<UserDataDto>();
        // userData.setPassword("pass");
        // userData = fetchEmployees();

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
        // if (!userData.getPassword().equals(password)) {
        // model.addAttribute(ERRORMESSAGE, ERROR);
        // return false;
        // }

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

    // ユーザー情報の取得
    public List<UserDataDto> fetchEmployees(String empId) {
        return employeeMstRepository.getEmpData(empId);
    }

    public boolean inputDataCheck(List<UserDataDto> userDataList, String password, Model model) {

        // 社員情報のリストの中身やパスワードが空やnullの場合はエラーがメッセージを追加しfalseで返す。
        if (userDataList.isEmpty() || userDataList == null  || password.isEmpty() || password == null) {
            model.addAttribute(ERRORMESSAGE, ERROR);
            return false;
        }

        String dbPassword = userDataList.get(0).getPassword();

        // 社員情報のパスワードと取得してきたパスワードが違う場合はエラーがメッセージを追加しfalseで返す。
        if (!password.equals(dbPassword)) {
            model.addAttribute(ERRORMESSAGE, ERROR);
            return false;
        }
        
        // チェックOK
        return true;
    }

}
