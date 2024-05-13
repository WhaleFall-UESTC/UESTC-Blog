package com.blog.servlets;

import com.blog.bean.Msg;
import com.blog.bean.Nts;
import com.blog.bean.User;
import com.blog.dao.MsgDAO;
import com.blog.dao.NtsDAO;
import com.blog.dao.PreInviterDAO;
import com.blog.dao.UserDAO;
import com.blog.myssm.myspringmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/preinviter")
public class PreInviter extends ViewBaseServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        String idStr = request.getParameter("id");

        int id = Integer.parseInt(idStr);
        UserDAO userDAO = new UserDAO();
        User user = new User();
        user = userDAO.preinviter(id);
        request.setAttribute("authority", 1);
        request.setAttribute("id",user.getId());
        MsgDAO msgDAO = new MsgDAO();
        List<Msg> msgs = msgDAO.selectall();
        NtsDAO ntsDAO = new NtsDAO();
        List<Nts> ntss = ntsDAO.selectall();
        PreInviterDAO preInviterDAO = new PreInviterDAO();
        List<com.blog.bean.PreInviter> preInviters = preInviterDAO.selectall();

        request.setAttribute("msgs", msgs);
        request.setAttribute("ntss", ntss);
        request.setAttribute("preinviters", preInviters);
        super.processTemplate("home", request, response);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}