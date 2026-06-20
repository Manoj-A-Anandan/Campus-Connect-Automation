package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import reports.ExtentManager;
import driver.DriverManager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExtentListener implements ITestListener {

    ExtentReports extent = ExtentManager.getInstance();

    private String getTestName(ITestResult result) {
        Object[] parameters = result.getParameters();
        if (parameters != null && parameters.length > 0) {
            String rawName = parameters[0].toString();
            if (rawName.startsWith("\"") && rawName.endsWith("\"")) {
                return rawName.substring(1, rawName.length() - 1);
            }
            return rawName;
        }
        return result.getName();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = getTestName(result);
        extent.createTest(testName)
                .pass("Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = getTestName(result);
        ExtentTest test = extent.createTest(testName);
        test.fail(result.getThrowable());

        byte[] screenshotBytes = DriverManager.getScreenshot();
        if (screenshotBytes != null) {
            try {
                // Capture and attach to Allure
                AllureUtils.attachScreenshot(screenshotBytes);

                // Save physical screenshot file
                String sanitizedName = testName.replaceAll("[^a-zA-Z0-9.-]", "_");
                String fileName = sanitizedName + "_" + System.currentTimeMillis() + ".png";
                Path destPath = Paths.get("reports/extent-report/screenshots/", fileName);
                Files.createDirectories(destPath.getParent());
                Files.write(destPath, screenshotBytes);

                // Attach using relative path from report (ExtentReport.html is in reports/extent-report/)
                String relativePath = "screenshots/" + fileName;
                test.fail("Failure Screenshot (Path)", 
                        MediaEntityBuilder.createScreenCaptureFromPath(relativePath).build());

                // Capture and embed as Base64 in Extent
                String screenshotBase64 = java.util.Base64.getEncoder().encodeToString(screenshotBytes);
                test.fail("Failure Screenshot (Base64)", 
                        MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());

            } catch (Exception e) {
                test.info("Failed to attach screenshot: " + e.getMessage());
            } finally {
                DriverManager.clearScreenshot();
            }
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}
