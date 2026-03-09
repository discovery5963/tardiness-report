package com.example.tardiness_report.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDataDto {
    private String empId;
    private String departmentId;
    private String teamId;
    private String empLname;
    private String empFname;
    private String belong;
    private String password;
    private String departmentName;
    private String teamName;
    private String unitNo;
    private String roleName;
    private String role;
    

    public String getEmpId() {
        return empId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public String getTeamId() {
        return teamId;
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

    public String getDepartmentName() {
        return departmentName;
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

    public String getBelong() {
        return belong;
    }

    public String getRole() {
        return role;
    }

    //Setter
    public void setEmpID(String empId) {
        this.empId = empId;
    }
    
    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
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

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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

    public void setBelong(String belong) {
        this.belong = belong;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
}
