package com.example.tardiness_report;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
public class Report {

    @RequestMapping("/report")
    public String returnHtml(Model model) {
        model.addAttribute("typeFlg", 2);
        model.addAttribute("formatFlg", 1);
        return "report";
    }
}
