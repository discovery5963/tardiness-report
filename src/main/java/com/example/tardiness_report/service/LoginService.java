package com.example.tardiness_report.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import com.example.tardiness_report.dto.UserDataDto;
import com.example.tardiness_report.repository.EmployeeMstRepository;
import lombok.Data;

@Service
public class LoginService {

    @Data // Lombokでgetterやsetterを自動生成
    public class Neko {
        private String empId;
        private String password;
        private int age;
    }

    @Autowired
    private EmployeeMstRepository employeeMstRepository;

    private String ERRORMESSAGE = "errorMessage";

    private String ERROR = "社員IDかパスワードが違っております";

    // ユーザー情報の取得
    public List<UserDataDto> fetchEmployees(String empId) {
        return employeeMstRepository.getEmpData(empId);
    }

    // ユーザーIDとパスワードの入力チェック
    public boolean inputDataCheck(List<UserDataDto> userDataList, String password, Model model) {

        // 社員情報のリストの中身やパスワードが空やnullの場合はエラーがメッセージを追加しfalseで返す。
        if (userDataList.isEmpty() || userDataList == null  || password.isEmpty() || password == null) {
            model.addAttribute(ERRORMESSAGE, ERROR);
            return false;
        }

        String dbPassword = userDataList.get(0).getPassword();

        // 社員情報のパスワードと取得してきたパスワードが違う場合はエラーがメッセージを追加しfalseで返す。
        if (!password.equals(dbPassword)) {
            model.addAttribute(ERRORMESSAGE, ERROR);
            return false;
        }
        
        // チェックOK
        return true;
    }

}
