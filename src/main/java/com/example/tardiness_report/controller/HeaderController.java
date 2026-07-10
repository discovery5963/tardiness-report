package com.example.tardiness_report.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

@Controller
public class HeaderController {
    /**
     * メニューへ戻るボタン押下時
     * 
     * @param model セッション情報を格納するmodel
     * @param session セッション情報
     * @return メニュー画面
     */
    @GetMapping("/menuReturn")
    public String menuReturn(Model model, HttpSession session) {

        session.setAttribute("inputDetail", null);
        session.setAttribute("lateReasonId", null);
        // メニュー画面へ遷移
        return "menu";
    }

    /**
     * ログアウトボタン押下時
     *
     * @param session セッション情報
     * @return メニュー画面
     */
    @PostMapping("/logout")
    public String logout(HttpSession session) {

        // セッションをクリアする
        session.invalidate();

        // ログイン画面へ遷移
        return "login";
    }

    /**
     * タブを閉じた際の処理(ログアウトボタンを押下せずに落とした場合の処理)
     *
     * @param session セッション情報
     * @return メニュー画面
     */
    @PostMapping("/forced_logout")
    public void forcedLogout(HttpSession session) {

        // セッションをクリアする
        session.invalidate();

    }
}
