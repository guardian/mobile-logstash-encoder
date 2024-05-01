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

This repo uses [`gha-scala-library-release-workflow`](https://github.com/guardian/gha-scala-library-release-workflow)
to automate publishing releases (both full & preview releases) - see
[**Making a Release**](https://github.com/guardian/gha-scala-library-release-workflow/blob/main/docs/making-a-release.md).
```shell
clean
release
```

The cross-compiled released artifacts should appear under:

- [Sonatype](https://oss.sonatype.org/#nexus-search;gav~com.gu~~~~)
- [Maven Central](https://repo1.maven.org/maven2/com/gu/). Note this can take a few minutes to synchronize.

Once the release is done, merge your branch into `main` (or `master`) branch.