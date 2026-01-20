package com.example.tardiness_report.controller;

import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.tardiness_report.service.ReportService;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;

@Controller
public class ReportController {
        private final ReportService reportService;
    
        public ReportController(ReportService reportService){
        this.reportService = reportService;
    }

    //新規登録
    @RequestMapping("/report")
    public String returnHtml(HttpSession session,
        Model model) {
        // 動作確認用
        session.setAttribute("empId", "0000000002");
        session.setAttribute("lateReasonId", "1");
        boolean errFlg = false;
        /*
        新規登録フラグ
        1：新規
        2：登録済み
        */
        String resistFlg = "1";

        // 社員IDをmodelにセット
        model.addAttribute("empId", session.getAttribute("empId"));

        // セッションに遅刻理由IDが含まれている場合、遅刻理由IDから当日の遅刻レコードを検索
        if(Objects.isNull(session.getAttribute("lateReasonID"))){
            model.addAttribute("lateReasonId", session.getAttribute("lateReasonId"));
            errFlg = reportService.getTodayTardinessRecord(model);
            model.addAttribute("inputDetail", model.getAttribute("Detail"));
            model.addAttribute("line", model.getAttribute("lineId"));
            resistFlg = "2";
        // セッションに遅刻理由IDが含まれていない場合、社員IDから当日の遅刻レコードを検索
        } else {
            errFlg = reportService.getTardinessRecord(model);
            model.addAttribute("inputDetail", model.getAttribute("Detail"));
            model.addAttribute("line", model.getAttribute("lineId"));
            resistFlg = "2";
        }
    
        // 当日の遅刻レコードが存在する場合には、登録状態に合わせて画面表示させる。
        // 存在しない場合は初期画面としてtypeFlg:1、formatFlg:1と表示させる
        if(Objects.isNull(model.getAttribute("resisterContentCd"))){
            /*
                状態ステータスをtypeFlgに設定
                1:登録
                2:確認
            */
            if(model.getAttribute("resisterContentCd").equals("2")){
                model.addAttribute("typeFlg", 2);
            }
            model.addAttribute("typeFlg", 1);
            /*
                遅刻理由IDをformatFlgに設定
                1:電車
                2:フリー
            */
            if(Objects.isNull(model.getAttribute("lateReasonCd"))){
                model.addAttribute("formatFlg", model.getAttribute("lateReasonCd"));
            } else {
                model.addAttribute("formatFlg", 1);        
            }
        }
        session.setAttribute("resistFlg", resistFlg);
        return "report";
    }

    //報告修正
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
        int content;
        if(session.getAttribute("resistFlg").equals("1")){
            content = 1;
        } else {
            content = 2;
        }
        System.out.println(ID + empId);
        System.out.println(CODE + format);
        System.out.println(CONTENT + content);
        System.out.println(LINE + line);
        System.out.println(DETAIL + inputDetail);
        model.addAttribute("inputDetail", inputDetail);
        model.addAttribute("line", line);


        model.addAttribute("typeFlg", 2);
        return "report";
    } 
}

