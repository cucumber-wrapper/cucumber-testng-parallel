package io.github.cucumber.wrapper.testng.service;

import io.github.cucumber.wrapper.testng.model.TestNGFeature;
import io.github.cucumber.wrapper.testng.model.TestNGScenario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CucumberTestListener implements ITestListener {

    @Override
    public synchronized void onFinish(ITestContext context) {
        ITestNGMethod[] pt = context.getPassedTests().getAllResults().stream()
                .map(ITestResult::getMethod)
                .toArray(ITestNGMethod[]::new);
        ITestNGMethod[] ft = context.getFailedTests().getAllResults().stream()
                .map(ITestResult::getMethod)
                .toArray(ITestNGMethod[]::new);
        ITestNGMethod[] st = context.getSkippedTests().getAllResults().stream()
                .map(ITestResult::getMethod)
                .toArray(ITestNGMethod[]::new);

        List<String> failedResults = context.getFailedTests().getAllResults().stream()
                .map(tr -> new FailedResult(tr.getThrowable(), (TestNGScenario) tr.getParameters()[0], (TestNGFeature) tr.getParameters()[1]).toString())
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        sb.append("===============================================\n");
        sb.append("Test Run:").append("\n");
        sb.append("  ").append("Passed: ").append(pt.length);
        if (ft.length != 0) {
            sb.append("\n").append("  ").append("Failed: ").append(ft.length).append("\n");
            failedResults.forEach(r -> sb.append("    ").append(r).append("\n"));
        }
        if (st.length != 0) {
            sb.append("\n").append("  ").append("Skipped: ").append(st.length);
        }

        double passRate = (double) pt.length * 100/(double) (pt.length + ft.length + st.length);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setRoundingMode(RoundingMode.DOWN);

        sb.append("\nPass rate is: ").append(decimalFormat.format(passRate)).append("%");
        sb.append("\n===============================================");
        System.out.println(sb.toString().replaceAll("(?m)^", "[Results] "));
    }

    @Data
    @AllArgsConstructor
    private static class FailedResult {
        private Throwable error;
        private TestNGScenario scenario;
        private TestNGFeature feature;

        @Override
        public synchronized String toString() {
            return "[" + feature.toString() + " -> " + scenario.toString() + "] with error: \n      " + error.toString();
        }
    }
}
