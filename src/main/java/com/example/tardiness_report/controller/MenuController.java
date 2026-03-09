package com.example.tardiness_report.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

@Controller
public class MenuController {

    /**
     * 初期表示
     *
     * @param model 初期表示に必要な値を格納するmodel
     * @param session セッション情報
     * @return メニュー画面
     */
    @GetMapping("/menu")
    public String showMenu(Model model, HttpSession session) {
        // ヘッダー情報の格納
        model.addAttribute("empId", session.getAttribute("empId"));
        model.addAttribute("empLname", session.getAttribute("empLname"));
        model.addAttribute("empFname", session.getAttribute("empFname"));
        model.addAttribute("teamName", session.getAttribute("teamName"));
        model.addAttribute("unitNo", session.getAttribute("unitNo"));
        model.addAttribute("roleName", session.getAttribute("roleName"));

        return "menu";
    }

    @GetMapping("/forReport")
    public String moveReport() {
        return "redirect:/report";
    }

    @GetMapping("/forSearch")
    public String moveSearch() {
        return "redirect:/search";
    }
}
