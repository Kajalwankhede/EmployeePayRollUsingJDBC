package com.employeepayrollservice;

import java.time.LocalDate;

public class EmployeePayrollData {
    private int id;
     String name;
     double salary;
    private LocalDate start;
    private String[] DeptName;
    private char gender;
    public EmployeePayrollData(int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }
    public EmployeePayrollData(int id, String name, double salary, LocalDate start) {
        this(id, name, salary);
        this.start = start;
    }
    public EmployeePayrollData(int id, String name, double salary, LocalDate start,char gender,String[] DeptName) {
        this(id, name, salary,start);
        this.gender=gender;
        this.DeptName=DeptName;
    }



    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EmployeePayrollData other = (EmployeePayrollData) obj;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (Double.doubleToLongBits(salary) != Double.doubleToLongBits(other.salary))
            return false;
        if (start == null) {
            if (other.start != null)
                return false;
        } else if (!start.equals(other.start))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "EmployeePayrollData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", start=" + start +
                '}';
    }
}
