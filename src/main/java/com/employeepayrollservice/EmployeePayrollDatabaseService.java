package com.employeepayrollservice;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDatabaseService {
    private PreparedStatement preparedStatement;
    private static EmployeePayrollDatabaseService employeePayrollDatabaseService;

    private class EmployeePayrollDBService {
    }
        private Connection getConnection() throws SQLException {
            String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSl=false";
            String userName = "root";
            String password = "Kajal@123";
            Connection connection;
            System.out.println("connecting to the database:" + jdbcURL);
            connection = DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("connection is successful...." + connection);
            return connection;
        }

//        public List<EmployeePayrollData> readData() throws PayrollServiceException {
//            String sql = "select * from employee_payroll";
            public static EmployeePayrollDatabaseService getInstance() {
                if(employeePayrollDatabaseService == null)
                    employeePayrollDatabaseService = new EmployeePayrollDatabaseService();
                return employeePayrollDatabaseService;
            }
    public List<EmployeePayrollData> getEmployeePayrollData(String name) {
        List<EmployeePayrollData> employeePayrollList = null;
        if(this.preparedStatement == null)
            this.prepareStatementForEmployeeData();
        try {
            preparedStatement.setString(1,name);
            ResultSet resultSet = preparedStatement.executeQuery();
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet result) {
            List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try {
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                Double salary = result.getDouble("salary");
                LocalDate startDate = result.getDate("start").toLocalDate();
                employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
                return employeePayrollList;
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void prepareStatementForEmployeeData() {
        try {
            Connection connection = this.getConnection();
            String sql = "select * from employee_payroll where name = ?";
            preparedStatement = connection.prepareStatement(sql);
        }catch(SQLException e) {
            e.printStackTrace();
        }

    }
    public List<EmployeePayrollData> readData() throws PayrollServiceException {
        String sql = "select * from employee_payroll";
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try (Connection connection = this.getConnection();) {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
                while (result.next()) {
                    int id = result.getInt("id");
                    String name = result.getString("name");
                    Double salary = result.getDouble("salary");
                    LocalDate startDate = result.getDate("start").toLocalDate();
                    employeePayrollList.add(new EmployeePayrollData(id,name,salary,startDate));
                }
            } catch (SQLException e) {

                throw new PayrollServiceException(e.getMessage(), PayrollServiceException.ExceptionType.RETRIEVAL_PROBLEM);
            }
            return employeePayrollList;
        }

       /* public int updateEmployeeData(String name, double salary) throws PayrollServiceException {
            return this.updateEmployeeDataUsingStatement(name, salary);
        }*/
    public int updateEmployeeData(String name, double salary) throws PayrollServiceException {
        return this.updateEmployeeDataUsingPreparedStatement(name, salary);
    }
        private int updateEmployeeDataUsingStatement(String name, double salary) throws PayrollServiceException {
            String sql = String.format("update employee_payroll set salary = %.2f where name = '%s';", salary, name);
            try {
                Connection connection = this.getConnection();
                Statement statement = connection.createStatement();
                return statement.executeUpdate(sql);
            } catch (SQLException e) {
                throw new PayrollServiceException(e.getMessage(), PayrollServiceException.ExceptionType.UPDATE_PROBLEM);
            }
        }
    public int updateEmployeeDataUsingPreparedStatement(String name, double salary) {
        try (Connection connection = this.getConnection();) {
            String sql = "update employee_payroll set salary=? where name=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1, salary);
            preparedStatement.setString(2, name);
            int status = preparedStatement.executeUpdate();
            return status;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<EmployeePayrollData> getEmployeeForDateRange(LocalDate startDate, LocalDate endDate)
            throws PayrollServiceException {
        String sql = String.format("select * from employee_payroll where start between '%s' and '%s';",
                Date.valueOf(startDate), Date.valueOf(endDate));
        return this.getEmployeePayrollDataUsingDB(sql);
    }

    private List<EmployeePayrollData> getEmployeePayrollDataUsingDB(String sql) throws PayrollServiceException {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try (Connection connection = employeePayrollDatabaseService.getConnection();) {
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


