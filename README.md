# Mobile Logstash Encoder

Mobile wishes to capture App, Stack and Stage using the simple-configuration AWS identity detection.

This library provides an encoder extends the LogstashEncoder to set the custom fields.

Add the MobileLogstash to an appender like

```xml
   <appender name="LOGFILE" class="ch.qos.logback.core.rolling.RollingFileAppender">    
        <encoder class="com.gu.mobile.logback.MobileLogstash">
            <defaultAppName>[INSERT APP NAME]</defaultAppName>
        </encoder>
        <file>logs/[INSERT APP NAME].log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/[INSERT APP NAME].%d{yyyy-MM-dd}.gz</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
   </appender>
```
## Releasing a new version

Newer versions of this library are released into the Sonatype repository, using a manual process on the developer's machine.

In order to release a new version:
- ensure you have a Sonatype account that has access to the `Guardian` organisation.
- ensure you have set up [PGP](https://central.sonatype.org/publish/requirements/gpg/) so artifacts can be signed before publishing.

The release process requires being on a development branch, with all required changes committed into the branch, and no unstaged changes locally.

Make sure you set upstream on the branch: `git push --set-upstream origin <BRANCH_NAME>`

The release process involves steps listed in the `releaseProcess` setting of [build.sbt](build.sbt). 

To execute the release process, in the SBT REPL of the project, run the following commands:
```shell
clean
release
```

The cross-compiled released artifacts should appear under:

- [Sonatype](https://oss.sonatype.org/#nexus-search;gav~com.gu~~~~)
- [Maven Central](https://repo1.maven.org/maven2/com/gu/). Note this can take a few minutes to synchronize.

Once the release is done, merge your branch into `main` (or `master`) branch.