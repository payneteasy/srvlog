[![CircleCI](https://circleci.com/gh/payneteasy/srvlog.svg?style=svg)](https://circleci.com/gh/payneteasy/srvlog)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.payneteasy%3Asrvlog&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.payneteasy%3Asrvlog)

srvlog - **Lightweight Logs Collector System.**
===

Supported logs formats and channels:
1) syslog;
2) logback;
3) snort payloads correlated with ossec alerts.

All logs are being collected in MariaDB database 
(MySQL supported too) and then indexed and searched 
by Sphinx. The middle-layer and front-end is written 
in pure Java hence it can be installed on many OS platforms 
which supported by Java, MariaDB and Sphinx. 

**srvlog** can be used in the projects where centralized 
logging solution is required. As an example of such 
requirements is PCI DSS requirements 10.1-10.2 

Software requirements:
1) JDK 17.0.10 and higher;
2) MariaDB 10.0.x and higher;
3) sphinxsearch 3.6.1 and higher;
4) Maven 3.9.x for building (the bundled wrapper `./mvnw` downloads it automatically).

Minimum system requirements:
1) 4GB RAM;
2) 100GB Disk space (for up to 200 thousands logs per day )
3) Intell i5, i7, Xeon processors.

### Building and starting embedded srvlog jetty server

The embedded server runs on Jetty 12 (ee10 environment, Servlet 6.0 / `jakarta.servlet` API).

Build uber-jar file:

```shell
./mvnw clean package
```
Set environment variables required for embedded server:
```shell
export JETTY_PORT=8080 # server port
export JETTY_CONTEXT=/srvlog # web application context path
export JETTY_ENV_CONFIG_PATH=/path/to/jetty-env-ui.xml # server env config xml
export WEB_DESCRIPTOR_PATH=/path/to/web.xml # web application config xml
export WEB_SOCKET_ENDPOINT_PATH=/ws-log # web socket endpoint context path
export WEB_SOCKET_MAX_MESSAGE_SIZE=65535 # web socket max message size in bytes
export WEB_SOCKET_IDLE_TIMEOUT_SECONDS=300 # web socket idle timeout in seconds
export SYSLOG_PROTOCOL=tcp # syslog protocol
export SYSLOG_HOST=localhost # syslog host
export SYSLOG_PORT=2514 # syslog port
export JSON_ADAPTER_BIND_ADDRESS=127.0.0.1 # json adapter bind address
export JSON_ADAPTER_PORT=28080 # json adapter port
export JSON_ADAPTER_PATH=/save-logs # json adapter path
export JSON_ADAPTER_TOKEN=token # json adapter token
export SPHINX_HOST=localhost # sphinx host
export SPHINX_PORT=9312 # sphinx port
export SPHINX_CONNECT_TIMEOUT=30000 # sphinx connect timeout
export SPHINX_QUERY_INDEXES=index1,index2 # comma separated query indexes
export LOG_STORAGE_CAPACITY=1000 # log broadcasting service storage capacity (web terminal page)
export LOGBACK_PROGRAM=programName # logback program name
export LOGBACK_TCP_PORT=4713 # port for logback tcp adapter
export LOGBACK_UDP_PORT=4713 # port for logback udp adapter
```
`JETTY_ENV_CONFIG_PATH` points to a Jetty XML file that binds the JNDI datasource `java:/comp/env/jdbc/srvlog`.
Since Jetty 12 the `Configure` element must reference the ee10 web application context class,
otherwise the server fails to start. Minimal example (see `srvlog-web/src/test/resources/jetty/jetty-env-ui.xml`):

```xml
<?xml version="1.0"?>
<!DOCTYPE Configure PUBLIC "-//Mort Bay Consulting//DTD Configure//EN" "http://jetty.mortbay.org/configure.dtd">
<Configure id="wac" class="org.eclipse.jetty.ee10.webapp.WebAppContext">
    <New id="dataSource" class="org.eclipse.jetty.plus.jndi.Resource">
        <Arg>java:/comp/env/jdbc/srvlog</Arg>
        <Arg>
            <New class="org.apache.commons.dbcp.BasicDataSource">
                <!-- the uber-jar ships MySQL Connector/J only; it works with MariaDB servers as well -->
                <Set name="url">jdbc:mysql://localhost/srvlog?characterEncoding=utf8&amp;useInformationSchema=true&amp;noAccessToProcedureBodies=true&amp;autoReconnect=false</Set>
                <Set name="driverClassName">com.mysql.cj.jdbc.Driver</Set>
                <Set name="username">srvlog</Set>
                <Set name="password">secret</Set>
                <Set name="testOnBorrow">true</Set>
                <!-- create_collections() sets per-session variables required by the stored procedures -->
                <Set name="validationQuery">call create_collections()</Set>
            </New>
        </Arg>
    </New>
</Configure>
```

When upgrading from a release built on Jetty 11 or older, replace `org.eclipse.jetty.webapp.WebAppContext`
with `org.eclipse.jetty.ee10.webapp.WebAppContext` in the existing `jetty-env.xml`; the rest of the file stays the same.

Start server uber-jar:
```shell
java -jar ./srvlog-web/target/srvlog-embed-server.jar
```