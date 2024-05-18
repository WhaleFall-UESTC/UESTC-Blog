package com.blog.servlets;

import com.blog.bean.Article;
import com.blog.dao.ArticleDAO;
import com.blog.myssm.myspringmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/edit")
public class Edit extends ViewBaseServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        String ID = request.getParameter("articleId");
        int id = 0;
        if (ID != null && !ID.equals("")) {
            id = Integer.parseInt(ID);
            System.out.println("Edit Article ID: " + id);
            request.setAttribute("id", id);
        } else {
            System.out.println("Do not get article ID when edit");
            response.sendRedirect("console");
        }

        ArticleDAO articleDAO = new ArticleDAO();
        Article article = articleDAO.select(id);

        if (article != null) {
            request.setAttribute("title", article.getTitle());
            request.setAttribute("content", article.getContent());
            System.out.println("Edit Article Title: " + article.getContent());
            request.setAttribute("descr", article.getDescription());
            super.processTemplate("edit", request, response);
        } else {
            System.out.println("Failed to find article when edit");
            response.sendRedirect("console");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
