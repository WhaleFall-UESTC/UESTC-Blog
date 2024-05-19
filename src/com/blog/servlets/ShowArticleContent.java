package com.blog.servlets;

import com.blog.bean.Article;
import com.blog.bean.Msg;
import com.blog.bean.User;
import com.blog.dao.ArticleDAO;
import com.blog.dao.MsgDAO;
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
import java.util.Objects;

@WebServlet("/showArticleContent")
public class ShowArticleContent extends ViewBaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        MsgDAO msgDAO = new MsgDAO();
        List<Msg> msgs = msgDAO.latest5Msg();
        NtsDAO ntsDAO = new NtsDAO();
        String nts = ntsDAO.latestNts();

        request.setAttribute("msgs", msgs);
        request.setAttribute("nts", nts);

        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        if (u == null) {
            request.setAttribute("authority", 0);
        } else {
            request.setAttribute("authority", u.getAuthority());
            System.out.println("User Authority: " + u.getAuthority());
        }

        String ArticleId = request.getParameter("ArticleId");

        ArticleDAO articleDAO = new ArticleDAO();

        Article article = articleDAO.select(Integer.parseInt(ArticleId));

        if(article.getContent() != null && !article.getContent().isEmpty()) {
            request.setAttribute("article", article);
        }


        List<Msg> msgs_all = msgDAO.selectall();
        request.setAttribute("msgs_all", msgs_all);
        request.setAttribute("content", 2);

        super.processTemplate("home", request, response);
    }
}
