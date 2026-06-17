package utilities;

import com.aventstack.extentreports.ExtentReports;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import reports.ExtentManager;

public class ExtentListener implements ITestListener {

    ExtentReports extent = ExtentManager.getInstance();


    public void onTestSuccess(ITestResult result) {

        extent.createTest(result.getName())
                .pass("Passed");
    }

    public void onTestFailure(ITestResult result) {

        extent.createTest(result.getName())
                .fail(result.getThrowable());
    }

    public void onFinish(ITestContext context) {

        extent.flush();
    }

}
