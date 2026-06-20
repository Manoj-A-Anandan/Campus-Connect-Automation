package utilities;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class AnnotationTransformer implements IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation,
                          Class testClass,
                          Constructor testConstructor,
                          Method testMethod) {
        boolean shouldRetry = false;

        if (testMethod != null) {
            String methodName = testMethod.getName().toLowerCase();
            if (methodName.contains("password") || methodName.contains("pwd") || methodName.contains("validation")) {
                shouldRetry = true;
            }
        }

        if (testClass != null) {
            String className = testClass.getSimpleName().toLowerCase();
            if (className.contains("password") || className.contains("pwd") || className.contains("validation")) {
                shouldRetry = true;
            }
        }

        if (shouldRetry) {
            annotation.setRetryAnalyzer(RetryAnalyzer.class);
        }
    }
}
