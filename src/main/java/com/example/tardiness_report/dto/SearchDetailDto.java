package com.example.tardiness_report.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class SearchDetailDto {

    // 画面表示用（検索条件エリア）

    /** 開始日付 */
    private String startDate;
    /** 終了日付 */
    private String endDate;

    /** 路線ID(路線名称のKey) */
    private String lineIdJoken;
    /** 路線名称 */
    private String lineNameJoken;
    /** 路線（その他） */
    private boolean islineOthers;
    /** 社員ID(社員名称のKey) */
    private String empId;
    /** 社員名称 */
    private String empLnameJoken;

    // 検索で路線と路線（その他）の選択肢を保持する
    private Map<String, Map<Integer, String>> mapitems;
    private String mapstring;

    private Map<String, Object> msg;

    // 画面表示用(検索結果表示エリア)
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
    /** 一覧表示用リスト */
    private List<SearchDetailDto> searchedList;

    // private String code;
    // private String name;
    // private String kana;
    // private String password;
    // private Integer stateCode;
    // private Integer statusCode;
    // private String divisionname;

    // private String searchname;
    // private String searchkana;

    // // 状態（複数）
    // private List<Integer> stateKeys;

    // // 雇用形態
    // private Integer statusKey;

    // // 検索で状態と雇用形態の選択肢を保持する
    // private Map<String, Map<Integer, String>> mapitems;
    // private String mapstring;
    // private Map<String, Object> msg;

    public SearchDetailDto() {

        // 検索条件エリア初期値
        startDate = "";
        endDate = "";
        lineIdJoken = "";
        lineNameJoken = "";
        islineOthers = false;
        empLnameJoken = "";
        empId = "";

        // 検索結果表示エリア初期値
        registeredDate = "";
        empLname = "";
        lateReasonCd = "";
        lateReason = "";
        lineName = "";
        detail = "";
        searchedList = new ArrayList<SearchDetailDto>();
        // code = "";
        // name = "";
        // kana = "";
        // password = "";
        // stateCode = 0;
        // statusCode = 0;
        // divisionname = "";

        // searchname = "";
        // searchkana = "";
        // stateKeys = new ArrayList<Integer>();
        // statusKey = 0;
        mapitems = new HashMap<String, Map<Integer, String>>();
        mapstring = "";
        msg = new HashMap<String, Object>();
    }
}
