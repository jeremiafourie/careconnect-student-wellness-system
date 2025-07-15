package com.bc.web.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/session-test")
public class SessionTestServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession(false);
        
        out.println("Session Test");
        out.println("============");
        out.println("Session ID: " + (session != null ? session.getId() : "No session"));
        out.println("Session Valid: " + (session != null && session.getAttribute("user") != null));
        
        if (session != null && session.getAttribute("user") != null) {
            out.println("User: " + session.getAttribute("studentNumber"));
            out.println("Login Time: " + session.getCreationTime());
            out.println("Last Access: " + session.getLastAccessedTime());
            out.println("Max Inactive: " + session.getMaxInactiveInterval() + " seconds");
        } else {
            out.println("No user logged in on this session");
        }
        
        out.println("User Agent: " + request.getHeader("User-Agent"));
        out.println("Remote IP: " + request.getRemoteAddr());
    }
}