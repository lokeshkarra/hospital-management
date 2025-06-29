package com.hospital.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

// This filter will intercept all requests except for the ones specified.
@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Get the requested URI
        String uri = httpRequest.getRequestURI();
        
        // Find the user's session
        HttpSession session = httpRequest.getSession(false); // false means do not create a new session

        // Check if the user is logged in
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        // Define pages that can be accessed without logging in
        boolean isLoginRequest = uri.endsWith("login.jsp") || uri.endsWith("login");
        boolean isLogoutRequest = uri.endsWith("logout");
        boolean isStaticResource = uri.contains("/css/") || uri.contains("/js/") || uri.contains("/images/");

        // --- The Core Logic ---
        if (isLoggedIn || isLoginRequest || isStaticResource) {
            // If logged in, or trying to log in, or requesting a resource, let them proceed.
            // Also, prevent logged-in users from accessing the login page again.
            if (isLoggedIn && isLoginRequest) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/dashboard");
            } else {
                // Set cache control headers for all protected pages
                httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-validate"); // HTTP 1.1.
                httpResponse.setHeader("Pragma", "no-cache"); // HTTP 1.0.
                httpResponse.setDateHeader("Expires", 0); // Proxies.
                chain.doFilter(request, response); // User is allowed, so continue the request.
            }
        } else if (isLogoutRequest && isLoggedIn) {
             // Handle logout separately if needed, otherwise this is fine.
             chain.doFilter(request, response);
        } else {
            // User is not logged in and is trying to access a protected page.
            // Redirect to the login page.
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}