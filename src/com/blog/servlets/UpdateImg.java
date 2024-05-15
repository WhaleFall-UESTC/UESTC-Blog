package com.blog.servlets;


import com.blog.bean.User;
import com.blog.dao.UserDAO;
import com.blog.myssm.myspringmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.annotation.MultipartConfig;
@WebServlet("/updateImg")
@MultipartConfig
public class UpdateImg extends ViewBaseServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        // 更改img文件夹中的头像文件
        Part part = request.getPart("img");
        String name = request.getParameter("name");
        String upfilename = part.getSubmittedFileName();

        String fileRoot = getServletContext().getInitParameter("fileRoot");

        Path  rootPath = Paths.get(fileRoot);
        Path filePath;

        int dot = upfilename.lastIndexOf(".");
        String base = upfilename.substring(0, dot);
        String ext = upfilename.substring(dot + 1);
        String newUpfile = name + "." + ext;

        while (true) {
            filePath = Paths.get(fileRoot + "\\" + newUpfile);
            if (Files.exists(filePath)) {
                // 先删除原有头像
                try {
                    Files.delete(filePath);
                    System.out.println("Deleted old file: " + filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            } else {
                try {
                    System.out.println("Try upload " + fileRoot + "\\" + newUpfile);
                    part.write(fileRoot + "\\" + newUpfile);
                    break;
                } catch (IOException e) {
                    if (!Files.exists(rootPath)) {
                        Files.createDirectories(rootPath);
                    } else {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }

        // 返回对应的self
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByName(name);
        session.setAttribute("self", user);

        response.sendRedirect("console");
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
