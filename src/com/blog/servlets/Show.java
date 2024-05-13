package com.blog.servlets;

import com.blog.bean.*;
import com.blog.bean.PreInviter;
import com.blog.dao.*;
import com.blog.myssm.myspringmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/show")
public class Show extends ViewBaseServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MsgDAO msgDAO = new MsgDAO();
        List<Msg> msgs = msgDAO.selectall();
        NtsDAO ntsDAO = new NtsDAO();
        List<Nts> ntss = ntsDAO.selectall();
        PreInviterDAO preInviterDAO = new PreInviterDAO();
        List<PreInviter> preInviters = preInviterDAO.selectall();

        request.setAttribute("msgs", msgs);
        request.setAttribute("ntss", ntss);
        request.setAttribute("preinviters", preInviters);
        request.setAttribute("authority", 0);

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
