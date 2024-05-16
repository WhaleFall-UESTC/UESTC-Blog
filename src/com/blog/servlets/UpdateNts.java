package com.blog.servlets;

import com.blog.bean.User;
import com.blog.dao.NtsDAO;
import com.blog.dao.UserDAO;
import com.blog.myssm.myspringmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/updateNts")
public class UpdateNts extends ViewBaseServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String nts = request.getParameter("nts");
        NtsDAO ntsDAO = new NtsDAO();
        boolean flag = ntsDAO.send(nts);
        if(flag){
            System.out.println("Send successfully!");
            String name = request.getParameter("selfName");
            UserDAO userDAO = new UserDAO();
            User user = userDAO.getUserByName(name);
            session.setAttribute("self", user);
            response.sendRedirect("console");
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
