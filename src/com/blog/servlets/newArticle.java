package com.blog.servlets;

import com.blog.bean.Article;
import com.blog.dao.ArticleDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/newArticle")
public class newArticle extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String descr = request.getParameter("descr");

        Article article = new Article();
        article.newArticle(title, descr, content);
        ArticleDAO articleDAO = new ArticleDAO();
        articleDAO.insert(article);

        response.sendRedirect("console");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
