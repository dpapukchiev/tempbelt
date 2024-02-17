package com.example.workflow;

import io.micronaut.context.annotation.Value;
import io.temporal.client.WorkflowClient;
import io.temporal.worker.WorkerFactory;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class WorkflowWorker {
    @Inject
    private RandomActivity randomActivity;
    @Inject
    private WorkflowClient workflowClient;
    @Value("${temporal.taskQueue}")
    private String         taskQueue;

    public WorkerFactory start() {
        var workerFactory = WorkerFactory.newInstance(workflowClient);
        var worker = workerFactory.newWorker(taskQueue);
        worker.registerWorkflowImplementationTypes(ComplexOperationWorkflowImpl.class);
        worker.registerActivitiesImplementations(randomActivity);

        workerFactory.start();

        return workerFactory;
    }
}
