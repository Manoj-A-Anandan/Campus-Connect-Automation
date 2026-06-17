package reports;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports getInstance() {

        if(extent == null){

            ExtentSparkReporter spark =
                    new ExtentSparkReporter(
                            "reports/extent-report/ExtentReport.html");

            spark.config()
                    .setDocumentTitle("Automation Report");

            spark.config()
                    .setReportName("Campus Internal Tool");

            extent = new ExtentReports();

            extent.attachReporter(spark);

            extent.setSystemInfo("Framework","Rest Assured");

            extent.setSystemInfo("Environment","QA");
        }

        return extent;
    }
}