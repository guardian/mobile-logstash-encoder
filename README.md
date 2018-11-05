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