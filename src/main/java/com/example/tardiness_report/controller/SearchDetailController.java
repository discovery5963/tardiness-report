package com.example.tardiness_report.controller;

import com.example.tardiness_report.dto.LineMstDto;
import com.example.tardiness_report.dto.SearchDetailDto;
import com.example.tardiness_report.dto.SearchDetailForm;
import com.example.tardiness_report.dto.UserDataDto;
import com.example.tardiness_report.repository.EmployeeMstRepository;
import com.example.tardiness_report.repository.LineMstRepository;
import com.example.tardiness_report.repository.SearchDetailRepository;
import com.example.tardiness_report.service.SearchDetailService;
import com.example.tardiness_report.controller.ReportController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchDetailController {

    private final SearchDetailService searchDetailService;
    private final LineMstRepository lineMstRepository;
    private final EmployeeMstRepository employeeMstRepository;
    private final SearchDetailRepository searchDetailRepository;

    // エラーメッセージ
    private String ERRORMESSAGE = "errorMessage";

    /** 1ページあたりの表示件数 */
    private static final int ITEMS_PER_PAGE = 10;

    public SearchDetailController(SearchDetailService searchDetailService,
            LineMstRepository lineMstRepository, EmployeeMstRepository employeeMstRepository,
            SearchDetailRepository searchDetailRepository) {
        this.searchDetailService = searchDetailService;
        this.lineMstRepository = lineMstRepository;
        this.employeeMstRepository = employeeMstRepository;
        this.searchDetailRepository = searchDetailRepository;
    }

    // form初期化
    @ModelAttribute("searchDetailForm")
    public SearchDetailForm searchDetailForm() {
        return new SearchDetailForm();
    }

    // 従業員マスタ情報取得
    @ModelAttribute("employeeList")
    public List<UserDataDto> getEmployeeList(HttpSession session) {
        return employeeMstRepository.getEmpDataForSearchDetail(
                (String) session.getAttribute("unitNo"), (String) session.getAttribute("teamId"),
                (String) session.getAttribute("role"));
    }

    // 路線マスタ情報取得
    @ModelAttribute("lineList")
    public List<LineMstDto> getLineList() {
        return lineMstRepository.getAllLineMstData();
    }

    /**
     * 検索・参照画面を初期表示
     *
     * @param model モデル
     * @param session セッション
     * @return 検索・参照画面(初期表示)
     */
    @GetMapping("/search")
    public String showDetail(Model model, HttpSession session) {

        // 路線名の取得
        List<LineMstDto> lineList = lineMstRepository.getAllLineMstData();
        model.addAttribute("lineList", lineList); // "lineList"としてhtmlに連携する。

        // 社員リストの取得
        List<UserDataDto> employeeList = getEmployeeList(session);
        model.addAttribute("employeeList", employeeList);

        model.addAttribute("searchDetailForm", new SearchDetailForm());
        model.addAttribute("isInitial", true); // 初期表示フラグ
        return "search-detail";
    }

    /**
     * 検索ボタン押下時処理
     *
     * @param form form入力値
     * @param model モデル
     * @param session セッション
     * @return 検索・参照画面(初期表示)
     */
    @PostMapping(value = "/search", params = "doSearch")
    public String doSearch(@ModelAttribute SearchDetailForm form, Model model,
            HttpSession session) {

        boolean isCsvOutput = false; // CSV出力フラグをfalseに設定

        // 社員IDの設定
        String empID = "";
        if (session.getAttribute("role").equals("1")) {
            // 一般社員の場合、検索条件の従業員IDに自分の従業員IDをセット
            empID = String.valueOf(session.getAttribute("empId"));
        } else {
            // 一般社員以外の場合、検索条件の従業員IDに入力した値をセット
            empID = form.getEmpId();
        }

        // 初期ページ番号を設定
        form.setCurrentPageNumber(1);

        // 開始日・終了日入力チェック（片方しか入力されていない場合エラー）
        if ((form.getStartDate() != null && !form.getStartDate().isEmpty())
                ^ (form.getEndDate() != null && !form.getEndDate().isEmpty())) {
            model.addAttribute(ERRORMESSAGE, "日付指定時は開始日付と終了日付はどちらも必須です。");
            return "search-detail";
        }

        // 総件数取得
        int allCount =
                searchDetailRepository.getAllListCount(String.valueOf(session.getAttribute("role")),
                        empID, String.valueOf(session.getAttribute("teamId")), form.getLineId(),
                        form.getStartDate(), form.getEndDate());
        form.setAllDataCount(allCount);

        // ページング時共通処理
        commonUtil(form, model, session);

        // 表示用にページングされたリストを取得
        List<SearchDetailDto> resultList =
                searchDetailRepository.getResultList(String.valueOf(session.getAttribute("role")),
                        empID, String.valueOf(session.getAttribute("teamId")), form.getStartDate(),
                        form.getEndDate(), form.getLineId(), ITEMS_PER_PAGE,
                        form.getCurrentPageNumber(), isCsvOutput);

        // 最大ページ番号計算
        int maxPageNum = (int) Math.ceil((double) form.getAllDataCount() / ITEMS_PER_PAGE);
        form.setMaxPageNumber(maxPageNum);


        model.addAttribute("searchDetailForm", form); // これで十分なはず
        model.addAttribute("resultList", resultList);
        model.addAttribute("isInitial", false); // 初期表示フラグOFF
        return "search-detail"; // 結果を同じ画面に表示
    }

    /**
     * 「前へボタン」押下時処理
     *
     * @param form form入力値
     * @param model モデル
     * @param session セッション
     * @return 検索・参照画面(初期表示)
     */
    @PostMapping(value = "/search", params = "goPrevious")
    public String goPreviousPage(@ModelAttribute SearchDetailForm form, Model model,
            HttpSession session) {

        // 社員IDの設定
        String empID = "";
        if (session.getAttribute("role").equals("1")) {
            // 一般社員の場合、検索条件の従業員IDに自分の従業員IDをセット
            empID = String.valueOf(session.getAttribute("empId"));
        } else {
            // 一般社員以外の場合、検索条件の従業員IDに入力した値をセット
            empID = form.getEmpId();
        }

        boolean isCsvOutput = false; // CSV出力フラグをfalseに設定

        // ページ番号を減算
        form.setCurrentPageNumber(form.getCurrentPageNumber() - 1);

        // ページング時共通処理
        commonUtil(form, model, session);

        // 表示用にページングされたリストを取得
        List<SearchDetailDto> resultList =
                searchDetailRepository.getResultList(String.valueOf(session.getAttribute("role")),
                        empID, String.valueOf(session.getAttribute("teamId")), form.getStartDate(),
                        form.getEndDate(), form.getLineId(), ITEMS_PER_PAGE,
                        form.getCurrentPageNumber(), isCsvOutput);

        model.addAttribute("searchDetailForm", form);
        model.addAttribute("resultList", resultList);
        model.addAttribute("isInitial", false); // 初期表示フラグ
        return "search-detail"; // 結果を同じ画面に表示
    }

    /**
     * 「次へボタン」押下時処理
     *
     * @param form form入力値
     * @param model モデル
     * @param session セッション
     * @return 検索・参照画面(初期表示)
     */
    @PostMapping(value = "/search", params = "goNext")
    public String goNextPage(@ModelAttribute SearchDetailForm form, Model model,
            HttpSession session) {

        // 社員IDの設定
        String empID = "";
        if (session.getAttribute("role").equals("1")) {
            // 一般社員の場合、検索条件の従業員IDに自分の従業員IDをセット
            empID = String.valueOf(session.getAttribute("empId"));
        } else {
            // 一般社員以外の場合、検索条件の従業員IDに入力した値をセット
            empID = form.getEmpId();
        }

        boolean isCsvOutput = false; // CSV出力フラグをfalseに設定

        // ページ番号を増算
        form.setCurrentPageNumber(form.getCurrentPageNumber() + 1);

        // 最後のページ判定
        boolean isLastPage = form.getCurrentPageNumber() * ITEMS_PER_PAGE >= form.getAllDataCount();
        form.setLastPage(isLastPage);

        // ページング時共通処理
        commonUtil(form, model, session);

        // 表示用にページングされたリストを取得
        List<SearchDetailDto> resultList =
                searchDetailRepository.getResultList(String.valueOf(session.getAttribute("role")),
                        empID, String.valueOf(session.getAttribute("teamId")), form.getStartDate(),
                        form.getEndDate(), form.getLineId(), ITEMS_PER_PAGE,
                        form.getCurrentPageNumber(), isCsvOutput);

        model.addAttribute("searchDetailForm", form);
        model.addAttribute("resultList", resultList);
        model.addAttribute("isInitial", false); // 初期表示フラグ
        return "search-detail"; // 結果を同じ画面に表示
    }

    /**
     * 「編集ボタン」押下時処理
     *
     * @param form form入力値
     * @param model モデル
     * @param session セッション
     * @return 報告画面（編集表示）
     */
    @PostMapping(value = "/search", params = "doEdit")
    public String doEdit(@ModelAttribute SearchDetailForm form, HttpSession session) {
                // 編集画面を呼び出し
                session.setAttribute("lateReasonId", form.getLateReasonId().toString());
                return "redirect:/report";
            }

    /**
     * CSV出力ボタン押下時処理
     *
     * @param form form入力値
     * @param model モデル
     * @param response HTTPレスポンス
     * @param session セッション
     * @return 検索・参照画面(初期表示)
     */
    @PostMapping(value = "/search", params = "csvOutput")
    public void csvOutput(@ModelAttribute SearchDetailForm form, Model model,
            HttpServletResponse response, HttpSession session) {

        // 社員IDの設定
        String empID = "";
        if (session.getAttribute("role").equals("1")) {
            // 一般社員の場合、検索条件の従業員IDに自分の従業員IDをセット
            empID = String.valueOf(session.getAttribute("empId"));
        } else {
            // 一般社員以外の場合、検索条件の従業員IDに入力した値をセット
            empID = form.getEmpId();
        }

        boolean isCsvOutput = true; // CSV出力フラグをtrueに設定

        // ページング時共通処理
        commonUtil(form, model, session);

        // CSV出力
        List<SearchDetailDto> resultList =
                searchDetailRepository.getResultList(String.valueOf(session.getAttribute("role")),
                        empID, String.valueOf(session.getAttribute("teamId")), form.getStartDate(),
                        form.getEndDate(), form.getLineId(), ITEMS_PER_PAGE,
                        form.getCurrentPageNumber(), isCsvOutput);

        try {
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=report.csv");

            PrintWriter writer = response.getWriter();

            // ヘッダー行の設定
            writer.println("日付,社員名,理由,路線,内容");

            // データ行の設定
            for (SearchDetailDto dto : resultList) {

                // 登録日は日付のみを出力する
                Timestamp ts = dto.getRegisterDate();
                String dateOnly = ts.toLocalDateTime().toLocalDate().toString(); // yyyy-MM-ddに変換

                writer.println(
                        dateOnly + "," + dto.getEmpName() + "," + removeLineBreak(dto.getDetail())
                                + "," + dto.getLineName() + "," + dto.getLateReason());
            }

            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("CSV 出力中にエラーが発生しました", e);
        }
    }

    /**
     * 文字列から改行コードを削除するユーティリティメソッド
     *
     * @param s 変換対象の文字列
     * @return 改行コードが削除後の文字列
     */
    private String removeLineBreak(String s) {
        if (s == null) {
            return "";
        } else {
            return s.replaceAll("[\\r\\n]", "");
        }
    }


    /**
     * 共通処理：ページネーション時共通処理
     *
     * @param form form入力値
     * @param model モデル
     * @param session セッション
     * @return 表示件数（xx ~ xx 件)の文字列
     */
    public void commonUtil(@ModelAttribute SearchDetailForm form, Model model,
            HttpSession session) {

        // 表示件数（xx ~ xx 件)の計算
        int startIndex = 0;
        int endIndex = 0;
        if (form.getAllDataCount() == 0) {
            form.setViewingDataCount("0件");
        } else {
            startIndex = (form.getCurrentPageNumber() - 1) * ITEMS_PER_PAGE + 1;
            endIndex =
                    Math.min(form.getCurrentPageNumber() * ITEMS_PER_PAGE, form.getAllDataCount());
        }


        String viewingDataCount = startIndex + "〜" + endIndex + "件";
        form.setViewingDataCount(viewingDataCount);


    }

}
