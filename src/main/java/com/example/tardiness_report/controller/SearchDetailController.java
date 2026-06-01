package com.example.tardiness_report.controller;

import com.example.tardiness_report.dto.LineMstDto;
import com.example.tardiness_report.dto.SearchDetailDto;
import com.example.tardiness_report.dto.SearchDetailForm;
import com.example.tardiness_report.dto.UserDataDto;
import com.example.tardiness_report.repository.EmployeeMstRepository;
import com.example.tardiness_report.repository.LineMstRepository;
import com.example.tardiness_report.repository.SearchDetailRepository;
import com.example.tardiness_report.service.SearchDetailService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.io.PrintWriter;
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
                (String) session.getAttribute("teamId"), (String) session.getAttribute("role"));
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
     * @return 検索・参照画面(初期表示)
     */
    @PostMapping(value = "/search", params = "doSearch")
    public String doSearch(@ModelAttribute SearchDetailForm form, Model model,
            HttpSession session) {

        // 開始日・終了日入力チェック（片方しか入力されていない場合エラー）
        if ((form.getStartDate() != null && !form.getStartDate().isEmpty())
                ^ (form.getEndDate() != null && !form.getEndDate().isEmpty())) {
            model.addAttribute(ERRORMESSAGE, "日付指定時は開始日付と終了日付はどちらも必須です。");
            return "search-detail";
        }

        // 初期ページ番号を設定
        form.setCurrentPageNumber(1);

        String empID = "";
        if (session.getAttribute("role").equals("1")) {
            // 一般社員の場合、検索条件の従業員IDに自分の従業員IDをセット
            empID = String.valueOf(session.getAttribute("empId"));
        } else {
            // 一般社員以外の場合、検索条件の従業員IDに入力した値をセット
            empID = form.getEmpId();
        }

        // 総件数取得
        int allCount =
                searchDetailRepository.getAllListCount(String.valueOf(session.getAttribute("role")),
                        empID, String.valueOf(session.getAttribute("teamId")), form.getLineId(),
                        form.getStartDate(), form.getEndDate());
        form.setAllDataCount(allCount);

        // 表示用にページングされたリストを取得
        List<SearchDetailDto> resultList = searchDetailRepository.getResultList(
                String.valueOf(session.getAttribute("role")), empID,
                String.valueOf(session.getAttribute("teamId")), form.getStartDate(),
                form.getEndDate(), form.getLineId(), ITEMS_PER_PAGE, form.getCurrentPageNumber());

        // ページネーション時共通処理
        commonUtil(form, model, session);

        // 最大ページ番号計算
        int maxPageNum = (int) Math.ceil((double) form.getAllDataCount() / form.getItemsPerPage());
        form.setMaxPageNumber(maxPageNum);

        model.addAttribute("searchDetailForm", form); // これで十分なはず
        model.addAttribute("resultList", resultList);
        model.addAttribute("isInitial", false); // 初期表示フラグOFF
        model.addAttribute("itemsPerPage", ITEMS_PER_PAGE); // ページ毎の表示件数をモデルに追加
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

        String empID = "";
        if (session.getAttribute("role").equals("1")) {
            // 一般社員の場合、検索条件の従業員IDに自分の従業員IDをセット
            empID = String.valueOf(session.getAttribute("empId"));
        } else {
            // 一般社員以外の場合、検索条件の従業員IDに入力した値をセット
            empID = form.getEmpId();
        }

        // ページ番号を減算
        form.setCurrentPageNumber(form.getCurrentPageNumber() - 1);

        // 表示件数の計算
        commonUtil(form, model, session);

        // 表示用にページングされたリストを取得
        List<SearchDetailDto> resultList = searchDetailRepository.getResultList(
                String.valueOf(session.getAttribute("role")), empID,
                String.valueOf(session.getAttribute("teamId")), form.getStartDate(),
                form.getEndDate(), form.getLineId(), ITEMS_PER_PAGE, form.getCurrentPageNumber());

        model.addAttribute("startDate", form);
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

        String empID = "";
        if (session.getAttribute("role").equals("1")) {
            // 一般社員の場合、検索条件の従業員IDに自分の従業員IDをセット
            empID = String.valueOf(session.getAttribute("empId"));
        } else {
            // 一般社員以外の場合、検索条件の従業員IDに入力した値をセット
            empID = form.getEmpId();
        }

        // ページ番号を増算
        form.setCurrentPageNumber(form.getCurrentPageNumber() + 1);

        // 最後のページ判定
        boolean isLastPage = form.getCurrentPageNumber() * ITEMS_PER_PAGE >= form.getAllDataCount();
        form.setLastPage(isLastPage);

        // 表示件数の計算
        commonUtil(form, model, session);

        // 表示用にページングされたリストを取得
        List<SearchDetailDto> resultList = searchDetailRepository.getResultList(
                String.valueOf(session.getAttribute("role")), empID,
                String.valueOf(session.getAttribute("teamId")), form.getStartDate(),
                form.getEndDate(), form.getLineId(), ITEMS_PER_PAGE, form.getCurrentPageNumber());

        model.addAttribute("startDate", form);
        model.addAttribute("searchDetailForm", form);
        model.addAttribute("resultList", resultList);
        model.addAttribute("isInitial", false); // 初期表示フラグ
        return "search-detail"; // 結果を同じ画面に表示
    }

    /**
     * CSV出力ボタン押下時処理
     *
     * @param form form入力値
     * @param model モデル
     * @return 検索・参照画面(初期表示)
     */
    @PostMapping(value = "/search", params = "csvOutput")
    public void csvOutput(@ModelAttribute SearchDetailForm form, Model model,
            HttpServletResponse response) {
        // CSV出力
        List<SearchDetailDto> searchResultList = searchDetailRepository.getCsvOutput(form);

        try {
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=report.csv");

            PrintWriter writer = response.getWriter();

            // ヘッダー行
            writer.println("日付,社員名,理由,路線,内容");

            // データ行
            for (SearchDetailDto dto : searchResultList) {
                writer.println(dto.getRegisterDate() + "," + dto.getEmpLname() + " "
                        + dto.getEmpFname() + "," + dto.getDetail() + ",");
            }

            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("CSV 出力中にエラーが発生しました", e);
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

        // ページ毎の表示件数
        form.setItemsPerPage(ITEMS_PER_PAGE);

        // 表示件数（xx ~ xx 件)の計算
        int startIndex = (form.getCurrentPageNumber() - 1) * ITEMS_PER_PAGE + 1;
        int endIndex =
                Math.min(form.getCurrentPageNumber() * ITEMS_PER_PAGE, form.getAllDataCount());

        String viewingDataCount = startIndex + "〜" + endIndex + "件";
        form.setViewingDataCount(viewingDataCount);

    }

    // /**
    // * 日記アプリの一覧画面を表示
    // *
    // * @param model
    // * @return resources/templates/list.html
    // */
    // @GetMapping
    // public String diaryList(@ModelAttribute SearchDetailDto form, Model model) {
    // List<Diary> list = searchDetailService.findList(form);
    // model.addAttribute("list", list);
    // model.addAttribute("getForm", form);
    // return "list";
    // }

    // // 一覧表示するリストを取得する。
    // @GetMapping(value = {"/search-detail"})
    // public String getList(@ModelAttribute("SearchDetailDto") SearchDetailDto
    // form, Model model,
    // HttpSession session) {
    // model.addAttribute("title", "一覧");
    // // フラッシュスコープのSearchDetailDtoのプロパティにマップデータを付加する。
    // // フラッシュデータをそのままHTMLに渡すだけならSearchDetailDtoの引数は不要。
    // setMapItems(form);
    // setSearchKey(form, session);
    // // 職員データの取得
    // Map<String, List<Map<String, Object>>> data = new HashMap<>();
    // // 一覧表示のサービスの戻り値
    // // data.put("users", searchDetailService.select(form));
    // model.addAttribute("data", data);
    // return "list";
    // }

    // // 一覧が最初に呼ばれる時に実行されるメソッド。
    // // マップデータ（mapitems）をDTOに設定する。
    // @PostMapping(value = {"/search-detail"})
    // public String postList(SearchDetailDto form, BindingResult result,
    // RedirectAttributes redirectAttributes, HttpSession session) {
    // // 状態（複数）
    // form.setStateKeys(searchDetailService.getStateKeys());
    // // 雇用形態
    // form.setStatusKey(searchDetailService.getStatusKey());
    // //
    // session.setAttribute("StateKeys", form.getStateKeys());
    // session.setAttribute("StatusKey", form.getStatusKey());
    // redirectAttributes.addFlashAttribute("SearchDetailDto", form);

    // // formのmapstringが空の場合はmapitemsをサービスから作成する。
    // // formのmapstringが空でない場合はmapstringをマップに変換する。
    // @SuppressWarnings("unchecked")
    // private void setMapItems(SearchDetailDto form) {
    // if (form.getMapstring() == "") {
    // form.getMapitems().put("state", nameService.getState());
    // form.getMapitems().put("status", nameService.getStatus());
    // try {
    // form.setMapstring(mapper.writeValueAsString(form.getMapitems()));
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // } else {
    // try {
    // form.setMapitems((Map<String, Map<Integer, String>>) mapper
    // .readValue(form.getMapstring(), Map.class));
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // }

    // @GetMapping("/update")
    // public String getUpdate(@ModelAttribute("SearchDetailDto") SearchDetailDto
    // form, Model model)
    // {
    // model.addAttribute("title", "更新");
    // // HTMLのhidden属性のマップ文字列を取得してローカル変数に格納する。
    // String tmp = form.getMapstring();
    // // HTMLのコードからユーザー情報を取得する。マップ文字列が空の状態。
    // form = userService.select(form.getCode());
    // // ユーザー情報にマップ文字列を代入。
    // form.setMapstring(tmp);
    // // formのmapstringからformのmapitemsを作成する。
    // setMapItems(form);
    // model.addAttribute("SearchDetailDto", form);
    // return "edit";
    // }

    // @PostMapping("/update")
    // public String postUpdate(SearchDetailDto form, BindingResult result,
    // RedirectAttributes redirectAttributes) {
    // redirectAttributes.addFlashAttribute("SearchDetailDto", form);
    // return "redirect:/update";
    // }
}
