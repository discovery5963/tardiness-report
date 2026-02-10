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
        private final String INPUT_DETAIL = "電車遅延時間：分\r\n" + "現場遅刻時間：分";
    
        public ReportController(ReportService reportService){
        this.reportService = reportService;
    }

    //新規登録
    @RequestMapping("/report")
    public String returnHtml(HttpSession session,
        Model model) {
        // 動作確認用
        session.setAttribute("empId", "0000000002");
        // session.setAttribute("lateReasonId", "1");
        boolean errFlg = false;

        // 社員IDをmodelにセット
        model.addAttribute("empId", session.getAttribute("empId"));
        System.out.println("log1");

        // 内容初期化
        model.addAttribute("inputDetail", INPUT_DETAIL);

        // セッションに遅刻理由IDが含まれている場合、遅刻理由IDから当日の遅刻レコードを検索
        if(!Objects.isNull(session.getAttribute("lateReasonId"))){
            model.addAttribute("lateReasonId", session.getAttribute("lateReasonId"));
            errFlg = reportService.getTodayTardinessRecord(model);
            model.addAttribute("inputDetail", model.getAttribute("detail"));
            model.addAttribute("line", model.getAttribute("lineId"));
            System.out.println("log2");
        // セッションに遅刻理由IDが含まれていない場合、社員IDから当日の遅刻レコードを検索
        } else {
            errFlg = reportService.getTardinessRecord(model);
            if(!Objects.isNull(model.getAttribute("detail"))){
            model.addAttribute("inputDetail", model.getAttribute("detail"));
            model.addAttribute("line", model.getAttribute("lineId"));
            System.out.println("log3");
            }
        }
    
        // 当日の遅刻レコードが存在する場合には、登録状態に合わせて画面表示させる。
        // 存在しない場合は初期画面としてtypeFlg:1、formatFlg:1と表示させる
        if(!Objects.isNull(model.getAttribute("resisterContentCd"))){
            /*
                状態ステータスをtypeFlgに設定
                1:登録
                2:確認
            */
            model.addAttribute("typeFlg", 2);
            System.out.println("log4" + model.getAttribute("resisterContentCd"));
           /*
                遅刻理由IDをformatFlgに設定
                1:電車
                2:フリー
            */
            model.addAttribute("formatFlg", model.getAttribute("lateReasonCd"));
            System.out.println("log5" + model.getAttribute("lateReasonCd"));
            /*
                resistFlg
                1:未登録
                2:登録済み
             */
            model.addAttribute("resistFlg", 2);
        } else {
            model.addAttribute("typeFlg", 1);
            model.addAttribute("formatFlg", 1);
            model.addAttribute("resistFlg", 1);
        }
        session.setAttribute("resistFlg", model.getAttribute("resistFlg"));
        
        return "report";
    }

    //登録
    @PostMapping("/resist")
    public String resistReport(HttpSession session,
        Model model,
        @RequestParam("inputDetail") String inputDetail,
        @RequestParam("format") String format,
        @RequestParam("line") String line
        ){
        String empId = (String) session.getAttribute("emp_id");
            /* ログ出力用 */
        final String ID ="社員IDは：";
        final String CODE ="遅刻理由コードは：";
        final String LINE ="路線IDは：";
        final String DETAIL ="内容は：";
        System.out.println("======logStart======");
        System.out.println(ID + empId);
        System.out.println(CODE + format);
        System.out.println(LINE + line);
        System.out.println(DETAIL + inputDetail);
        model.addAttribute("format", format);
        model.addAttribute("line", line);
        model.addAttribute("inputDetail", inputDetail);
        model.addAttribute("typeFlg", 2);


        if(session.getAttribute("resistFlg").equals(1)){
            // INSERT処理
            
        } else {
            // UPDATE処理

        }

        return "report";
    }
    // 修正
    @PostMapping("/modify")
    public String modifyReport(HttpSession session,
        Model model,
        @RequestParam("inputDetail") String inputDetail,
        @RequestParam("format") String format,
        @RequestParam("line") String line){
        model.addAttribute("format", format);
        model.addAttribute("line", line);
        model.addAttribute("inputDetail", inputDetail);
        model.addAttribute("typeFlg", 1);
        return "report";
    }
}

