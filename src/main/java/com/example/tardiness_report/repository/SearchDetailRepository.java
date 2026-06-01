package com.example.tardiness_report.repository;

import com.example.tardiness_report.dto.SearchDetailDto;
import com.example.tardiness_report.dto.SearchDetailForm;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// 検索・参照画面用マスタデータ取得リポジトリクラス

@Repository
@RequiredArgsConstructor
public class SearchDetailRepository {
    private final JdbcTemplate jdbcTemplate;

    /**
     * 指定した検索条件に紐づくレコードの総件数を取得する。
     *
     * @param role ログインユーザーの役職
     * @param empID 検索対象の従業員ID
     * @param teamId 検索対象のチームID
     * @param lineId 検索対象の路線ID
     * @param startDate 検索対象の開始日付（登録日）
     * @param endDate 検索対象の終了日付（登録日）
     * @return 検索結果の総件数
     * @throws org.springframework.dao.DataAccessException JDBC 操作に失敗した場合にスローされる
     */
    public int getAllListCount(String role, String empID, String teamId, String lineId,
            String startDate, String endDate) {
        // 検索SQL（全件検索状態）
        String sql = """
                SELECT
                    COUNT(*) AS total_count
                FROM
                    SEARCH_LIST_VIEW
                WHERE
                    1 = 1
                """;


        sql = setCriteria(sql, role, empID, teamId, lineId, startDate, endDate);

        int allCount = jdbcTemplate.queryForObject(sql, int.class);

        return allCount;
    }

    /**
     * 指定した従業員IDに紐づく遅刻理由の一覧をページングして取得する。
     *
     * @param role ログインユーザーの役職
     * @param empID 検索対象の従業員ID（null不可）
     * @param teamId 検索対象のチームID
     * @param startDate 検索対象の開始日付（登録日）。nullまたは空文字の場合は条件に含めない
     * @param endDate 検索対象の終了日付（登録日）。nullまたは空文字の場合は条件に含めない
     * @param lineId 検索対象の路線ID。nullまたは
     * @param limitCount 取得する最大件数（ページサイズ）
     * @param currentPageNumber 現在のページ番号（1から開始）
     * @return SearchDetailDto のリスト。該当なしの場合は空リストを返す。
     * @throws org.springframework.dao.DataAccessException JDBC 操作に失敗した場合にスローされる
     */
    public List<SearchDetailDto> getResultList(String role, String empID, String teamId,
            String startDate, String endDate, String lineId, int limitCount,
            int currentPageNumber) {

        LocalDate startDateLD = null;
        LocalDate endDateLD = null;

        // 値が設定されている場合は開始日付、終了日付をLocalDate型に変換
        if (startDate != null && !startDate.isEmpty()) {
            startDateLD = LocalDate.parse(startDate);
        }
        if (endDate != null && !endDate.isEmpty()) {
            endDateLD = LocalDate.parse(endDate);
        }

        // オフセットの計算
        int offsetValue = (currentPageNumber - 1) * limitCount;

        // SQLクエリの作成
        String sql = """
                SELECT
                    EMP_NAME,
                    REGISTER_DATE,
                    LATE_REASON_CD,
                    DETAIL,
                    LINE_NAME
                FROM
                    SEARCH_LIST_VIEW
                WHERE
                    1 = 1
                """;

        // SQLクエリに条件を追加
        sql = setCriteria(sql, role, empID, teamId, lineId, startDate, endDate);

        sql += " ORDER BY REGISTER_DATE ";
        sql += " LIMIT %d OFFSET %d".formatted(limitCount, offsetValue);

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        List<SearchDetailDto> result = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            SearchDetailDto dto = SearchDetailDto.builder().empName((String) row.get("EMP_NAME"))
                    .registerDate((Timestamp) row.get("REGISTER_DATE"))
                    .lateReasonCd((String) row.get("LATE_REASON_CD"))
                    .detail((String) row.get("DETAIL")).lineName((String) row.get("LINE_NAME"))
                    .build();

            result.add(dto);
        }

        return result;
    }

    /**
     * CSV出力するデータを取得する。（検索結果の全件を取得）
     *
     * @param form formで渡された検索条件
     * @throws org.springframework.dao.DataAccessException JDBC 操作に失敗した場合にスローされる
     */
    public List<SearchDetailDto> getCsvOutput(SearchDetailForm form) {

        // SQLクエリの作成
        // TODO SQLの修正が必要（formから検索条件を指定）
        String sql = String.format("""
                SELECT
                    EMP_NAME,
                    REGISTER_DATE,
                    LATE_REASON_CD,
                    DETAIL,
                    LINE_NAME
                FROM
                    SEARCH_LIST_VIEW
                WHERE
                    EMP_ID = '%s'
                ORDER BY
                    REGISTER_DATE
                """, form.getEmpId());

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        List<SearchDetailDto> result = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            SearchDetailDto dto = SearchDetailDto.builder().empName((String) row.get("EMP_NAME"))
                    .registerDate((Timestamp) row.get("REGISTER_DATE"))
                    .lateReasonCd((String) row.get("LATE_REASON_CD"))
                    .detail((String) row.get("DETAIL")).lineName((String) row.get("LINE_NAME"))
                    .build();
            result.add(dto);
        }

        return result;
    }

    /**
     * 指定した検索条件を基にSQLクエリに条件を追加する。
     *
     * @param sql 基底SQLクエリ
     * @param role ログインユーザーの役職
     * @param empID 検索対象の従業員ID
     * @param teamId 検索対象のチームID
     * @param lineId 検索対象の路線ID
     * @param startDate 検索対象の開始日付（登録日）
     * @param endDate 検索対象の終了日付（登録日）
     * @return 条件を追加したSQLクエリ
     */
    public String setCriteria(String sql, String role, String empID, String teamId, String lineId,
            String startDate, String endDate) {


        // 従業員IDが入力されている場合
        if ((empID != null && !empID.isEmpty())) {
            sql = sql + " AND EMP_ID = '" + empID + "'";
        }

        // 路線IDが入力されている場合
        if (lineId != null && !lineId.isEmpty()) {
            sql = sql + " AND LINE_ID = '" + lineId + "'";
        }

        // 開始日付、終了日付に値がある場合
        if ((startDate != null && !startDate.isEmpty())
                && (endDate != null && !endDate.isEmpty())) {
            // SQLに条件を追加
            sql = sql + "AND REGISTER_DATE BETWEEN '" + startDate + "' AND '" + endDate + "'";
        }

        // ｢遅刻理由｣検索処理(管理職付きの場合)
        // 'セッション.社員ID'に紐づく、社員マスタ.役職=2,3,4の場合(C以上MGR以下)
        if (role.equals("2") || role.equals("3") || role.equals("4")) {
            sql = sql + " AND TEAM_ID = '" + teamId + "'";
        }

        // 'セッション.社員ID'に紐づく、社員マスタ.役職=5(MGR)の場合
        if (role.equals("5")) {
            sql = sql + " AND (TEAM_ID = '" + teamId + "' OR ROLE != '1')";
        }
        return sql;
    }


}
