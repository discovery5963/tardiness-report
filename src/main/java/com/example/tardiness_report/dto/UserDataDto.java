package com.example.tardiness_report.dto;

public class UserDataDto {
    private String empID;
    private String departmentId;
    private String teamId;
    private String role;
    private String empLname;
    private String empFname;
    private String belong;
    private String password;        
    private String departmentName;
    private String teamName;

    public String getEmpID() {
        return empID;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public String getTeamId() {
        return teamId;
    }

    public String getRole() {
        return role;
    }

    public String getEmpLname() {
        return empLname;
    }

    public String getEmpFname() {
        return empFname;
    }

    public String getEelong() {
        return belong;
    }

    public String getPassword() {
        return password;
    }

    public String getPepartmentName() {
        return departmentName;
    }

    public String getTeamName() {
        return teamName;
    }




    //Setter
    public void setEmpID(String empID) {
        this.empID = empID;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setEmpLname(String empLname) {
        this.empLname = empLname;
    }

    public void setEmpFname(String empFname) {
        this.empFname = empFname;
    }

    public void setBelong(String belong) {
        this.belong = belong;
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
    
    
}
