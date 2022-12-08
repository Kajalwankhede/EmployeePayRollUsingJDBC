package com.employeepayroll;

import com.employeepayrollservice.EmployeePayrollData;
import com.employeepayrollservice.EmployeePayrollService;
import com.employeepayrollservice.PayrollServiceException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.employeepayrollservice.EmployeePayrollService.IOService.DB_IO;

public class EmployeePayRollDatabaseTest {
    static EmployeePayrollService employeePayrollService;

    @BeforeClass
    public static void initializeConstructor() {
        employeePayrollService = new EmployeePayrollService();
    }


    @Test
    public void givenThreeEmployeesWhenWrittenToFileShouldMatchEmployeeEntries() {
        EmployeePayrollData[] arrayOfEmps = {
                new EmployeePayrollData(1, "Jeff Bezos", 100000.0),
                new EmployeePayrollData(2, "Bill Gates", 200000.0),
                new EmployeePayrollData(3, "Mark Zuckerberg", 300000.0)
        };
        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
        employeePayrollService.writeEmployeePayrollData(EmployeePayrollService.IOService.FILE_IO);
        employeePayrollService.printData(EmployeePayrollService.IOService.FILE_IO);
        long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.FILE_IO);
        Assert.assertEquals(3, entries);
    }

    @Test
    public void givenFileOnReadingFileShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> entries = employeePayrollService.readPayrollData(EmployeePayrollService.IOService.FILE_IO);
    }

    @Test
    public void givenEmployeePayrollinDBwhenRetrievedShouldMatchEmployeeCount() throws PayrollServiceException {
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(DB_IO);
        Assert.assertEquals(5, employeePayrollData.size());
    }

    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_shouldSynchronizewithDataBase() throws PayrollServiceException {
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(DB_IO);
        employeePayrollService.updateEmployeeSalary("Terisa", 3000000.00);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
        Assert.assertTrue(result);
    }

    @Test
    public void givenDateRangeWhenRetrievedShouldMatchEmployeeCount() throws PayrollServiceException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DB_IO);
        LocalDate startDate = LocalDate.of(2019, 11, 13);
        LocalDate endDate = LocalDate.now();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService
                .readEmployeePayrollForDateRange(DB_IO, startDate, endDate);
        Assert.assertEquals(5, employeePayrollData.size());
    }

    @Test
    public void givenPayrollData_whenAverageSalaryRetrievedByGendershouldReturnProperValue()
            throws PayrollServiceException {
        employeePayrollService.readEmployeePayrollData(DB_IO);
        Map<String, Double> averageSalaryByGender = employeePayrollService.readAverageSalaryByGender(DB_IO);
        Assert.assertTrue(averageSalaryByGender.get("male").equals(30000.00) && averageSalaryByGender.get("Female").equals(40000.00));
    }

    @Test
    public void givenPayrollData_whenAverageSalaryRetrievedByGendershouldReturnProperCountValue()
            throws PayrollServiceException {
        employeePayrollService.readEmployeePayrollData(DB_IO);
        Map<String, Double> countByGender = employeePayrollService.readCountByGender(DB_IO);
        Assert.assertTrue(countByGender.get("male").equals(2.0) && countByGender.get("Female").equals(3.0));
    }

    @Test
    public void givenPayrollData_whenAverageSalaryRetrievedByGendershouldReturnProperMinimumValue()
            throws PayrollServiceException {
        employeePayrollService.readEmployeePayrollData(DB_IO);
        Map<String, Double> countByGender = employeePayrollService.readMinumumSalaryByGender(DB_IO);

        Assert.assertTrue(countByGender.get("male").equals(20000.00) && countByGender.get("Female").equals(40000.00));
    }

    @Test
    public void givenPayrollData_whenAverageSalaryRetrievedByGendershouldReturnProperMaximumValue()
            throws PayrollServiceException {
        employeePayrollService.readEmployeePayrollData(DB_IO);
        Map<String, Double> countByGender = employeePayrollService.readMaximumSalaryByGender(DB_IO);
        Assert.assertTrue(countByGender.get("male").equals(30000.00) && countByGender.get("Female").equals(3000000.00));
    }

    @Test
    public void givenPayrollData_whenAverageSalaryRetrievedByGendershouldReturnProperSumValue()
            throws PayrollServiceException {
        employeePayrollService.readEmployeePayrollData(DB_IO);
        Map<String, Double> sumSalaryByGender = employeePayrollService.readSumSalaryByGender(DB_IO);
        Assert.assertTrue(sumSalaryByGender.get("male").equals(50000.00) && sumSalaryByGender.get("Female").equals(6040000.00));
    }

    @Test
    public void givenNeEmployee_whenAaddedShouldSyncWithTheDatabase() throws PayrollServiceException {
        employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        employeePayrollService.addEmployeeToPayroll("Mark", 5000000.00, LocalDate.now(), "male");
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Mark");
        Assert.assertTrue(result);
    }
}