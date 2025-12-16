package com.example.tardiness_report.service;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import com.example.tardiness_report.dto.ReportDataDto;
import com.example.tardiness_report.repository.LateReasonRepository;

@Service
public class ReportService {
    public void getTodayTardinessRecord(Model model){

        // 遅刻理由IDを保持している場合
        if(!model.getAttribute("lateReasonId").equals(null)){
            List<ReportDataDto> lateReasonData = new ArrayList<ReportDataDto>();
            // 遅刻理由IDをキーに検索
            lateReasonData = fetchLateReason(model);
            // 取得結果をmodelにadd
            model.addAttribute("lateReasonCd", lateReasonData.get(0).getLateReasonCd());
            model.addAttribute("lateResisterContentCd", lateReasonData.get(0).getResisterContentCd());
            model.addAttribute("lineId", lateReasonData.get(0).getLineId());
            model.addAttribute("detail", lateReasonData.get(0).getDetail());
        }
    }
        // 遅刻理由IDにてレコード検索
    public List<ReportDataDto> fetchLateReason(Model model) {
        return LateReasonRepository.getLateReasonFromlateReasonId(model);
    }
}
