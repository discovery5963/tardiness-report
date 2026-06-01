package com.example.tardiness_report.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
/** 検索・参照画面DTO */
public class SearchDetailDto {

    /** 遅刻理由ID */
    private Long lateReasonId;

    /** 社員ID */
    private String empId;

    /** 登録日時 */
    private Timestamp registerDate;

    /** 社員姓 */
    private String empLname;

    /** 社員名 */
    private String empFname;

    /** 社姓姓名 */
    private String empName;

    /** 遅刻理由コード */
    private String lateReasonCd;

    /** 遅刻理由 */
    private String lateReason;

    /** 路線名 */
    private String lineName;

    /** 遅刻詳細 */
    private String detail;

    /** データ総件数 */
    private int allCount;
}
