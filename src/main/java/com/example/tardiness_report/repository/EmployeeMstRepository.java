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
        // 検索対象の社員ID（実装後は引数で検索キーを取得して指定）

        String sql = """
                SELECT
                    emp.emp_id,
                    emp.emp_lname,
                    emp.emp_fname,
                    emp.team_id,
                    pass.password,
                    team.team_name,
                    team.unit_no,
                    role.role,
                    role.role_name
                FROM employee_mst emp
                INNER JOIN password_mst pass
                    ON emp.emp_id = pass.emp_id
                LEFT JOIN TardinessReport.team_mst team
                    ON emp.team_id = team.team_id
                LEFT JOIN TardinessReport.role_mst role
                    ON emp.role = role.role
                WHERE emp.emp_id = '""" + empID + "'";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        List<UserDataDto> result = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            UserDataDto dto = UserDataDto.builder().empId((String) row.get("emp_id"))
                    .empLname((String) row.get("emp_lname")).empFname((String) row.get("emp_fname"))
                    .teamId((String) row.get("team_id")).password((String) row.get("password"))
                    .teamName((String) row.get("team_name")).unitNo((String) row.get("unit_no"))
                    .role((String) row.get("role")).roleName((String) row.get("role_name")).build();
            result.add(dto);
        }

        return result;
    }

    /**
     * 検索・参照画面用社員情報取得
     *
     * @param unitNo ログインユーザーのユニットNO
     * @param teamId ログインユーザーのチームID
     * @param role ログインユーザーの役職
     */
    public List<UserDataDto> getEmpDataForSearchDetail(String unitNo, String teamId, String role) {

        String sql = """
                SELECT
                    EMP_ID,
                    EMP_NAME
                FROM 
                    SEARCH_LIST_VIEW
                WHERE
                    1 = 1
                """;

        // 「社員名称｣検索処理((管理職付きの場合のみ実行する。)
        // 下記条件に合致しない社員は全社員参照可能とする。
        
        // 'セッション.社員ID'に紐づく、社員マスタ.役職=2,3の場合(C/LD)の場合
        // 自チーム（自セクション）を参照できる。
        if (role.equals("2") || role.equals("3")) {
            sql = sql + " AND TEAM_ID = '" + teamId + "'";
            // 'セッション.社員ID'に紐づく、社員マスタ.役職=4(AMG)の場合
            // 自ユニットを参照できる。
        } else if (role.equals("4")) {
            sql = sql + " AND  UNIT_NO = '" + unitNo + "'";
            // 'セッション.社員ID'に紐づく、社員マスタ.役職=5(MGR)の場合
            // 管理職者(自ユニット以外も含む)または自ユニットを参照できる。
        } else if (role.equals("5")) {
            sql = sql + " AND (ROLE NOT IN ('1') OR UNIT_NO = '" + unitNo + "')";
        }

        sql += " ORDER BY ROLE DESC, EMP_NAME ASC ";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        List<UserDataDto> result = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            UserDataDto dto = UserDataDto.builder().empId((String) row.get("EMP_ID"))
                    .empName((String) row.get("EMP_NAME"))
                    .build();
            result.add(dto);
        }

        return result;
    }


}
