package com.example.tardiness_report.controller;

import java.util.Objects;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.tardiness_report.dto.LineMstDto;
import com.example.tardiness_report.dto.SearchDetailForm;
import com.example.tardiness_report.repository.LineMstRepository;
import com.example.tardiness_report.repository.LateReasonRepository;
import com.example.tardiness_report.service.ReportService;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

@Controller
public class ReportController {
        private final ReportService reportService;
        private final LineMstRepository lineMstRepository;
		private final LateReasonRepository lateReasonRepository;
        private final String INPUT_DETAIL = "電車遅延時間：分\r\n" + "現場遅刻時間：分";
    
        public ReportController(ReportService reportService, LineMstRepository lineMstRepository,
         LateReasonRepository lateReasonRepository){
        this.reportService = reportService;
        this.lineMstRepository = lineMstRepository;
		this.lateReasonRepository = lateReasonRepository;
    }

    @ModelAttribute("searchDetailForm")
    public SearchDetailForm searchDetailForm() {
        return new SearchDetailForm();
    }

    //新規登録
    @RequestMapping("/report")
    public String returnHtml(HttpSession session,
        Model model) {
        // 動作確認用
        // session.setAttribute("empId", "0000000002");
        // session.setAttribute("lateReasonId", "1");
        boolean errFlg = false;

        // 路線名の取得
        List<LineMstDto> lineList = lineMstRepository.getAllLineMstData();
        model.addAttribute("lineList", lineList); // "lineList"としてhtmlに連携する。
        model.addAttribute("lineId", 0000000001);
        
        // 社員IDをmodelにセット
        model.addAttribute("empId", session.getAttribute("empId"));
        System.out.println("log1");

        String strInputDetail = (String)session.getAttribute("inputDetail");
        // 内容初期化
        if(strInputDetail == null){
            model.addAttribute("inputDetail", INPUT_DETAIL);
        } else {
            model.addAttribute("inputDetail", session.getAttribute("inputDetail"));
            model.addAttribute("line", session.getAttribute("line"));
            model.addAttribute("format", session.getAttribute("format"));
            model.addAttribute("typeFlg", 2);
            model.addAttribute("formatFlg", 1);
            model.addAttribute("resistFlg", 1);
            return "report";
        }
        

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
            session.setAttribute("lateReasonId", model.getAttribute("lateReasonId"));
            model.addAttribute("inputDetail", model.getAttribute("detail"));
            model.addAttribute("line", model.getAttribute("lineId"));
            String lineName = lineList.stream()
            .filter(item -> item.getLineId().equals(model.getAttribute("lineId")))
            .map(LineMstDto::getLineName)
            .findFirst()
            .orElse(null);
            model.addAttribute("lineName", lineName);
            session.setAttribute("format", model.getAttribute("late_reason_cd"));
            System.out.println("log3");
            }
        }

        String lateReasonCd =(String) model.getAttribute("lateReasonCd");
        if (lateReasonCd != null) {
            session.setAttribute("lateReasonCd", lateReasonCd);
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
        @RequestParam("lineId") String lineId,
        @RequestParam("lineName") String lineName
        ){
        String empId = (String) session.getAttribute("empId");
        if("train".equals(format)){
            format = "1";
        } else if ("free".equals(format)){
            format = "2";
        }
            /* ログ出力用 */
        final String ID ="社員IDは：";
        final String CODE ="遅刻理由コードは：";
        final String LINE ="路線IDは：";
        final String DETAIL ="内容は：";
        boolean errFlg = true;
        System.out.println("======logStart======");
        System.out.println(ID + empId);
        System.out.println(CODE + format);
        System.out.println(LINE + lineId);
        System.out.println(DETAIL + inputDetail);
        System.out.println("======logSnd======");
        
        session.setAttribute("format", format);
        session.setAttribute("lineId", lineId);
        session.setAttribute("inputDetail", inputDetail);
        model.addAttribute("format", format);
        model.addAttribute("lineId", lineId);
        model.addAttribute("lineName", lineName);
        model.addAttribute("inputDetail", inputDetail);
        model.addAttribute("typeFlg", 2);
        String lateReasonCd = format;
        String lateReasonId = null;
        List<LineMstDto> lineList = lineMstRepository.getAllLineMstData();
        model.addAttribute("lineList", lineList);
        errFlg = reportService.getTardinessRecord(model);
        if(!Objects.isNull(model.getAttribute("detail"))){
            session.setAttribute("lateReasonId", model.getAttribute("lateReasonId"));
            lateReasonId = (String)session.getAttribute("lateReasonId");
            session.setAttribute("resistFlg", 2);
        }

        System.out.println(lateReasonCd);
        if(session.getAttribute("resistFlg").equals(1)){
            // INSERT処理
            lateReasonRepository.insertLateReason(empId,lateReasonCd,format,lineId,inputDetail);
        } else if(session.getAttribute("resistFlg").equals(2)) {
            // UPDATE処理
            lateReasonRepository.updateLateReason(lateReasonCd,format,lineId,inputDetail,lateReasonId);
        }
        if(model.getAttribute("format").equals("2")){
            lineId = null;
        }
        lineName = lineList.stream()
        .filter(item -> item.getLineId().equals(model.getAttribute("lineId")))
        .map(LineMstDto::getLineName)
        .findFirst()
        .orElse(null);
        model.addAttribute("lineName", lineName);
        return "report";
    }
    // 修正
    @PostMapping("/modify")
    public String modifyReport(HttpSession session,
        Model model){
        String format = "";
        // 路線名の取得
        List<LineMstDto> lineList = lineMstRepository.getAllLineMstData();
        model.addAttribute("lineList", lineList);
        String lineName = "";
        lineName = lineList.stream()
        .filter(item -> item.getLineId().equals(model.getAttribute("lineId")))
        .map(LineMstDto::getLineName)
        .findFirst()
        .orElse(null);
        model.addAttribute("lineName", lineName);
        boolean errFlg = true;
        String lateReasonId = null;
        errFlg = reportService.getTardinessRecord(model);
        if(!Objects.isNull(model.getAttribute("detail"))){
            session.setAttribute("lateReasonId", model.getAttribute("lateReasonId"));
            lateReasonId = (String)session.getAttribute("lateReasonId");
            model.addAttribute("inputDetail", model.getAttribute("detail"));
            lineName = lineList.stream()
            .filter(item -> item.getLineId().equals(model.getAttribute("lineId")))
            .map(LineMstDto::getLineName)
            .findFirst()
            .orElse(null);
            model.addAttribute("lineName", lineName);
            session.setAttribute("resistFlg", 2);
            session.setAttribute("format",model.getAttribute("lateReasonCd"));
        }    
        format = (String)session.getAttribute("format");
        if("1".equals(format)){
            format = "1";
        } else if ("2".equals(format)){
            format = "2";
        } 
        model.addAttribute("formatFlg", format);
        model.addAttribute("typeFlg", 1);
        return "report";
    }
}

