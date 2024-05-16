package com.blog.servlets;

import com.blog.bean.User;
import com.blog.dao.UserDAO;
import com.blog.myssm.myspringmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/updateUser")
public class UpdateUser extends ViewBaseServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        // 更新users数据库中的数据
        String id = request.getParameter("userId");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");status
        User user = new User();
        UserDAO userDAO = new UserDAO();
        user = userDAO.getUserById(Integer.parseInt(id));
        if (name != null && !name.isEmpty()) {
            user.setName(name);
            System.out.println("Change name: " + name);
        }
        if (email != null && !email.isEmpty()) {
            user.setEmail(email);
            System.out.println("Change email: " + email);
        }
        if (password != null && !password.isEmpty()) {
            user.setPassword(password);
            System.out.println("Change password: " + password);
        }

        userDAO.update(Integer.parseInt(id), user);

        session.setAttribute("user", user);
        System.out.println("self: " + user.getName());
        response.sendRedirect("console");
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
