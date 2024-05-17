package com.blog.servlets;

import com.blog.bean.Article;
import com.blog.bean.User;
import com.blog.dao.ArticleDAO;
import com.blog.dao.NtsDAO;
import com.blog.dao.UserDAO;
import com.blog.myssm.myspringmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/console")
public class Console extends ViewBaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        System.out.println("self in consolr: " + u.getName());
        if (u == null || u.getAuthority() == 0) {
            response.sendRedirect("login.html");
        } else {
            request.setAttribute("self", u);
            request.setAttribute("authority", u.getAuthority());
        }

        NtsDAO ntsDAO = new NtsDAO();
        String nts = ntsDAO.latestNts();
        request.setAttribute("nts", nts);

        ArticleDAO articleDAO = new ArticleDAO();
        List<Article> articles = articleDAO.selectPart(1, articleDAO.countArticles());
        request.setAttribute("articles", articles);

        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.selectall();
        request.setAttribute("users", users);

        request.setAttribute("console", 1);
        super.processTemplate("console", request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
