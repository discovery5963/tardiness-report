package com.example.tardiness_report.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.tardiness_report.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;



@Controller
public class LoginController {

    private final LoginService loginService;
    

    public LoginController(LoginService loginService){
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String loginHome() {
        System.out.println("ログインOK");
        return "login" ;
    }

    @PostMapping("/menu")
    public String getLogin(@RequestParam("empID") String empID,@RequestParam("password") String password, 
    HttpSession session, Model model) {

        Map<String, String> loginFormat = new HashMap<>();
        loginFormat.put("empID", empID);
        loginFormat.put("password", password);

        if(!loginService.dbCheck()){
            System.out.println("接続失敗");
            return "login";
        }
        

        if (!loginService.getLoginMethod(loginFormat, session, model)) {
            model.addAttribute("error", 1);
            return "login";
        }
        
        System.out.println("画面遷移OK");
        System.out.println(session.getAttribute("password"));

        return "menu";
    }
}
