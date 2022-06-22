# Spring Boot application template

[![Build Status](https://travis-ci.org/hmcts/sscs-case-migration.svg?branch=master)](https://travis-ci.org/hmcts/sscs-case-migration)

## Building the case migration tool

## Get the base repo

Clone this - https://github.com/hmcts/ccd-case-migration-starter.git

Then pull this branch - https://github.com/hmcts/ccd-case-migration-starter/tree/sscs-9911-improved

In IntelliJ
Run gradle build in the processor module which I have updated to use Elastic search
Then run publishToMavenLocal in gradle to add it to your local maven repository

## Get this repo

Clone this - https://github.com/hmcts/sscs-case-migration.git

Then pull this branch - https://github.com/hmcts/sscs-case-migration/tree/sscs-9911-improved

Then run clean and build and you will get a jar file at build/libs/sscs-case-migration.jar

## Run case migration tool

The case migration tool is a spring command line application. This means it needs to be run from the command line using the jar we previously built. When you run the tool don't worry about accidentally performing updates as it does a dryrun by default. For the tool to work you will also need to be connected to the hmcts VPN to access the internal services, this will also allow you to test that you are connecting with IDAM and CCD.

To run the tool execute the jar file on the command line like so;

`java -jar sscs-case-migration.jar`

We also need to provide certain parameters to the tool in order to function.

The first of these is the `--migration.caseId` parameter

`java -jar sscs-case-migration.jar --migration.caseId=1648028211163560`

To run the tool you can do this to start and use a caseId that is on your local. 

Alternatively you can use use `--migration.startDate` and `--migration.endDate` to control how many cases get updated -- like so;

`java -jar sscs-case-migration.jar --migration.startDate=2022-03-23 --migration.endDate=2022-03-25`

Run this and it will update your local for the dates between the start and end, the example above would process the 23rd and the 24th of March 2022 -- note that this is not inclusive so the 25th would not be included.

If you specify `--migration.dryrun` to be false this will perform an update of a case. The example below will allow you to test updates are being done correctly on one case.

`java -jar sscs-case-migration.jar --migration.caseId=1648028211163560 --migration.dryrun=false`

Running with dry run enabled won’t update anything but will log the date of the oldest case to tell you where to start and will tell you the number of cases on each date so you can control the updates.

We locally have application.properties files for demo, aat and perftest. We swap them out and build another jar file for each environment. The secrets for these can also be found in Azure vault. To run the tool with a different profile you can provide the `--spring.profiles.active` parameter like so;

`java -jar sscs-case-migration.jar --spring.profiles.active=perftest --migration.caseId=1648028211163560`

An important addition to the tool is processing the cases in parallel streams to speed up operation. This has been benchmarked to reduce the total runtime of doing a 600k cases migration from 3 weeks down to 15 hours -- a significant time saving. The parallel streams was implemented in a way to be backwards compatible with the original code. This is disabled by default but can be enabled by using the `--migration.parallel` parameter -- like so;

`java -jar sscs-case-migration.jar --migration.startDate=2022-05-10  --migration.endDate=2022-05-12  --migration.parallel=false`

The command above will execute a dryrun between the two dates specified using parallel streams.

By default the tool will fetch all the cases for a given day, even on a dryrun. If we were to try to get the sum of cases over a particular period this would take quite a long time. This is why we (Waqas mainly) have included the `--migration.indexCases` parameter. By default this will be set as false but if enabled it will return early when sequencing the cases between a given set of dates. Meaning that we can index the cases much faster in the case of doing the production migration for example. It is recommended not to run this with parallel streams so the log output is in date sequence ascending order.

`java -jar sscs-case-migration.jar --migration.startDate=2022-05-10  --migration.endDate=2022-05-12  --migration.indexCases=true`

The command above will index the cases between the given dates and output the number of cases on the 10th and 11th May 2022.


# Original README below

## Purpose

The purpose of this template is to speed up the creation of new Spring applications within HMCTS
and help keep the same standards across multiple teams. If you need to create a new app, you can
simply use this one as a starting point and build on top of it.

## What's inside

The template is a working application with a minimal setup. It contains:
 * application skeleton
 * setup script to prepare project
 * common plugins and libraries
 * docker setup
 * swagger configuration for api documentation ([see how to publish your api documentation to shared repository](https://github.com/hmcts/reform-api-docs#publish-swagger-docs))
 * code quality tools already set up
 * integration with Travis CI
 * Hystrix circuit breaker enabled
 * MIT license and contribution information
 * Helm chart using chart-java.

The application exposes health endpoint (http://localhost:4550/health) and metrics endpoint
(http://localhost:4550/metrics).

## Plugins

The template contains the following plugins:

  * checkstyle

    https://docs.gradle.org/current/userguide/checkstyle_plugin.html

    Performs code style checks on Java source files using Checkstyle and generates reports from these checks.
    The checks are included in gradle's *check* task (you can run them by executing `./gradlew check` command).

  * pmd

    https://docs.gradle.org/current/userguide/pmd_plugin.html

    Performs static code analysis to finds common programming flaws. Included in gradle `check` task.


  * jacoco

    https://docs.gradle.org/current/userguide/jacoco_plugin.html

    Provides code coverage metrics for Java code via integration with JaCoCo.
    You can create the report by running the following command:

    ```bash
      ./gradlew jacocoTestReport
    ```

    The report will be created in build/reports subdirectory in your project directory.

  * io.spring.dependency-management

    https://github.com/spring-gradle-plugins/dependency-management-plugin

    Provides Maven-like dependency management. Allows you to declare dependency management
    using `dependency 'groupId:artifactId:version'`
    or `dependency group:'group', name:'name', version:version'`.

  * org.springframework.boot

    http://projects.spring.io/spring-boot/

    Reduces the amount of work needed to create a Spring application

  * org.owasp.dependencycheck

    https://jeremylong.github.io/DependencyCheck/dependency-check-gradle/index.html

    Provides monitoring of the project's dependent libraries and creating a report
    of known vulnerable components that are included in the build. To run it
    execute `gradle dependencyCheck` command.

  * com.github.ben-manes.versions

    https://github.com/ben-manes/gradle-versions-plugin

    Provides a task to determine which dependencies have updates. Usage:

    ```bash
      ./gradlew dependencyUpdates -Drevision=release
    ```

## Setup

Located in `./bin/init.sh`. Simply run and follow the explanation how to execute it.

## Notes

Since Spring Boot 2.1 bean overriding is disabled. If you want to enable it you will need to set `spring.main.allow-bean-definition-overriding` to `true`.

JUnit 5 is now enabled by default in the project. Please refrain from using JUnit4 and use the next generation

## Building and deploying the application

### Building the application

The project uses [Gradle](https://gradle.org) as a build tool. It already contains
`./gradlew` wrapper script, so there's no need to install gradle.

To build the project execute the following command:

```bash
  ./gradlew build
```

### Running the application

Create the image of the application by executing the following command:

```bash
  ./gradlew assemble
```

Create docker image:

```bash
  docker-compose build
```

Run the distribution (created in `build/install/sscs-case-migration` directory)
by executing the following command:

```bash
  docker-compose up
```

This will start the API container exposing the application's port
(set to `4550` in this template app).

In order to test if the application is up, you can call its health endpoint:

```bash
  curl http://localhost:4550/health
```

You should get a response similar to this:

```
  {"status":"UP","diskSpace":{"status":"UP","total":249644974080,"free":137188298752,"threshold":10485760}}
```

### Alternative script to run application

To skip all the setting up and building, just execute the following command:

```bash
./bin/run-in-docker.sh
```

For more information:

```bash
./bin/run-in-docker.sh -h
```

Script includes bare minimum environment variables necessary to start api instance. Whenever any variable is changed or any other script regarding docker image/container build, the suggested way to ensure all is cleaned up properly is by this command:

```bash
docker-compose rm
```

It clears stopped containers correctly. Might consider removing clutter of images too, especially the ones fiddled with:

```bash
docker images

docker image rm <image-id>
```

There is no need to remove postgres and java or similar core images.

### Other

Hystrix offers much more than Circuit Breaker pattern implementation or command monitoring.
Here are some other functionalities it provides:
 * [Separate, per-dependency thread pools](https://github.com/Netflix/Hystrix/wiki/How-it-Works#isolation)
 * [Semaphores](https://github.com/Netflix/Hystrix/wiki/How-it-Works#semaphores), which you can use to limit
 the number of concurrent calls to any given dependency
 * [Request caching](https://github.com/Netflix/Hystrix/wiki/How-it-Works#request-caching), allowing
 different code paths to execute Hystrix Commands without worrying about duplicating work

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

