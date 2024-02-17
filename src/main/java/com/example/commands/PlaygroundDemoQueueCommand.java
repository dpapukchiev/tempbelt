
package com.example.commands;

import com.example.MessageService;
import com.example.workflow.DemoQueueSignalWorkflow;
import io.micronaut.context.annotation.Value;
import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.api.enums.v1.WorkflowExecutionStatus;
import io.temporal.api.workflowservice.v1.DescribeWorkflowExecutionRequest;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.workflow.Workflow;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static picocli.CommandLine.Command;

@Command(
        name = "playground.demo.queue",
        description = "Starts a demo workflow and send signals to it. " +
                "The program will wait for the workflow to complete before exiting. " +
                "This workflow will be executed by an external worker.",
        mixinStandardHelpOptions = true
)
public class PlaygroundDemoQueueCommand implements Callable<Object> {
    private final Logger         log = Workflow.getLogger(PlaygroundDemoQueueCommand.class);
    @Inject
    private       MessageService messageService;
    @Inject
    private       WorkflowClient workflowClient;
    @Value("${temporal.namespace}")
    private       String         namespace;
    @Option(
            names = {"-a", "--age"},
            defaultValue = "2",
            description = "The age of the person. Demo of how parameters are passed to the workflow."
    )
    int age;

    @Option(
            names = {"-i"},
            defaultValue = "10",
            description = "The number of times to send a signal to the workflow."
    )
    int times;

    @Option(
            names = {"-t"},
            defaultValue = "10",
            description = "The maximum time (in sec) to wait for the workflow to complete."
    )
    int timeout;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @Override
    public Object call() {
        if (spec.helpCommand()) {
            return null;
        }
        var workflowId = UUID.randomUUID().toString();
        var workflow = workflowClient.newWorkflowStub(
                DemoQueueSignalWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setWorkflowId(workflowId)
                        .setTaskQueue("test")
                        .build()
        );
        WorkflowClient.start(workflow::start);

        for (int i = 0; i < times; i++) {
            var signal = i + "-" + age + "-" + messageService.sayHello();
            workflow.sendSignal(signal);
            System.out.println("Sent signal: " + signal);
        }

        ensureWorkflowIsDone(workflowId);
        return null;
    }

    private void ensureWorkflowIsDone(String workflowId) {
        var serviceBlockingStub = workflowClient.getWorkflowServiceStubs().blockingStub();
        await()
                .atMost(timeout, TimeUnit.SECONDS)
                .pollInterval(Duration.ofSeconds(2))
                .alias("Wait for workflow " + workflowId + " to complete")
                .until(() -> {
                    var workflowExecutionInfo = serviceBlockingStub.describeWorkflowExecution(
                            DescribeWorkflowExecutionRequest.newBuilder()
                                    .setNamespace(namespace)
                                    .setExecution(WorkflowExecution.newBuilder().setWorkflowId(workflowId))
                                    .build()
                    ).getWorkflowExecutionInfo();
                    var status = workflowExecutionInfo.getStatus();
                    log.info("Workflow status: {}", status);
                    return status.equals(WorkflowExecutionStatus.WORKFLOW_EXECUTION_STATUS_COMPLETED);
                });
    }
}
