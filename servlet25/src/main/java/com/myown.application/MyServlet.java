package com.myown.application;

import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class MyServlet extends HttpServlet {
    private static String encoding = "UTF-8";

    @Override
    public void init() {
        ServletConfig config = this.getServletConfig();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("MyServlet::service");


        response.setCharacterEncoding(encoding);

        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        String requestPath = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (contextPath == null) {
            contextPath = "";
        }

        System.out.println("requestPath: " + requestPath);
        System.out.println("contextPath: " + contextPath);

        int slash = contextPath.startsWith("/") ? 1 : 0;
        String path = requestPath.substring(contextPath.length() + slash);
        if (StringUtils.isBlank(path)) {
            path = "index.html";
        }

        String templatepath = "index.html";
        System.out.println("switch on: " + path);
        switch (path) {

            case "index.html": {
                request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
                return;
            }

            default:
        }
    }
}
