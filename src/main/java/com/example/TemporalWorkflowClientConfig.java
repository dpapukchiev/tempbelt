package com.example;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import jakarta.inject.Singleton;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

@Factory
public class TemporalWorkflowClientConfig {
    @Value("${temporal.host}")
    private String temporalHost;

    @Singleton
    public WorkflowClient workflowClient() {
        var service = WorkflowServiceStubs.newConnectedServiceStubs(
                WorkflowServiceStubsOptions.newBuilder()
                        .setTarget(temporalHost)
                        .build(),
                Duration.of(10, SECONDS)
        );

        return WorkflowClient.newInstance(service);
    }
}
