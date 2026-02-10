package com.example.tardiness_report.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
/**
 * 路線マスタDTO
 */
public class LineMstDto {

    /** 路線ID */
    private String lineId;
    /** 路線名 */
    private String lineName;

}
