package com.employeepayrollservice;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDatabaseService {
    public class EmployeePayrollDBService {
        private Connection getConnection() throws SQLException {
            String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSl=false";
            String userName = "root";
            String password = "Kajal@123#";
            Connection connection;
            System.out.println("connecting to the database:" + jdbcURL);
            connection = DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("connection is successful...." + connection);
            return connection;
        }

        public List<EmployeePayrollData> readData() throws PayrollServiceException {
            String sql = "select * from employee_payroll";
            List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
            try {
                Connection connection = this.getConnection();
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(sql);
                while (result.next()) {
                    int id = result.getInt("id");
                    String name = result.getString("name");
                    Double salary = result.getDouble("salary");
                    LocalDate startDate = result.getDate("start").toLocalDate();
                    employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
                }
            } catch (SQLException e) {
                throw new PayrollServiceException(e.getMessage(), PayrollServiceException.ExceptionType.RETRIEVAL_PROBLEM);
            }
            return employeePayrollList;
        }
    }
}