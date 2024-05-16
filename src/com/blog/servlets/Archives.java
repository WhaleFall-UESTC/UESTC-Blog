package com.blog.servlets;

import com.blog.bean.*;
import com.blog.dao.ArticleDAO;
import com.blog.dao.MsgDAO;
import com.blog.dao.NtsDAO;
import com.blog.myssm.myspringmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
