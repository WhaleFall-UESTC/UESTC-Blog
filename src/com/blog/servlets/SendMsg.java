package com.blog.servlets;

import com.blog.bean.Msg;
import com.blog.bean.Nts;
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
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/sendMsg")
public class SendMsg extends ViewBaseServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String msg = request.getParameter("msg");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int userId = user.getId();

//        String userauthorityStr = request.getParameter("authority");
//        int userauthority = Integer.parseInt(userauthorityStr);
        Msg m5g = new Msg();
        m5g.setMsg(msg);
        m5g.setUserId(userId);
        MsgDAO msgDAO = new MsgDAO();
        boolean flag = msgDAO.send(m5g);
        if (!flag) {
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script type=\"text/javascript\">");
            out.println("alert('留言失败')");
            out.println("</script>");
        }

        response.sendRedirect("board");
//        if(flag){
//            List<Msg> msgs = msgDAO.selectall();
//            NtsDAO ntsDAO = new NtsDAO();
//            List<Nts> ntss = ntsDAO.selectall();
//            PreInviterDAO preInviterDAO = new PreInviterDAO();
//            List<PreInviter> preInviters = preInviterDAO.selectall();
//
//            request.setAttribute("msgs", msgs);
//            request.setAttribute("ntss", ntss);
//            request.setAttribute("preinviters", preInviters);
//            System.out.println("Send successfully!");
//            request.setAttribute("authority", userauthority);
//            super.processTemplate("home",request,response);
//        } else{
//            request.setAttribute("authority", userauthority);
//            super.processTemplate("home",request,response);
//        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
