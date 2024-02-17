package com.example.workflow;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface RandomActivity {
    @ActivityMethod
    String random();

    @ActivityMethod
    String getJavaVersion();

    @ActivityMethod
    void writeToFiles(String path, String content);

    @ActivityMethod
    void logValue(String signal, String workflowId);

    @ActivityMethod
    void logWarning(String signal, String workflowId);
}
