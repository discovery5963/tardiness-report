package com.example.tardiness_report.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import com.example.tardiness_report.dto.SearchDetailForm;
import com.example.tardiness_report.dto.UserDataDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchDetailService {

    private final JdbcTemplate jdbcTemplate;

    /**
     * 遅刻情報検索
     *
     * @param form
     * @return 検索結果一覧リスト
     */
    public List<SearchDetailForm> findListOld(SearchDetailForm form) {
        List<SearchDetailForm> dummyDataList = new ArrayList<>();
        form.setRegisteredDate("2024-06-02");
        form.setEmpLname("山田太郎");
        form.setLateReason("寝坊");
        form.setLineName("中央線");
        form.setDetail("ダミーデータです");

        dummyDataList.add(form);
        // dummyDataList.add("みんなのJava");
        // return dao.findList(form);
        return dummyDataList;
    }

    /**
     * 遅刻情報一覧取得
     *
     * @param form
     * @return 検索結果一覧リスト
     */
    public List<UserDataDto> findList(SearchDetailForm form) {

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
                WHERE emp.emp_id = '""" + form.getEmpId() + "'";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        List<UserDataDto> result = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            UserDataDto dto = UserDataDto.builder().empId((String) row.get("emp_id"))
                    .departmentId((String) row.get("department_id"))
                    .teamId((String) row.get("team_id")).empLname((String) row.get("emp_lname"))
                    .empFname((String) row.get("emp_fname")).belong((String) row.get("belong"))
                    .password((String) row.get("password"))
                    .departmentName((String) row.get("department_name"))
                    .teamName((String) row.get("team_name")).role((String) row.get("role")).build();

            result.add(dto);
        }

        return result;
    }



}
