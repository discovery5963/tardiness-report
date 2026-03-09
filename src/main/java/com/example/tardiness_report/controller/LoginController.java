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

    /**
     * 初期表示.
     *
     * @return ログイン画面
     */
    @GetMapping("/login")
    public String loginHome() {
        return "login";
    }

    /**
     * 社員IDとパスワードが入力され、ログインが押下された際の処理.
     *
     * @param empID 入力された社員情報
     * @param password 入力されたパスワード
     * @param session ログイン成功時に、後続へ社員情報を格納するためsession
     * @param model ログインに失敗した際の値を格納するmodel
     * @return メニュー画面
     */
    @PostMapping("/forMenu")
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

        // 3.3. 「3.1.」で取得してきた以下のユーザ情報をセッションに格納
        UserDataDto userData= userDataList.get(0);
        session.setAttribute("empId", userData.getEmpId());
        session.setAttribute("empLname", userData.getEmpLname());
        session.setAttribute("empFname", userData.getEmpFname());
        session.setAttribute("teamId", userData.getTeamId());
        session.setAttribute("teamName", userData.getTeamName());
        session.setAttribute("unitNo", userData.getUnitNo());
        session.setAttribute("roleName", userData.getRoleName());

        // 3.4. 下記画面に遷移する。
        return "redirect:/menu";
    }
}
