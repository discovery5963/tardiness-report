package com.example.tardiness_report.dto;

import java.util.Map;
import lombok.Data;

/**
 * 検索・参照画面の画面入力値を保持するクラス
 */
@Data
public class SearchDetailForm {

    // 検索条件エリア
    /** 開始日付 */
    private String startDate;
    /** 終了日付 */
    private String endDate;

    /** 路線(ID) */
    private String lineId;
    /** 路線(その他) */
    private boolean islineOthers;
    /** 社員名称(ID) */
    private String empId;

    private Map<String, Object> msg;

    // 検索結果表示エリア1
    /** 現在ページ数 */
    private int currentPageNumber;
    /** 開始表示件数 */
    private int startViewingDataCount;
    /** 終了表示件数 */
    private int endViewingDataCount;
    /** 総件数 */
    private int allDataCount;
    /** ページ毎表示件数 */
    private int itemsDisplayed;
    /** 開始ページ件数 */
    private int startPageNumber;
    /** 終了ページ件数 */
    private int endPageNumber;
    // DB項目以外（これより↑はすべて別クラスに移動する
    // TODO

    // 検索結果表示エリア2
    /** 日付 */
    private String registeredDate;
    /** 社員名 */
    private String empLname;
    /** 遅刻理由コード */
    private String lateReasonCd;
    /** 遅刻理由 */
    private String lateReason;
    /** 路線名 */
    private String lineName;
    /** 内容（遅刻詳細） */
    private String detail;


}
