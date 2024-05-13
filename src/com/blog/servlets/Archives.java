package com.blog.servlets;

import com.blog.bean.Article;
import com.blog.bean.Msg;
import com.blog.bean.Nts;
import com.blog.bean.PreInviter;
import com.blog.dao.ArticleDAO;
import com.blog.dao.MsgDAO;
import com.blog.dao.NtsDAO;
import com.blog.dao.PreInviterDAO;
import com.blog.myssm.myspringmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/archives")
public class Archives extends ViewBaseServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
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

        ArticleDAO articleDAO = new ArticleDAO();
        List<String> timeList = articleDAO.findDifferentYearMonths();

        List<List<Article>> primaryList = new ArrayList<>();

        for (String time : timeList) {
            String yearMonth = time.substring(0, 7);
            primaryList.add(articleDAO.selectArticlesByTime(yearMonth));
        }

        request.setAttribute("primaryList", primaryList);
        request.setAttribute("archives", 1);

        super.processTemplate("home", request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
