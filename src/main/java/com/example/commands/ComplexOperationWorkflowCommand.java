
package com.example.commands;

import com.example.workflow.ComplexOperationWorkflow;
import com.example.workflow.WorkflowWorker;
import io.micronaut.context.annotation.Value;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.worker.WorkerFactory;
import jakarta.inject.Inject;
import picocli.CommandLine.Option;

import java.util.UUID;
import java.util.concurrent.Callable;

import static picocli.CommandLine.Command;

@Command(
        name = "complex.operation.workflow",
        description = "Starts a complex operation workflow. " +
                "This includes reading from file, executing a cli command, and writing to a file. ",
        mixinStandardHelpOptions = true
)
public class ComplexOperationWorkflowCommand implements Callable<Object> {
    @Inject
    private WorkflowWorker worker;
    @Inject
    private WorkflowClient workflowClient;
    @Value("${temporal.taskQueue}")
    private String         taskQueue;

    @Option(
            names = {"-d", "--drop"},
            defaultValue = "42",
            description = "One of the activities is to configured to fail if the input is 42. " +
                    "This is to demonstrate how to handle errors in workflows. "
    )
    int drop;

    @Option(
            names = {"-sw", "--start-worker"},
            defaultValue = "false",
            description = "On top of running the load also start a local temporal worker to handle the load."
    )
    boolean startWorker;

    @Override
    public Object call() {
        WorkerFactory workerFactory = null;
        try {
            if (startWorker) {
                workerFactory = worker.start();
            }

            var workflow = workflowClient.newWorkflowStub(
                    ComplexOperationWorkflow.class,
                    WorkflowOptions.newBuilder()
                            .setWorkflowId(UUID.randomUUID().toString())
                            .setTaskQueue(taskQueue)
                            .build()
            );
            workflow.start(drop);
        } finally {
            if (startWorker && workerFactory != null) {
                workerFactory.shutdown();
            }
        }
        return null;
    }
}
