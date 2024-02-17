
package com.example.commands;

import com.example.workflow.WorkflowWorker;
import jakarta.inject.Inject;

import java.util.concurrent.Callable;

import static picocli.CommandLine.Command;

@Command(
        name = "worker.start",
        description = "Start a local temporal worker to execute workflows and activities.",
        mixinStandardHelpOptions = true
)
public class StartLocalWorkerCommand implements Callable<Object> {
    @Inject
    private WorkflowWorker worker;

    @Override
    public Object call() {
        worker.start();
        return null;
    }
}
