package com.example.tardiness_report.dto;

import lombok.Builder;
import lombok.Data;


// 遅刻理由DTO
@Data
@Builder
public class ReportDataDto {
    private String lateReasonId;
    private String empId;
    private String lateReasonCd;
    private String resisterContentCd;
    private String lineId;
    private String detail;
    private String updateDate;
    private String resisterDate;

    // getter
    public String getLateReasonId() {
        return lateReasonId;
    }
    public String getEmpId() {
        return empId;
    }
    public String getLateReasonCd() {
        return lateReasonCd;
    }
    public String getResisterContentCd() {
        return resisterContentCd;
    }
    public String getLineId() {
        return lineId;
    }
    public String getDetail() {
        return detail;
    }
    public String getUpdateDate() {
        return updateDate;
    }
    public String getResisterDate() {
        return resisterDate;
    }

    // Setter
    public void setLateReasonId(String lateReasonId) {
        this.lateReasonId = lateReasonId;
    }
    public void setEmpID(String empId) {
        this.empId = empId;
    }
    public void setLateReasonCd(String lateReasonCd) {
        this.lateReasonCd = lateReasonCd;
    }
    public void setResisterContentCd(String resisterContentCd) {
        this.resisterContentCd = resisterContentCd;
    }
    public void setLineId(String lineId) {
        this.lineId = lineId;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
    public void setResisterDate(String resisterDate) {
        this.resisterDate = resisterDate;
    }
}
