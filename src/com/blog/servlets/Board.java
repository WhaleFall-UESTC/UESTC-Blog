package com.blog.servlets;

import com.blog.bean.Msg;
import com.blog.bean.PreInviter;
import com.blog.bean.User;
import com.blog.dao.MsgDAO;
import com.blog.dao.NtsDAO;
import com.blog.dao.PreInviterDAO;
import com.blog.myssm.myspringmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/board")
public class Board extends ViewBaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
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

        List<Msg> msgs_all = msgDAO.selectall();
        request.setAttribute("msgs_all", msgs_all);
        request.setAttribute("board", 1);

        super.processTemplate("home", request, response);
    }
}
