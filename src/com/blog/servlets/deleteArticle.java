package com.blog.servlets;

import com.blog.dao.ArticleDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/deleteArticle")
public class deleteArticle extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ID = request.getParameter("articleId");
        int id;
        if (ID != null && !ID.equals("")) {
            id = Integer.parseInt(ID);
        } else {
            System.out.println("id is empty when delete");
            return;
        }
        ArticleDAO articleDAO = new ArticleDAO();
        articleDAO.delete(id);
        response.sendRedirect("console");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
