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
import java.util.Objects;

@WebServlet("/login")
public class Login extends ViewBaseServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        UserDAO userDAO = new UserDAO();

        // 通过邮箱寻找用户
        User user = userDAO.getUserByEmail(email);

        // 检查密码是否输入正确
        if(user != null && Objects.equals(user.getPassword(), password)){
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect("show");
            System.out.println("User " + user.getName() + " login successfully!");
        } else {
            request.setAttribute("tipMsg", "email or password is wrong!");
            System.out.println("Login unsuccessfully!");
            super.processTemplate("login", request, response);
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
