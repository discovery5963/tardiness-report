package com.example.tardiness_report.service;

import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Service
@ControllerAdvice
public class HeaderService {

    /**
     * ヘッダーに表示する社員情報をセットするためのメソッド
     * @param model セッション情報を格納するmodel
     * @param session セッション情報
     */
    @ModelAttribute
    public static void headerModelSet(Model model, HttpSession session){
        // ヘッダー情報をmodelに格納
        model.addAttribute("empId", session.getAttribute("empId"));
        model.addAttribute("empLname", session.getAttribute("empLname"));
        model.addAttribute("empFname", session.getAttribute("empFname"));
        model.addAttribute("teamName", session.getAttribute("teamName"));
        model.addAttribute("unitNo", session.getAttribute("unitNo"));
        model.addAttribute("roleName", session.getAttribute("roleName"));
    }

}
