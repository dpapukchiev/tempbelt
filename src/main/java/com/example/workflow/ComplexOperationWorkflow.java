package com.example.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface ComplexOperationWorkflow {
    @WorkflowMethod
    void start(int param1);
}
