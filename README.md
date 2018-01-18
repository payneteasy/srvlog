[![Build Status](https://travis-ci.org/payneteasy/srvlog.svg?branch=master)](https://travis-ci.org/payneteasy/srvlog)

srvlog - **Lightweight Logs Collector System.**
===

Supported logs formats and channels:
1) syslog;
2) logback;
3) log4j;
3) snort payloads correlated with ossec alerts.

All logs are being collected in MariaDB database 
(MySQL also supported) and then indexed and searched 
by Sphinx. The middle-layer and front-end is written 
in pure Java hence it can be installed on many OS platforms 
which supported by Java, MariaDB and Sphinx. 

**srvlog** can be used in the projects where centralized 
logging solution is required. As an example of such 
requirements is PCI DSS requirements 10.1-10.2 

Software requirements:
1) JDK 1.8.x (1.9.x has not been tested yet);
2) Java Application Container - Tomcat 7.x and higher, Jetty 8.1.x;
2) MariaDB 10.0.x and higher;
3) sphinxsearch 2.2.x and higher.

Minimum system requirements:
1) 4GB RAM;
2) 100GB Disk space (for up to 200 thousands logs per day )
3) Intell i5, i7, Xeon processors.



