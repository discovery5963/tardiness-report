package com.example.tardiness_report.repository;

import com.example.tardiness_report.dto.SearchDetailDto;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// 検索・参照画面用マスタデータ取得リポジトリクラス

@Repository
@RequiredArgsConstructor
public class SearchDetailRepository {
    private final JdbcTemplate jdbcTemplate;

    /**
     * 指定した従業員IDに紐づくレコードの総件数を取得する。
     *
     * @param empID 検索対象の従業員ID（null不可）
     * @return 検索結果の総件数
     * @throws org.springframework.dao.DataAccessException JDBC 操作に失敗した場合にスローされる
     */
    public int getAllListCount(String empID) {

        String sql =
                """
                SELECT
                    COUNT(*) AS total_count
                FROM
                    LATE_REASON LR
                    INNER JOIN EMPLOYEE_MST EMP
                        ON LR.EMP_ID = EMP.EMP_ID
                    INNER JOIN LINE_MST LM
                        ON LR.LINE_ID = LM.LINE_ID
                WHERE
                    LR.EMP_ID = ?
                """;

        return jdbcTemplate.queryForObject(sql, Integer.class, empID);
    }

    /**
     * 指定した従業員IDに紐づく遅刻理由の一覧をページングして取得する。
     *
     * @param empID 検索対象の従業員ID（null不可）
     * @param limitCount 取得する最大件数（ページサイズ）
     * @param currentPageNumber 現在のページ番号（1から開始）
     * @return SearchDetailDto のリスト。該当なしの場合は空リストを返す。
     * @throws org.springframework.dao.DataAccessException JDBC 操作に失敗した場合にスローされる
     */
    public List<SearchDetailDto> getResultList(
            String empID, int limitCount, int currentPageNumber) {

        // オフセットの計算
        int offsetValue = (currentPageNumber - 1) * limitCount;
        // SQLクエリの作成
        String sql =
                String.format(
                        """
                        SELECT
                            EMP.EMP_LNAME,
                            EMP.EMP_FNAME,
                            LR.REGISTER_DATE,
                            LR.LATE_REASON_CD,
                            LR.DETAIL,
                            LM.LINE_NAME
                        FROM
                            LATE_REASON LR
                            INNER JOIN EMPLOYEE_MST EMP
                                ON LR.EMP_ID = EMP.EMP_ID
                            INNER JOIN LINE_MST LM
                                ON LR.LINE_ID = LM.LINE_ID
                        WHERE
                            LR.EMP_ID = '%s'
                        ORDER BY
                            REGISTER_DATE
                        LIMIT %d OFFSET %d
                        """,
                        empID, limitCount, offsetValue);

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        List<SearchDetailDto> result = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            SearchDetailDto dto =
                    SearchDetailDto.builder()
                            .empLname((String) row.get("EMP_LNAME"))
                            .empFname((String) row.get("EMP_FNAME"))
                            .registerDate((Timestamp) row.get("REGISTER_DATE"))
                            .lateReasonCd((String) row.get("LATE_REASON_CD"))
                            .detail((String) row.get("DETAIL"))
                            .lineName((String) row.get("LINE_NAME"))
                            .build();

            result.add(dto);
        }

        return result;
    }
}
