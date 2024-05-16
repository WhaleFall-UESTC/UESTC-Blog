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

@WebServlet("/updateAuthority")
public class UpdateAuthority extends ViewBaseServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        // 更新users数据库中的数据
        String id = request.getParameter("userId");
        String authority = request.getParameter("authority");
        User user = new User();
        UserDAO userDAO = new UserDAO();
        user = userDAO.getUserById(Integer.parseInt(id));
        user.setAuthority(Integer.parseInt(authority));

        userDAO.update(Integer.parseInt(id), user);

        String selfName = request.getParameter("selfName");
        User self = userDAO.getUserByName(selfName);
        session.setAttribute("self", self);
        response.sendRedirect("console");
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}