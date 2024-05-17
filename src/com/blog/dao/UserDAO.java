package com.blog.dao;

import com.blog.bean.Msg;
import com.blog.bean.User;
import com.blog.utils.JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public void insert(User user){
        Connection con = null;
        Statement sta = null;
        ResultSet res = null;
        try {
            //1.获取连接
            con = JDBCUtils.getConnection();
            sta = con.createStatement();
            String name = user.getName();
            String password = user.getPassword();
            String email = user.getEmail();
            int authority = user.getAuthority();
            //2.定义sql
            String insertSql = "insert into users (name,password,email,authority) values " +
                    "('"+name+"','"+password+"','"+email+"','"+authority+"')";
            //3.获取执行sql的对象
            sta.executeUpdate(insertSql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(res, sta, con);
        }
    }

    public User getUserById(int Id) {
        Connection con = null;
        Statement sta = null;
        ResultSet res = null;
        User u = new User();

        try {
            con = JDBCUtils.getConnection();
            sta = con.createStatement();
            String sql = "select * from users where id = ?";
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

                return u;
            }
        }

        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(res, sta, con);
        }

        return null;
    }

    public User getUserByName(String name) {
        Connection con = null;
        Statement sta = null;
        ResultSet res = null;
        User u = new User();

        try {
            con = JDBCUtils.getConnection();
            sta = con.createStatement();
            String sql = "select * from users where name = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, name);
            res = pstmt.executeQuery();

            if (res.next()) {
                int id = res.getInt("id");
                String email = res.getString("email");
                String password = res.getString("password");
                int authority = res.getInt("authority");

                u.setId(id);
                u.setName(name);
                u.setPassword(password);
                u.setEmail(email);
                u.setAuthority(authority);

                return u;
            }
        }

        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(res, sta, con);
        }

        return null;
    }

    public User getUserByEmail(String email) {
        Connection con = null;
        Statement sta = null;
        ResultSet res = null;
        User u = new User();

        try {
            con = JDBCUtils.getConnection();
            sta = con.createStatement();
            String sql = "select * from users where email = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, email);
            res = pstmt.executeQuery();

            if (res.next()) {
                int id = res.getInt("id");
                String name = res.getString("name");
                String password = res.getString("password");
                int authority = res.getInt("authority");

                u.setId(id);
                u.setName(name);
                u.setPassword(password);
                u.setEmail(email);
                u.setAuthority(authority);

                return u;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(res, sta, con);
        }

        return null;
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

    public void delete(int id){
        Connection con = null;
        Statement sta = null;
        ResultSet res = null;
        try {
            //1.获取连接
            con = JDBCUtils.getConnection();
            //2.定义sql
            String deleteSql = "delete from users where id =" + id;
            //3.获取执行sql的对象
            sta = con.createStatement();
            //4.执行刪除
            int count = sta.executeUpdate(deleteSql);

            System.out.println("delete user count = " + count);

            deleteSql = "delete from msgs where id =" + id;
            //3.获取执行sql的对象
            sta = con.createStatement();
            //4.执行刪除
            count = sta.executeUpdate(deleteSql);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(res, sta, con);
        }
    }
    public void update(int id, User user){
        Connection con = null;
        Statement sta = null;
        ResultSet res = null;
        try {
            //1.获取连接
            con = JDBCUtils.getConnection();
            sta = con.createStatement();
            String name = user.getName();
            String password = user.getPassword();
            String email = user.getEmail();
            int authority = user.getAuthority();
            //2.定义sql
            String updateSql = "update users set name='" + name + "',\npassword='" + password + "',\nemail='" + email + "',\nauthority='"+ authority+ "'\nwhere id="+ id;
            //3.获取执行sql的对象
            PreparedStatement pre = con.prepareStatement(updateSql);

            int count = pre.executeUpdate();
            System.out.println("count = " + count);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(res, sta, con);
        }
    }
}
