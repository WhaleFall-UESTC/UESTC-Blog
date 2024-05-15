package com.blog.dao;

import com.blog.bean.Msg;
import com.blog.bean.User;
import com.blog.utils.JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public User login(String email, String password){
        User u = null;
        Connection con = null;
        Statement sta = null;
        ResultSet res = null;
        try {
            //1.获取连接
            con = JDBCUtils.getConnection();
            //2.定义sql
            String sql = "select * from users where email = '"+email+"' and password = '"+password+"'";
            //3.获取执行sql的对象
            sta = con.createStatement();
            //4.执行查询
            res = sta.executeQuery(sql);
            //5.判断
            if(res.next()){
                u = new User();
                u.setName(res.getString("name"));
                u.setPassword(res.getString("password"));
                u.setAuthority(res.getInt("authority"));
                u.setId(res.getInt("id"));
                System.out.println("登录成功");
            } else{
                System.out.println("用户名或密码错误");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(res, sta, con);
        }
        return u;
    }

    public boolean register(User user){
        String name=  user.getName();
        String email = user.getEmail();
        String password = user.getPassword();
        Connection con = null;
        Statement sta = null;
        ResultSet res = null;
        try {
            con = JDBCUtils.getConnection();
            sta = con.createStatement();

            //判断输入是否合法
            if(name.isEmpty() || password.isEmpty()){
                return false;
            }

            //判断用户名是否已存在
            String querySql = "select * from users where name = '"+name+"'";
            res = sta.executeQuery(querySql);
            if(res.next()){
                System.out.println("用户名已存在！");
                return false;
            }

            //添加新用户
            String insertSql = "insert into users (name,password,email,authority) values " +
                    "('"+name+"','"+password+"','"+email+"','"+1+"')";
            sta.executeUpdate(insertSql);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(res, sta, con);
        }
        return true;
    }

    public static User changetoinviter(int id){
        User u = null;
        Connection con = null;
        Statement sta = null;
        ResultSet res = null;
        try {
            con = JDBCUtils.getConnection();
            sta = con.createStatement();

            int rows = sta.executeUpdate("update users set authority = 2 where id=" + id);
            String sql = "select * from users where id = '"+id+"'";
            System.out.println(rows);
            res = sta.executeQuery(sql);
            //5.判断
            if(res.next()){
                u = new User();
                u.setName(res.getString("name"));
                u.setPassword(res.getString("password"));
                u.setAuthority(res.getInt("authority"));
                u.setId(res.getInt("id"));
            } else{
                System.out.println("用户名或密码错误");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(res, sta, con);
        }
        return u;
    }

    public User preinviter(int id) {
        User u = null;
        Connection con = null;
        Statement sta = null;
        ResultSet res = null;
        try {
            con = JDBCUtils.getConnection();
            sta = con.createStatement();
            String sql = "select * from users where id = '"+id+"'";
            res = sta.executeQuery(sql);
            //5.判断
            if(res.next()){
                u = new User();
                u.setName(res.getString("name"));
                u.setPassword(res.getString("password"));
                u.setEmail(res.getString("email"));
                u.setId(res.getInt("id"));
                String insertSql = "insert into pre_inviters (name,email,userid) values " +
                        "('"+u.getName()+"','"+u.getEmail()+"','"+u.getId()+"')";
                sta.executeUpdate(insertSql);
            } else{
                System.out.println("用户名或密码错误");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(res, sta, con);
        }
        return u;
    }

    public User getUserById(int Id) {
        Connection con = null;
        Statement sta = null;
        ResultSet res = null;
        User u = new User();

        try {
            con = JDBCUtils.getConnection();
            sta = con.createStatement();
            String sql = "select * from users where id = ?;";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, Id);
            res = pstmt.executeQuery();

            if (res.next()) {
                int id = res.getInt("id");
                String name = res.getString("name");
                String password = res.getString("password");
                String email = res.getString("email");
                int authority = res.getInt("authority");

                u.setId(id);
                u.setName(name);
                u.setPassword(password);
                u.setEmail(email);
                u.setAuthority(authority);
            }
        }

        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(res, sta, con);
        }

        return u;
    }

    public List<User> selectall() {
        Connection con = null;
        Statement sta = null;
        ResultSet res = null;

        Msg msg = null;
        List<User> users = new ArrayList<>();
        try {
            con = JDBCUtils.getConnection();
            sta = con.createStatement();
            String sql = "select * from users order by id desc;";
            PreparedStatement pstmt = con.prepareStatement(sql);
            res = pstmt.executeQuery();
            while (res.next()) {
                User u = new User();

                int id = res.getInt("id");
                String name = res.getString("name");
                String password = res.getString("password");
                String email = res.getString("email");
                int authority = res.getInt("authority");

                u.setId(id);
                u.setName(name);
                u.setPassword(password);
                u.setEmail(email);
                u.setAuthority(authority);

                users.add(u);
            }
        }

        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(res, sta, con);
        }
        return users;
    }
}
