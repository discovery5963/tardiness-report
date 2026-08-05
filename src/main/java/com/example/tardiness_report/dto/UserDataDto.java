package com.example.tardiness_report.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDataDto {
    /** 社員ID */
    private String empId;
    /** 部署ID */
    private String departmentId;
    /** チームID */
    private String teamId;
    /** 社員姓 */
    private String empFname;
    /** 社員名 */
    private String empLname;
    /** 社員名（フルネーム） */
    private String empName;
    /** 所属 */
    private String belong;
    /** パスワード */
    private String password;
    /** 部署名 */
    private String departmentName;
    /** チーム名 */
    private String teamName;
    /** ユニット番号 */
    private String unitNo;
    /** 役職名 */
    private String roleName;
    /** 役職ID */
    private String role;
    
}
