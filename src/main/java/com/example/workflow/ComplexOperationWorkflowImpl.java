package com.example.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

public class ComplexOperationWorkflowImpl implements ComplexOperationWorkflow {
    private final RandomActivity randomActivity = Workflow.newActivityStub(RandomActivity.class,
            ActivityOptions.newBuilder()
                    .setScheduleToCloseTimeout(Duration.ofSeconds(10))
                    .build()
    );

    private final RandomActivity randomActivityMax1Try = Workflow.newActivityStub(RandomActivity.class,
            ActivityOptions.newBuilder()
                    .setScheduleToCloseTimeout(Duration.ofSeconds(10))
                    .setRetryOptions(RetryOptions.newBuilder()
                            .setMaximumAttempts(1)
                            .build())
                    .build()
    );

    @Override
    public void start(int param1) {
        var result = randomActivity.random();

        var javaVersion = randomActivity.getJavaVersion();

        var toSave = "java=" + javaVersion + " random=" + result;
        try {
            randomActivityMax1Try.logValue(param1 + result, Workflow.getInfo().getWorkflowId());
        } catch (Exception e) {
            randomActivity.writeToFiles("/tmp/", toSave);
            randomActivity.logWarning(result, Workflow.getInfo().getWorkflowId());
        }
    }
}
