package com.employeepayrollservice;

import java.sql.*;
import java.time.LocalDate;

public class EmployeePayrollDBServiceERD {
    private static EmployeePayrollDBServiceERD employeePayrollDBServiceERD;

    private EmployeePayrollDBServiceERD() {
    }

    public static EmployeePayrollDBServiceERD getInstance() {
        if (employeePayrollDBServiceERD == null) {
            employeePayrollDBServiceERD = new EmployeePayrollDBServiceERD();
        }
        return employeePayrollDBServiceERD;
    }

    @SuppressWarnings("finally")
    public EmployeePayrollData addEmployeeToPayroll(String name, double salary, LocalDate startDate, String gender)
            throws PayrollServiceException {
        int employeeId = -1;
        Connection connection = null;
        EmployeePayrollData employeePayrollData = null;
        try {
            connection = EmployeePayrollDatabaseService.getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new PayrollServiceException(e.getMessage(), PayrollServiceException.ExceptionType.CONNECTION_PROBLEM);
        }
        try (Statement statement = connection.createStatement()) {
            String sql = String.format(
                    "insert into employee_payroll (name,gender,salary,start)" + "values ('%s', '%s', '%s', '%s')", name,
                    gender, salary, Date.valueOf(startDate));
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next())
                    employeeId = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            throw new PayrollServiceException(e.getMessage(), PayrollServiceException.ExceptionType.INSERTION_PROBLEM);
        }

        try (Statement statement = connection.createStatement()) {
            double Deduction = salary * 0.2;
            double TaxablePay = salary - Deduction;
            double IncomeTax = TaxablePay * 0.1;
            double NetPAy = salary - IncomeTax;
            String sql = String.format(
                    "insert into payroll_details(empId, BasicPay, Deduction, TaxablePay, IncomeTax, NetPAy) values "
                            + "('%s', '%s', '%s', '%s', '%s', '%s')",
                    employeeId, salary, Deduction, TaxablePay, IncomeTax, NetPAy);
            int rowAffected = statement.executeUpdate(sql);
            if (rowAffected == 1) {
                employeePayrollData = new EmployeePayrollData(employeeId, name, salary, startDate);
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
                return employeePayrollData;
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            throw new PayrollServiceException(e.getMessage(), PayrollServiceException.ExceptionType.INSERTION_PROBLEM);
        }
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Statement statement = connection.createStatement()) {
            int DeptId = 201;
            String DeptName = "Finance";
            String sql = String.format("insert into Department(DeptId,DeptName) values('%s','%s')", DeptId, DeptName);
            int rowAffected = statement.executeUpdate(sql);
            if (rowAffected == 0)
                throw new PayrollServiceException("insertion into department table is unsuccessful !!!",
                        PayrollServiceException.ExceptionType.INSERTION_PROBLEM);
        } catch (PayrollServiceException e1) {
            System.out.println(e1);
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            throw new PayrollServiceException(e.getMessage(),
                    PayrollServiceException.ExceptionType.INSERTION_PROBLEM);
        }
        try (Statement statement = connection.createStatement()) {
            int DeptId1 = 203;
            String sql1 = String.format("insert into employee_dept(DeptId,employeeId) values('%s','%s')", employeeId, DeptId1);
            statement.executeUpdate(sql1);
            int DeptId = 202;
            String sql = String.format("insert into employee_dept(DeptId,employeeId) values('%s','%s')", employeeId, DeptId);
            int rowAffected1 = statement.executeUpdate(sql);
            if (rowAffected1 == 1) {
                employeePayrollData = new EmployeePayrollData(employeeId, name, salary, startDate);
            }
            if (rowAffected1 == 0)
                throw new PayrollServiceException("Insertion Problem !!!",
                        PayrollServiceException.ExceptionType.INSERTION_PROBLEM);
        } catch (PayrollServiceException e1) {
            System.out.println(e1);
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            throw new PayrollServiceException("Insertion problem !!!",
                    PayrollServiceException.ExceptionType.INSERTION_PROBLEM);
        }

        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new PayrollServiceException(e.getMessage(),
                            PayrollServiceException.ExceptionType.CONNECTION_PROBLEM);
                }
           return employeePayrollData;
        }
    }
    public int removeEmployee(String name) {
        try (Connection connection = EmployeePayrollDatabaseService.getConnection();) {
            String sql = "update employee_payroll set is_active=? where name=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBoolean(1, false);
            preparedStatement.setString(2, name);
            int status = preparedStatement.executeUpdate();
            return status;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}