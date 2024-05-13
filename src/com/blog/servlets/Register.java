package com.blog.servlets;

import com.blog.bean.User;
import com.blog.dao.UserDAO;
import com.blog.myssm.myspringmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class Register extends ViewBaseServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setEmail(email);
        UserDAO userDAO = new UserDAO();
        boolean flag = userDAO.register(user);
        if(flag){
            System.out.println("Register successfully!");
            super.processTemplate("login",request,response);
        } else{
            System.out.println("Register unsuccessfully!");
            request.setAttribute("tipMsg", "Username has been used!");
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
