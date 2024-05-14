package com.blog.servlets;

import com.blog.bean.*;
import com.blog.bean.PreInviter;
import com.blog.dao.*;
import com.blog.myssm.myspringmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/show")
public class Show extends ViewBaseServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MsgDAO msgDAO = new MsgDAO();
        List<Msg> msgs = msgDAO.latest5Msg();
        NtsDAO ntsDAO = new NtsDAO();
        String nts = ntsDAO.latestNts();
        PreInviterDAO preInviterDAO = new PreInviterDAO();
        List<PreInviter> preInviters = preInviterDAO.selectall();

        request.setAttribute("msgs", msgs);
        request.setAttribute("nts", nts);
        request.setAttribute("preinviters", preInviters);

        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        if (u == null) {
            request.setAttribute("authority", 0);
        } else {
            request.setAttribute("authority", u.getAuthority());
            System.out.println("User Authority: " + u.getAuthority());
        }

        /* Content: Articles in the middle */
        ArticleDAO articleDAO = new ArticleDAO();
        List<Article> articles = articleDAO.selectPart(1, articleDAO.countArticles());
        request.setAttribute("articles", articles);
        request.setAttribute("content", 1);

        super.processTemplate("home", request, response);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
