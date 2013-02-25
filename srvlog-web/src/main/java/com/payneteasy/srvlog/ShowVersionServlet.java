package com.payneteasy.srvlog;

import com.payneteasy.srvlog.adapter.syslog.ISyslogAdapterConfig;
import com.payneteasy.srvlog.service.ILogCollector;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Date: 25.02.13 Time: 16:56
 */
public class ShowVersionServlet extends HttpServlet{
    
    private ISyslogAdapterConfig syslogConfig;
    private final ReentrantLock lock = new ReentrantLock();
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        StringBuilder stringBuilder = new StringBuilder();

        ISyslogAdapterConfig syslogAdapterConfig = getSyslogConfig();

        stringBuilder.append("Syslog4j started on  port = ");
        stringBuilder.append(syslogAdapterConfig.getSyslogPort());
        stringBuilder.append(" by protocol = ");
        stringBuilder.append(syslogAdapterConfig.getSyslogProtocol());

        response.getWriter().print(stringBuilder);

    }
    
    private ISyslogAdapterConfig getSyslogConfig(){
          if(syslogConfig == null){
            try {
                lock.lock();
                ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
                syslogConfig = (ISyslogAdapterConfig) context.getBean("springSyslogAdapterConfig");
            }finally {
                lock.unlock();
            }
          }
        return syslogConfig;
    }
}
