package com.employeepayrollservice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static com.employeepayrollservice.EmployeePayrollService.IOService.DB_IO;

public class EmployeePayrollService {


    public enum IOService{CONSOLE_IO,FILE_IO,DB_IO,REST_IO}
    private  EmployeePayrollDBServiceERD employeePayrollDBServiceERD;
    private EmployeePayrollDatabaseService employeePayrollDatabaseService;
    private List<EmployeePayrollData> employeePayrollList;

    public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList){
        this();
        this.employeePayrollList = employeePayrollList;
    }
    public  EmployeePayrollService(){
        employeePayrollDBServiceERD=EmployeePayrollDBServiceERD.getInstance();
        employeePayrollDatabaseService=EmployeePayrollDatabaseService.getInstance();
    }
    public static void main(String[] args) {
        ArrayList<EmployeePayrollData> employeePayrollList=new ArrayList<EmployeePayrollData>();
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);
        Scanner consoleInputReader = new Scanner(System.in);
        employeePayrollService.readEmployeePayrollData(consoleInputReader);
        employeePayrollService.writeEmployeePayrollData(IOService.CONSOLE_IO);
    }

    public void readEmployeePayrollData(Scanner consoleInputReader) {
        System.out.println("Enter Employee ID: ");
        int id = consoleInputReader.nextInt();
        System.out.println("Enter Employee Name ");
        String name = consoleInputReader.next();
        System.out.println("Enter Employee Salary ");
        double salary = consoleInputReader.nextDouble();
        employeePayrollList.add(new EmployeePayrollData(id, name, salary));
    }

    public void writeEmployeePayrollData(IOService ioService) {
        if (ioService.equals(IOService.CONSOLE_IO))
            System.out.println("\nWriting Employee Payroll Roaster to Console\n" + employeePayrollList);
        else if (ioService.equals(IOService.FILE_IO)) {
            new EmployeePayrollFileIOService().writeData(employeePayrollList);
        }
    }

    public void printData(IOService fileIo) {
        if(fileIo.equals(IOService.FILE_IO)) {
            new EmployeePayrollFileIOService().printData();
        }

    }

    public long countEntries(IOService fileIo) {
        if(fileIo.equals(IOService.FILE_IO)) {
            return new EmployeePayrollFileIOService().countEntries();
        }
        return 0;
    }

    public List<EmployeePayrollData> readPayrollData(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO))
            this.employeePayrollList = new EmployeePayrollFileIOService().readData();
        return employeePayrollList;
    }

    public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService) throws PayrollServiceException {
        if(ioService.equals(DB_IO))
            this.employeePayrollList = employeePayrollDatabaseService.readData();
        return employeePayrollList;
    }
    public void updateEmployeeSalary(String name, double salary) throws PayrollServiceException {
        int result = employeePayrollDatabaseService.updateEmployeeData(name, salary);
        if (result == 0)
            return;
        EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
        if (employeePayrollData != null)
            employeePayrollData.salary = salary;

    }

    private EmployeePayrollData getEmployeePayrollData(String name) {
        EmployeePayrollData employeePayrollData;
        employeePayrollData = this.employeePayrollList.stream()
                .filter(employeePayrollDataItem -> employeePayrollDataItem.name.equals(name))
                .findFirst()
                .orElse(null);
        return employeePayrollData;
    }

    public boolean checkEmployeePayrollInSyncWithDB(String name) {
         employeePayrollList = employeePayrollDatabaseService.getEmployeePayrollData(name);
        return employeePayrollList.get(0).equals(getEmployeePayrollData(name));

    }

    public List<EmployeePayrollData> readEmployeePayrollForDateRange(IOService ioService, LocalDate startDate,
                                                                     LocalDate endDate) throws PayrollServiceException {
        if (ioService.equals(IOService.DB_IO))
            return employeePayrollDatabaseService.getEmployeeForDateRange(startDate, endDate);
        return null;
    }
    public Map<String, Double> readAverageSalaryByGender(IOService ioService) throws PayrollServiceException {
        if (ioService.equals(IOService.DB_IO))
            return employeePayrollDatabaseService.getAverageSalaryByGender();
        return null;
    }

    public Map<String, Double> readCountByGender(IOService ioService) throws PayrollServiceException {
        if (ioService.equals(IOService.DB_IO))
            return employeePayrollDatabaseService.getCountByGender();
        return null;
    }

    public Map<String, Double> readMinumumSalaryByGender(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            return employeePayrollDatabaseService.getMinimumByGender();
        return null;
    }

    public Map<String, Double> readMaximumSalaryByGender(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            return employeePayrollDatabaseService.getMaximumByGender();
        return null;
    }

    public Map<String, Double> readSumSalaryByGender(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            return employeePayrollDatabaseService.getSalarySumByGender();
        return null;
    }
    public void addEmployeeToPayroll(String name, double salary, LocalDate startDate, String gender) throws PayrollServiceException {
        //employeePayrollList.add(employeePayrollDatabaseService.addEmployeeToPayroll(name, salary, startDate, gender));
        employeePayrollList.add(employeePayrollDBServiceERD.addEmployeeToPayroll(name, salary, startDate, gender));
    }

    public int removeEmployeeFromPayroll(String name, IOService ioService) {
        int employeeCount=0;
        if (ioService.equals(IOService.DB_IO))
            employeeCount=employeePayrollDBServiceERD.removeEmployee(name);
        return employeeCount;
    }

    public List<EmployeePayrollData> readActiveEmployeePayrollData(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollList = employeePayrollDatabaseService.readActiveEmployeeData();
        return this.employeePayrollList;
    }

}
