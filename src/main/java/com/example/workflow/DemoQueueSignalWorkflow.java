package com.example.workflow;

import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface DemoQueueSignalWorkflow {
    @WorkflowMethod
    void start();

    @SignalMethod
    void sendSignal(String signal);
}