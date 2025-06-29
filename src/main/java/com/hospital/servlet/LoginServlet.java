package com.hospital.servlet;

import com.hospital.dao.UserDAO;
import com.hospital.model.User;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        
        // Server-side validation
        String errorMessage = validateCredentials(userId, password);
        
        if (errorMessage != null) {
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
        
        // Authenticate user
        User user = userDAO.authenticateUser(userId, password);
        
        if (user != null) {
            // Create session
            HttpSession session = request.getSession();
            session.setAttribute("loggedIn", true);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("user", user);
            session.setAttribute("userRole", user.getRole());
            session.setMaxInactiveInterval(30 * 60); // 30 minutes
            
            // Redirect to dashboard
            response.sendRedirect("dashboard");
        } else {
            request.setAttribute("errorMessage", "Invalid User ID or Password");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
    
    private String validateCredentials(String userId, String password) {
        if (userId == null || userId.trim().isEmpty()) {
            return "User ID is required";
        }
        
        if (password == null || password.trim().isEmpty()) {
            return "Password is required";
        }
        
        // User ID validation: at least 8 characters, alphanumeric
        if (!Pattern.matches("^[a-zA-Z0-9]{8,}$", userId)) {
            return "User ID must be at least 8 characters and alphanumeric.";
        }
        
        // Password validation: at least 10 characters, one uppercase, one number, one special character
        if (!Pattern.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{10,}$", password)) {
            return "Password must be at least 10 characters, including one uppercase, one number, and one special character.";
        }
        
        return null;
    }
}