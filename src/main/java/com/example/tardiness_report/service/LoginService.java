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

    public boolean getLoginMethod(Map<String, String> loginFormat, HttpSession session,
            Model model) {

        // userDataをDBから取得してきたあたいとして一旦作成(この後削除する記述)
        List<UserDataDto> userData = new ArrayList<UserDataDto>();
        //userData.setPassword("pass");
        userData = fetchEmployees();

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
        //     model.addAttribute(ERRORMESSAGE, ERROR);
        //     return false;
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
    public List<UserDataDto> fetchEmployees() {
        return employeeMstRepository.getEmpData();
    }

}




