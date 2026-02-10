package com.example.tardiness_report.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.example.tardiness_report.dto.LineMstDto;
import lombok.RequiredArgsConstructor;


// 路線マスタ取得用リポジトリクラス

@Repository
@RequiredArgsConstructor
public class LineMstRepository {
    private final JdbcTemplate jdbcTemplate;

    // 路線マスタ情報全取得
    public List<LineMstDto> getAllLineMstData() {

        String sql = """
                SELECT
                    l.line_id,
                    l.line_name
                FROM line_mst l
                """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        List<LineMstDto> result = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            LineMstDto dto = LineMstDto.builder().lineId((String) row.get("line_id"))
                    .lineName((String) row.get("line_name")).build();

            result.add(dto);
        }

        return result;
    }

}
