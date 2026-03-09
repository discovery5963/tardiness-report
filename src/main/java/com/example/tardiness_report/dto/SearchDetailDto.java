package com.example.tardiness_report.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
/** 検索・参照画面DTO */
public class SearchDetailDto {

    /** 登録日時 */
    private Timestamp registerDate;

    /** 社員姓 */
    private String empLname;

    /** 社員名 */
    private String empFname;

    /** 遅刻理由コード */
    private String lateReasonCd;

    /** 路線名 */
    private String lineName;

    /** 遅刻詳細 */
    private String detail;
}
