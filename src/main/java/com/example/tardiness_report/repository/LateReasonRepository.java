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
    /*
    LocalDateTime now = LocalDateTime.now();
    // フォーマットを指定
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    // 文字列に変換
    String formattedDate = now.format(formatter);
    */
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

        // 遅刻理由IDをキーとしたレコード取得処理
    public List<ReportDataDto> getLateReason(String empId){
    /*
    LocalDateTime now = LocalDateTime.now();
    // フォーマットを指定
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    // 文字列に変換
    String formattedDate = now.format(formatter);
    */
    String sql = """
            SELECT
                T1.late_reason_cd,
                T1.register_content_cd,
                T1.line_id,
                T1.detail
            FROM late_reason T1
            WHERE T1.rmp_id = ?
            AND T1.register_date >= CURRENT_DATE
            """;

        long id = Long.parseLong(empId);

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

}
