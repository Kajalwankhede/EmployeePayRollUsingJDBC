package com.employeepayrollservice;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
public class EmployeePayrollDatabase {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        String DB_URL = "jdbc:mysql://localhost:3306/payroll_service"; // give database name
        String USER = "root";
        String PASS = "Kajal@123";
        Connection con ;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Driver name
            System.out.println("Driver loaded");
        }catch (ClassNotFoundException e){
            throw new IllegalStateException("Cannot find driver in the classpath",e);
        }
        listDrivers();
        try{
            System.out.println("Connecting to database : "+DB_URL);
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connection successfully "+con);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void listDrivers() {
        Enumeration<Driver> driverList=DriverManager.getDrivers();
        while
        (driverList.hasMoreElements()){
            Driver driverClass=(Driver) driverList.nextElement();
            System.out.println(" "+driverClass.getClass().getName());
        }
    }
}
