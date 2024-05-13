package com.blog.servlets;

import com.blog.bean.Msg;
import com.blog.bean.Nts;
import com.blog.bean.PreInviter;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/changetoinviter")
public class ChangeToInviter extends ViewBaseServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String idStr = request.getParameter("id");
        int id = Integer.valueOf(idStr);
        UserDAO userDAO = new UserDAO();
        User user = UserDAO.changetoinviter(id);
        PreInviterDAO preInviterDAO = new PreInviterDAO();
        preInviterDAO.delete(id);
        if(user != null){
            MsgDAO msgDAO = new MsgDAO();
            List<Msg> msgs = msgDAO.selectall();
            NtsDAO ntsDAO = new NtsDAO();
            List<Nts> ntss = ntsDAO.selectall();
            List<PreInviter> preInviters = preInviterDAO.selectall();

            request.setAttribute("msgs", msgs);
            request.setAttribute("ntss", ntss);
            request.setAttribute("preinviters", preInviters);
            request.setAttribute("authority", 3);
            super.processTemplate("home",request,response);
        } else{
            request.setAttribute("tipMsg","email or password is wrong!");
            super.processTemplate("login",request,response);
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}