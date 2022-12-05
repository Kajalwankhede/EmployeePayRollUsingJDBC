package com.employeepayroll;

import com.employeepayrollservice.EmployeePayrollData;
import com.employeepayrollservice.EmployeePayrollService;
import com.employeepayrollservice.PayrollServiceException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class EmployeePayRollDatabaseTest {
    static EmployeePayrollService employeePayrollService;
    @BeforeClass
    public static void initializeConstructor()
    {
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
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
      Assert.assertEquals(3, employeePayrollData.size());
    }
}
