package com.example.tardiness_report.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.tardiness_report.dto.SearchDetailDto;
import com.example.tardiness_report.service.LoginService;
import com.example.tardiness_report.service.SearchDetailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
public class SearchDetailController {

    private final SearchDetailService searchDetailService;

    public SearchDetailController(SearchDetailService searchDetailService) {
        this.searchDetailService = searchDetailService;
    }

    @ModelAttribute("searchDetailDto")
    public SearchDetailDto populateSearchDetailDto() {
        return new SearchDetailDto();
    }

    /**
     * 検索・参照画面を初期表示
     *
     * @param model モデル
     * @return 検索・参照画面(初期表示)
     */
    @GetMapping("/search")
    public String showDetail(Model model) {
        model.addAttribute("searchDetailDto", new SearchDetailDto());
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
    @PostMapping("/search")
    public String doSearch(@ModelAttribute SearchDetailDto form, Model model) {
        List<SearchDetailDto> results = searchDetailService.findList(form);
        form.setSearchedList(results); // 検索結果をDTOにセット

        model.addAttribute("searchDetailDto", form);
        model.addAttribute("results", results);
        model.addAttribute("isInitial", false); // 初期表示フラグ
        return "search-detail"; // 結果を同じ画面に表示
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
    // public String getList(@ModelAttribute("SearchDetailDto") SearchDetailDto form, Model model,
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
    // public String getUpdate(@ModelAttribute("SearchDetailDto") SearchDetailDto form, Model model)
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
