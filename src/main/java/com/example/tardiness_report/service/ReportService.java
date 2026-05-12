package com.example.tardiness_report.service;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import com.example.tardiness_report.dto.ReportDataDto;
import com.example.tardiness_report.repository.LateReasonRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class ReportService {
    private final LateReasonRepository lateReasonRepository;
    // 遅刻理由IDをキーに検索
    public boolean getTodayTardinessRecord(Model model){
        List<ReportDataDto> lateReasonData = new ArrayList<ReportDataDto>();
        boolean errFlg = false;
        try{
            lateReasonData = fetchLateReasonFromlateReasonId(model);
            // 取得結果をmodelにadd
            model.addAttribute("lateReasonCd", lateReasonData.get(0).getLateReasonCd());
            model.addAttribute("resisterContentCd", lateReasonData.get(0).getResisterContentCd());
            model.addAttribute("lineId", lateReasonData.get(0).getLineId());
            model.addAttribute("detail", lateReasonData.get(0).getDetail());
        } catch(Exception e) {
            errFlg = true;

        }
        return errFlg;
    }
    // 当日の遅刻レコード検索
    public boolean getTardinessRecord(Model model){
        List<ReportDataDto> lateReasonData = new ArrayList<ReportDataDto>();
        boolean errFlg = false;
        try{
            lateReasonData = fetchLateReason(model);
            // 取得結果をmodelにadd
            // 遅刻理由ID
            model.addAttribute("lateReasonId", lateReasonData.get(0).getLateReasonId());
            // 遅刻理由コード
            model.addAttribute("lateReasonCd", lateReasonData.get(0).getLateReasonCd());
            // 状態理由コード
            model.addAttribute("resisterContentCd", lateReasonData.get(0).getResisterContentCd());
            // 路線ID
            model.addAttribute("lineId", lateReasonData.get(0).getLineId());
            // 詳細
            model.addAttribute("detail", lateReasonData.get(0).getDetail());
        } catch(Exception e) {
            errFlg = true;

        }
        return errFlg;
    }

    // 登録処理
    public boolean insertLateReason(Model model){
        boolean errFlg = false;
        try{
            fetchInsertlateReasonId(model);
        } catch(Exception e) {
            errFlg = true;
        }
        return errFlg;
    }

    // 更新処理
    public boolean updateLateReason(){
        boolean errFlg = false;
        try{

        } catch(Exception e) {
            errFlg = true;

        }
        return errFlg;
    }

    // 遅刻理由IDにてレコード検索
    public List<ReportDataDto> fetchLateReasonFromlateReasonId(Model model) {
        return lateReasonRepository.getLateReasonFromlateReasonId((String)model.getAttribute("lateReasonId"));
    }
    // 遅刻理由IDにてレコード検索
    public List<ReportDataDto> fetchLateReason(Model model) {
        return lateReasonRepository.getLateReason((String)model.getAttribute("empId"));
    }

    // 登録処理
    public void fetchInsertlateReasonId(Model model) {
        lateReasonRepository.insertLateReason(
            (String)model.getAttribute("empId"),
            (String)model.getAttribute("format"),
            "1",
            (String)model.getAttribute("line"),
            (String)model.getAttribute("inputDetail"));
    }

    // 更新処理
    public void fetchUpdatelateReasonId(Model model) {
        lateReasonRepository.updateLateReason(
            (String)model.getAttribute("format"),
            "2",
            (String)model.getAttribute("line"),
            (String)model.getAttribute("inputDetail"),
            (String)model.getAttribute("lateReasonId"));
    }
}
