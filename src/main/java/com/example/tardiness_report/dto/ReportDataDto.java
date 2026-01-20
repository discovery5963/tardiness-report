package com.example.tardiness_report.dto;

import lombok.Builder;
import lombok.Data;


// 遅刻理由DTO
@Data
@Builder
public class ReportDataDto {
    //遅刻理由ID
    private String lateReasonId;
    // 社員ID
    private String empId;
    // 遅刻理由コード
    private String lateReasonCd;
    // 状態ステータス
    private String resisterContentCd;
    // 路線ID
    private String lineId;
    // 詳細
    private String detail;
    // 更新日付
    private String updateDate;
    // 処理日付
    private String resisterDate;
}
