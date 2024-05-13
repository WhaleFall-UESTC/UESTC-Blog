package com.blog.servlets;

import com.blog.bean.Msg;
import com.blog.bean.Nts;
import com.blog.bean.PreInviter;
import com.blog.dao.MsgDAO;
import com.blog.dao.NtsDAO;
import com.blog.dao.PreInviterDAO;
import com.blog.myssm.myspringmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/sendNts")
public class SendNts extends ViewBaseServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String nts = request.getParameter("nts");
        String userauthorityStr = request.getParameter("authority");
        int userauthority = Integer.parseInt(userauthorityStr);
        Nts nt5 = new Nts();
        nt5.setNts(nts);
        NtsDAO ntsDAO = new NtsDAO();
        boolean flag = ntsDAO.send(nt5);
        if(flag){
            MsgDAO msgDAO = new MsgDAO();
            List<Msg> msgs = msgDAO.selectall();
            List<Nts> ntss = ntsDAO.selectall();
            PreInviterDAO preInviterDAO = new PreInviterDAO();
            List<PreInviter> preInviters = preInviterDAO.selectall();

            request.setAttribute("msgs", msgs);
            request.setAttribute("ntss", ntss);
            request.setAttribute("preinviters", preInviters);
            System.out.println("Send successfully!");
            request.setAttribute("authority", userauthority);
            super.processTemplate("home",request,response);
        } else{
            request.setAttribute("authority", userauthority);
            super.processTemplate("home",request,response);
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
