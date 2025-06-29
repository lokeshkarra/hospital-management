package com.hospital.servlet;

import com.hospital.dao.UserDAO; // CHANGED
import com.hospital.model.User;  // CHANGED
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/staff") // URL pattern remains the same
public class StaffServlet extends HttpServlet {

    private UserDAO userDAO; // CHANGED

    @Override
    public void init() {
        userDAO = new UserDAO(); // CHANGED
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "list" : request.getParameter("action");

        switch (action) {
            case "new":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteUser(request, response); // RENAMED
                break;
            default: // "list"
                listUsers(request, response); // RENAMED
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "add":
                addUser(request, response); // RENAMED
                break;
            case "update":
                updateUser(request, response); // RENAMED
                break;
            default:
                listUsers(request, response); // RENAMED
                break;
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<User> listUsers = userDAO.getAllUsers(); // CHANGED
        request.setAttribute("listUsers", listUsers); // CHANGED
        request.setAttribute("activePage", "staff");
        request.getRequestDispatcher("staff-list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	request.setAttribute("activePage", "staff");
        request.getRequestDispatcher("staff-form.jsp").forward(request, response);
    }

//    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        int id = Integer.parseInt(request.getParameter("id"));
//        User existingUser = userDAO.getUserById(id); // CHANGED
//        request.setAttribute("user", existingUser); // CHANGED
//        request.setAttribute("activePage", "staff");
//        request.getRequestDispatcher("staff-form.jsp").forward(request, response);
//    }

 // In StaffServlet.java

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        User existingUser = userDAO.getUserById(id);
        
        // Change the attribute name from "user" to "staffUser"
        request.setAttribute("staffUser", existingUser); // <-- RENAMED
        
        request.setAttribute("activePage", "staff"); // This is still correct
        request.getRequestDispatcher("staff-form.jsp").forward(request, response);
    }
    private void addUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        User newUser = new User(); // CHANGED
        newUser.setUserId(request.getParameter("userId"));
        newUser.setPassword(request.getParameter("password")); // Remember to hash this!
        newUser.setFirstName(request.getParameter("firstName"));
        newUser.setLastName(request.getParameter("lastName"));
        newUser.setRole(request.getParameter("role"));
        newUser.setSpecialization(request.getParameter("specialization"));
        newUser.setEmail(request.getParameter("email"));
        newUser.setPhone(request.getParameter("phone"));

        if (userDAO.createUser(newUser)) { // CHANGED
            request.getSession().setAttribute("message", "Staff member added successfully!");
        } else {
            request.getSession().setAttribute("error", "Failed to add staff member.");
        }
        request.setAttribute("activePage", "staff");
        response.sendRedirect("staff?action=list");
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        User user = new User(); // CHANGED
        user.setId(Integer.parseInt(request.getParameter("id"))); // CHANGED
        user.setFirstName(request.getParameter("firstName"));
        user.setLastName(request.getParameter("lastName"));
        user.setRole(request.getParameter("role"));
        user.setSpecialization(request.getParameter("specialization"));
        user.setEmail(request.getParameter("email"));
        user.setPhone(request.getParameter("phone"));
        user.setStatus(request.getParameter("status"));

        if (userDAO.updateUser(user)) { // CHANGED
            request.getSession().setAttribute("message", "Staff updated successfully!");
        } else {
            request.getSession().setAttribute("error", "Failed to update staff.");
        }
        request.setAttribute("activePage", "staff");
        response.sendRedirect("staff?action=list");
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        if (userDAO.deactivateUser(id)) { // CHANGED
            request.getSession().setAttribute("message", "Staff member deactivated successfully!");
        } else {
            request.getSession().setAttribute("error", "Failed to deactivate staff member.");
        }
        request.setAttribute("activePage", "staff");
        response.sendRedirect("staff?action=list");
    }
    
}