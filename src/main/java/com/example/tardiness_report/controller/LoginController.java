package com.example.tardiness_report.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.tardiness_report.service.LoginService;
import com.example.tardiness_report.dto.UserDataDto;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

@Controller
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String loginHome() {
        return "login";
    }

    @PostMapping("/menu")
    public String getLogin(@RequestParam("empID") String empID, @RequestParam("password") String password,
            HttpSession session, Model model) {

        Map<String, String> loginFormat = new HashMap<>();
        loginFormat.put("empID", empID);
        loginFormat.put("password", password);

        // 3.1. 社員情報の取得を行う
        List<UserDataDto> userDataList = loginService.fetchEmployees(empID);
        
        // 3.2. 入力チェックを行う。
        if (!loginService.inputDataCheck(userDataList, password, model)) {
            model.addAttribute("error", 1);
            return "login";
        }

        // 3.3. 「3.1.」で取得してきた以下のユーザ情報をセッションに格納(TODO：直書きでOKとのこと)
        UserDataDto userData= userDataList.get(0);
        session.setAttribute("empId", userData.getEmpId());
        session.setAttribute("departmentName", userData.getDepartmentName());
        session.setAttribute("empLname", userData.getEmpLname());
        session.setAttribute("empFname", userData.getEmpFname());
        session.setAttribute("teamName", userData.getTeamName());
        // session.setAttribute("roleName", userData.getRoleName());// TODO：dtoに役職名を付け足さないといけない


        // 3.4. 下記画面に遷移する。

        System.out.println("画面遷移OK"); // TODO:確認用、後々削除する記載
        System.out.println(session.getAttribute("password"));// TODO:確認用、後々削除する記載

        return "menu";
    }
}
