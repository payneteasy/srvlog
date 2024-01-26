package com.payneteasy.srvlog;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Date: 25.02.13 Time: 16:56
 */
public class ShowVersionServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(ShowVersionServlet.class);

    private volatile String version = "no init";

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        response.getWriter().print(version);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        Properties properties = new Properties();
        InputStream in = config.getServletContext().getResourceAsStream("/META-INF/maven/com.payneteasy.srvlog/srvlog-web/pom.properties");
        if(in == null) {
            version = "no input stream";
            return;
        }
        try {
            properties.load(in);
        } catch (IOException e) {
            version = e.getMessage();
            LOG.error("Cannot read pom.properties");
        }
        version = properties.getProperty("version", "no version");
        super.init(config);
    }
}
