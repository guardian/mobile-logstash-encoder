# Mobile Logstash Encoder

Mobile wishes to capture App, Stack and Stage using the simple-configuration AWS identity detection.

This library provides an encoder extends the LogstashEncoder to set the custom fields.

Add the MobileLogstash to an appender like

```xml
   <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="com.gu.mobile.logback.MobileLogstash">
            <defaultAppName>[INSERT APP NAME]</defaultAppName>
        </encoder>
    </appender>
```