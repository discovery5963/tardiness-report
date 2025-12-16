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
    public List<UserDataDto> getEmpData() {
        // TODO
        // 検索対象の社員ID（実装後は引数で検索キーを取得して指定）
        String empID = "0000000011";

        String sql = """
            SELECT
                emp.emp_id,
                emp.department_id,
                emp.team_id,
                emp.role,
                emp.emp_lname,
                emp.emp_fname,
                emp.belong,
                pass.password,
                dep.department_name,
                team.team_name
            FROM employee_mst emp
            INNER JOIN password_mst pass
            ON emp.emp_id = pass.emp_id
            INNER JOIN department_mst dep
            ON emp.department_id = dep.department_id
            LEFT JOIN TardinessReport.team_mst team
            ON emp.team_id = team.team_id
            WHERE emp.emp_id = '""" + empID + "'";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        List<UserDataDto> result = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            UserDataDto dto = UserDataDto.builder()
                .empId((String) row.get("emp_id"))
                .departmentId((String) row.get("department_id"))
                .teamId((String) row.get("team_id"))
                .empLname((String) row.get("emp_lname"))
                .empFname((String) row.get("emp_fname"))
                .belong((String) row.get("belong"))
                .password((String) row.get("password"))
                .departmentName((String) row.get("department_name"))
                .teamName((String) row.get("team_name"))
                .role((String) row.get("role"))
                .build();

            result.add(dto);
        }

        return result;
    }
    
}
