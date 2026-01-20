package com.example.tardiness_report.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.example.tardiness_report.dto.UserDataDto;
import lombok.RequiredArgsConstructor;


// 社員情報取得用リポジトリクラス

@Repository
@RequiredArgsConstructor
public class EmployeeMstRepository {
    private final JdbcTemplate jdbcTemplate;

    // 社員情報取得
    public List<UserDataDto> getEmpData(String empID) {
        // TODO
        // 検索対象の社員ID（実装後は引数で検索キーを取得して指定）
        // String empID = "0000000011";

        String sql = """
            SELECT 
                EM.EMP_ID
                ,EM.EMP_LNAME
                ,EM.EMP_FNAME
                ,PM.PASSWORD
                ,TM.TEAM_NAME
                ,TM.UNIT_NO
                ,RM.ROLE_NAME
            FROM EMPLOYEE_MST EM
            LEFT JOIN PASSWORD_MST PM
                ON EM.EMP_ID = PM.EMP_ID
            LEFT JOIN TEAM_MST TM
                ON EM.TEAM_ID = TM.TEAM_ID
            LEFT JOIN ROLE_MST RM
                ON EM.ROLE = RM.ROLE
            WHERE PM.EMP_ID = '""" + empID + "'";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        List<UserDataDto> result = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            UserDataDto dto = UserDataDto.builder()
                .empId((String) row.get("emp_id"))
                .empLname((String) row.get("emp_lname"))
                .empFname((String) row.get("emp_fname"))
                .password((String) row.get("password"))
                .teamName((String) row.get("team_name"))
                .roleName((String) row.get("role_name"))
                .build();

            result.add(dto);
        }

        return result;
    }
    
}
