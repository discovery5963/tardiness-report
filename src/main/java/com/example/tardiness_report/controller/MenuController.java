package com.example.tardiness_report.controller;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public class MenuController {

    // 初期表示
    @GetMapping("/menu")
    public String showMenu(Model model, HttpSession session) {
        // TODO:初期表示に必要なのはヘッダー情報のみ
        model.addAttribute("empId", session.getAttribute("empId"));
        model.addAttribute("empLname", session.getAttribute("empLname"));
        model.addAttribute("empFname", session.getAttribute("empFname"));
        model.addAttribute("teamName", session.getAttribute("teamName"));
        model.addAttribute("unitNo", session.getAttribute("unitNo"));
        model.addAttribute("roleName", session.getAttribute("roleName"));

        return "menu";
    }

    // @PostMapping("/report")
    // public String moveReport() {
    //     return "report";
    // }

    // @PostMapping("/search")
    // public String moveSearch() {
    //     return "search";
    // }
}
