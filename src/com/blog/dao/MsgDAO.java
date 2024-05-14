package com.blog.dao;

import com.blog.bean.Msg;
import com.blog.utils.JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MsgDAO {
    public List<Msg> selectall() {
        Connection con = null;
        Statement sta = null;
        ResultSet res = null;

        Msg msg = null;
        List<Msg> msgs = new ArrayList<>();
        try {
            con = JDBCUtils.getConnection();
            sta = con.createStatement();
            String sql = "select * from msgs order by id desc;";
            PreparedStatement pstmt = con.prepareStatement(sql);
            res = pstmt.executeQuery();
            while (res.next()) {
                int id = res.getInt("id");
                String m5g = res.getString("msg");
                int userid = res.getInt("userid");

                msg = new Msg();
                msg.setId(id);
                msg.setMsg(m5g);
                msg.setUserId(userid);

                msgs.add(msg);
            }
        }

        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(res, sta, con);
        }
        return msgs;
    }

    public List<Msg> latest5Msg() {
        Connection con = null;
        Statement sta = null;
        ResultSet res = null;
        List<Msg> msgs = new ArrayList<>();

        try {
            con = JDBCUtils.getConnection();
            sta = con.createStatement();
            String sql = "select * from msgs order by id desc limit 5;";
            PreparedStatement pstmt = con.prepareStatement(sql);
            res = pstmt.executeQuery();
            while (res.next()) {
                int Id = res.getInt("id");
                String Msg = res.getString("msg");
                int userid = res.getInt("userid");

                Msg msg_tmp= new Msg();
                msg_tmp.setId(Id);
                msg_tmp.setMsg(Msg);
                msg_tmp.setUserId(userid);

                msgs.add(msg_tmp);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(res, sta, con);
        }

        return msgs;
    }


    public boolean send(Msg inf)
    {
        String msg = inf.getMsg();
        int userId = inf.getUserId();
        Connection con = null;
        Statement sta = null;
        ResultSet res = null;
        try {
            con = JDBCUtils.getConnection();
            sta = con.createStatement();
            if(msg.isEmpty()){
                return false;
            }

            String insertSql = "insert into msgs (msg,userid) values('"+msg+"','"+userId+"')";
            sta.executeUpdate(insertSql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(res, sta, con);
        }
        return true;
    }
}
