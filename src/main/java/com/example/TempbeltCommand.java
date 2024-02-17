package com.example;

import com.example.commands.ComplexOperationWorkflowCommand;
import com.example.commands.PlaygroundDemoQueueCommand;
import com.example.commands.StartLocalWorkerCommand;
import io.micronaut.configuration.picocli.PicocliRunner;
import org.slf4j.Logger;
import picocli.CommandLine.Command;

import static org.slf4j.LoggerFactory.getLogger;

@Command(
        name = "tempbelt",
        description = "...",
        mixinStandardHelpOptions = true,
        subcommands = {
                PlaygroundDemoQueueCommand.class,
                ComplexOperationWorkflowCommand.class,
                StartLocalWorkerCommand.class,
        }
)
public class TempbeltCommand implements Runnable {
    private final Logger log = getLogger(TempbeltCommand.class);

    public static void main(String[] args) {
        PicocliRunner.run(TempbeltCommand.class, args);
    }

    public void run() {
        log.info("Hello from Tempbelt!");
    }
}
