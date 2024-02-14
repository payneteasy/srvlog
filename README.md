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
`1) JDK 17.0.10 and higher;
2) MariaDB 10.0.x and higher;
3) sphinxsearch 3.6.1 and higher.

Minimum system requirements:
1) 4GB RAM;
2) 100GB Disk space (for up to 200 thousands logs per day )
3) Intell i5, i7, Xeon processors.

### Building and starting embedded srvlog jetty server

Build uber-jar file:

```shell
mvn clean package
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
```
Start server uber-jar:
```shell
java -jar ./srvlog-web/target/srvlog-embed-server.jar
```