package com.example.tardiness_report.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDataDto {
    private String empId;
    private String empLname;
    private String empFname;
    private String password;
    private String teamName;
    private String unitNo;
    private String roleName;

    public String getEmpId() {
        return empId;
    }

    public String getEmpLname() {
        return empLname;
    }

    public String getEmpFname() {
        return empFname;
    }

    public String getPassword() {
        return password;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public String getRoleName() {
        return roleName;
    }

    //Setter
    public void setEmpID(String empId) {
        this.empId = empId;
    }

    public void setEmpLname(String empLname) {
        this.empLname = empLname;
    }

    public void setEmpFname(String empFname) {
        this.empFname = empFname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
}
