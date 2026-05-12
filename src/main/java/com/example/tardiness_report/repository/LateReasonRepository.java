package com.example.tardiness_report.repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.example.tardiness_report.dto.ReportDataDto;

import lombok.RequiredArgsConstructor;


// 遅刻理由TBL操作リポジトリクラス

@Repository
@RequiredArgsConstructor
public class LateReasonRepository {
    private final JdbcTemplate jdbcTemplate;

    // 遅刻理由IDをキーとしたレコード取得処理
    public List<ReportDataDto> getLateReasonFromlateReasonId(String lateReasonId){
    String sql = """
            SELECT
                T1.late_reason_cd,
                T1.register_content_cd,
                T1.line_id,
                T1.detail
            FROM late_reason T1
            WHERE T1.late_reason_id = ?
            """;

        long id = Long.parseLong(lateReasonId);

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, id);
        List<ReportDataDto> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            ReportDataDto dto = ReportDataDto.builder()
                .empId((String) row.get("late_reason_cd"))
                .resisterContentCd((String) row.get("register_content_cd"))
                .lineId((String) row.get("line_id"))
                .detail((String) row.get("detail"))
                .build();

            result.add(dto);
        }
        return result;
    }

    // 社員IDをキーとしたレコード取得処理
    public List<ReportDataDto> getLateReason(String empId){
    String sql = """
            SELECT
                T1.late_reason_cd,
                T1.register_content_cd,
                T1.line_id,
                T1.detail
            FROM late_reason T1
            WHERE T1.emp_id = ?
            AND T1.register_date >= CURRENT_DATE
            """;


        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, empId);
        List<ReportDataDto> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            ReportDataDto dto = ReportDataDto.builder()
                .empId((String) row.get("late_reason_cd"))
                .resisterContentCd((String) row.get("register_content_cd"))
                .lineId((String) row.get("line_id"))
                .detail((String) row.get("detail"))
                .build();

            result.add(dto);
        }
        return result;
    }
    // 遅刻理由コードの最大値取得処理
    public long getLateReasonId() {
        String sql = "SELECT COALESCE(MAX(late_reason_id), 0) + 1 FROM late_reason";
        long result = Long.parseLong(jdbcTemplate.queryForObject(sql, String.class));
        return result;
    }


public void insertLateReason(String empId, String lateReasonCd, String registerContentCd, String lineId, String detail){

    long lateReasonId = getLateReasonId();

    String sql = """
        INSERT INTO late_reason(
            LATE_REASON_ID,
            EMP_ID,
            LATE_REASON_CD,
            REGISTER_CONTENT_CD,
            LINE_ID,
            DETAIL,
            UPDATE_DATE,
            REGISTER_DATE
        )
        VALUES(?, ?, ?, ?, ?, ?, NOW(), NOW())
        """;

    jdbcTemplate.update(
        sql,
        lateReasonId,       // 1
        empId,              // 2
        lateReasonCd,       // 3
        registerContentCd,  // 4
        lineId,             // 5
        detail              // 6
    );
}
    
    // レコード更新処理
    public void updateLateReason(String lateReasonCd, String registerContentCd, String lineId, String detail, String lateReasonId){
    String sql = """
            UPDATE LATE_REASON T1
            SET T1.LATE_REASON_CD = ?,
            	T1.REGISTER_CONTENT_CD = ?.
            	T1.LINE_ID = ?,
            	T1.DETAIL = ?,
            	T1.UPDATE_DATE = to_char(NOW(), 'YYYY-MM-DD HH24:MI:SS')
            WHERE LATE_REASON_ID = ?
            """;
            jdbcTemplate.queryForList(sql, lateReasonCd, registerContentCd, lineId, detail, lateReasonId);
    }
}
