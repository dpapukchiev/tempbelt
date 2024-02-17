## Tempbelt - A CLI tool for developer productivity and automation
This CLI tool is designed to help developers with their day-to-day tasks and automate repetitive tasks. 
It is a POC of how a CLI tool can be used to automate tasks and improve developer productivity.
The project uses micronaut in CLI mode using PicocliRunner for command line parsing and execution.

### How to build from source?
1. Clone the repository `git clone git@github.com:dpapukchiev/tempbelt.git`
2. Change to the project directory `cd tempbelt`
3. Build the project `./gradlew installDist`
4. Add the `bin` directory to your `PATH` environment variable  
`export PATH="${PATH}:/path-to-project/tempbelt/build/install/tempbelt/bin"`
5. Run the CLI tool `tempbelt`

### Commands
```
Usage: tempbelt [-hV] [COMMAND]
...
  -h, --help      Show this help message and exit.
  -V, --version   Print version information and exit.
Commands:
  playground.demo.queue       Starts a demo workflow and send signals to it.
                                The program will wait for the workflow to
                                complete before exiting. This workflow will be
                                executed by an external worker.
  complex.operation.workflow  Starts a complex operation workflow. This
                                includes reading from file, executing a cli
                                command, and writing to a file.
  worker.start                Start a local temporal worker to execute
                                workflows and activities.
```

#### Playground Demo Queue
```
Usage: tempbelt playground.demo.queue [-hV] [-a=<age>] [-i=<times>]
                                      [-t=<timeout>]
Starts a demo workflow and send signals to it. The program will wait for the
workflow to complete before exiting. This workflow will be executed by an
external worker.
  -a, --age=<age>   The age of the person. Demo of how parameters are passed to
                      the workflow.
  -h, --help        Show this help message and exit.
  -i=<times>        The number of times to send a signal to the workflow.
  -t=<timeout>      The maximum time (in sec) to wait for the workflow to
                      complete.
  -V, --version     Print version information and exit.
```

#### Complex Operation Workflow
```
Usage: tempbelt complex.operation.workflow [-hV] [-sw] [-d=<drop>]
Starts a complex operation workflow. This includes reading from file, executing
a cli command, and writing to a file.
  -d, --drop=<drop>         One of the activities is to configured to fail if
                              the input is 42. This is to demonstrate how to
                              handle errors in workflows.
  -h, --help                Show this help message and exit.
  -sw, --start-worker       On top of running the load also start a local
                              temporal worker to handle the load.
  -V, --version             Print version information and exit.
```

#### Worker Start
```
Usage: tempbelt worker.start
Start a local temporal worker to execute workflows and activities.
```


