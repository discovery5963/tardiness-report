package com.example.tardiness_report.repository;

import com.example.tardiness_report.dto.SearchDetailDto;
import com.example.tardiness_report.dto.SearchDetailForm;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
     * @param role ログインユーザーの役職
     * @param empID 検索対象の従業員ID
     * @param lineId 検索対象の路線ID
     * @param startDate 検索対象の開始日付（登録日）
     * @param endDate 検索対象の終了日付（登録日）
     * @return 検索結果の総件数
     * @throws org.springframework.dao.DataAccessException JDBC 操作に失敗した場合にスローされる
     */
    public int getAllListCount(
            String role, String empID, String lineId, String startDate, String endDate) {
        // 検索SQL（全件検索状態）
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
                """;
        
        // SQL条件数カウント
        int count = 0;

        // 従業員IDが入力されている場合
        if ((empID != null && !empID.isEmpty())) {
            sql = sql + " WHERE LR.EMP_ID =" + empID + "'";
            count = count + 1;
        }

        // 路線IDが入力されている場合
        if (lineId != null && !lineId.isEmpty()) {
            if (count == 0) {
                // 既に指定された条件がない場合
                sql = sql + " WHERE LR.LINE_ID = '" + lineId +"'";    
            } else {
                //既に指定された条件がある場合
                sql = sql + " AND LR.LINE_ID = '" + lineId +"'";
            }
            count = count + 1;
        }


        // 開始日付、終了日付に値がある場合
        if ((startDate != null && !startDate.isEmpty()) && (endDate != null && !endDate.isEmpty())) {
            // 開始日付、終了日付の値確認と日付型への変換
            // String型からLocalDate型に変換
            LocalDate startDateLD = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            LocalDate endDateLD = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            // SQLに条件を追加
            sql = sql + "AND "; //TODO 記載途中
        }


        return jdbcTemplate.queryForObject(
                sql,
                new Object[] {empID, lineId, startDate, endDate},
                new int[] {Types.VARCHAR, Types.VARCHAR, Types.DATE, Types.DATE},
                Integer.class);
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
            String empID,
            String startDate,
            String endDate,
            int limitCount,
            int currentPageNumber) {

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
                            LR.EMP_ID = ?
                            OR LR.LINE_ID = ?
                            OR LR.REGISTER_DATE BETWEEN ? AND ?
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

    /**
     * CSV出力するデータを取得する。（検索結果の全件を取得）
     *
     * @param form formで渡された検索条件
     * @return SearchDetailDto のリスト。該当なしの場合は空リストを返す。
     * @throws org.springframework.dao.DataAccessException JDBC 操作に失敗した場合にスローされる
     */
    public List<SearchDetailDto> getCsvOutputList
        (SearchDetailForm form, HttpServletResponse response) throws IOException {

        // SQLクエリの作成
        String sql = String.format(
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
                        """,
                        form.getEmpId());
        
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

        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=report.csv");

        PrintWriter writer = response.getWriter();

        // ヘッダー行
        writer.println("日付,社員名,理由,路線,内容");

        // データ行
        for (SearchDetailDto dto : result) {
            // TODO 文字列結合をStringBuilderに置き換える。以下の出力項目の内容はまだ修正途中。
            writer.println(dto.getRegisterDate() + "," + dto.getEmpLname() + " " + dto.getEmpFname() + "," + dto.getDetail() + ",");
        }

        writer.flush();

        return result;
    }
}
