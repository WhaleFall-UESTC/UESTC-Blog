package com.blog.servlets;

import com.blog.dao.ArticleDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/updateArticle")
public class updateArticle extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("articleId"));
        String title = request.getParameter("title");
        String descr = request.getParameter("descr");
        String content = request.getParameter("content");

//        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String time = now.format(formatter);

        System.out.println(title);

        ArticleDAO articleDAO = new ArticleDAO();
        if (title != null && !title.isEmpty()) {
            System.out.println("title: " + title);
            articleDAO.update(id, "title", title);
        }
        if (descr != null && !descr.isEmpty()) {
            System.out.println("descr: " + descr);
            articleDAO.update(id, "descr", descr);
        }
        if (content != null && !content.isEmpty()) {
            System.out.println("content: " + content);
            articleDAO.update(id, "content", content);

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String time = now.format(formatter);
            articleDAO.update(id, "time", time);
        }
//        articleDAO.update(id, "time", time);
        response.sendRedirect("console");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}