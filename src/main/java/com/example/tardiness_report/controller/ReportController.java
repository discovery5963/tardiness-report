package com.example.tardiness_report.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;

@Controller
public class ReportController {

    @RequestMapping("/report")
    public String returnHtml(HttpSession session,
        Model model) {
        session.setAttribute("emp_id", "0000000001");
        /*typeFlg 
         * 1:登録
         * 2:確認
         */
        model.addAttribute("typeFlg", 1);
        /*formatFlg
         * 1: 電車遅延
         * 2: フリーフォーマット
         */
        model.addAttribute("formatFlg", 1);
        return "report";
    }

    @PostMapping("/resist")
    public String resistReport(HttpSession session,
        Model model,
        @RequestParam("inputDetail") String inputDetail,
        @RequestParam("format") String format,
        @RequestParam("line") String line
        ){
        final String ID ="社員IDは：";
        final String CODE ="遅刻理由コードは：";
        final String CONTENT ="状態ステータスは：";
        final String LINE ="路線IDは：";
        final String DETAIL ="内容は："; 
        System.out.println("======logStart======");
        String empId = (String) session.getAttribute("emp_id");
        int content = 1;
        System.out.println(ID + empId);
        System.out.println(CODE + format);
        if(session.getAttribute("late_reason_id") == null){
            content = 2;
        }
        System.out.println(CONTENT + content);
        System.out.println(LINE + line);
        System.out.println(DETAIL + inputDetail);
        model.addAttribute("inputDetail", inputDetail);
        model.addAttribute("line", line);

        /*typeFlg 
         * 1:登録
         * 2:確認
         */
        model.addAttribute("typeFlg", 2);
        return "report";
    } 
}

