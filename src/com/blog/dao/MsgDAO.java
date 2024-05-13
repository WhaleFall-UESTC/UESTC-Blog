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
            String sql = "select * from msgs;";
            PreparedStatement pstmt = con.prepareStatement(sql);
            res = pstmt.executeQuery();
            while (res.next()) {
                int id = res.getInt("id");
                String m5g = res.getString("msg");

                msg = new Msg();
                msg.setId(id);
                msg.setMsg(m5g);

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
    public boolean send(Msg inf)
    {
        String msg = inf.getMsg();
        Connection con = null;
        Statement sta = null;
        ResultSet res = null;
        try {
            con = JDBCUtils.getConnection();
            sta = con.createStatement();
            if(msg.isEmpty()){
                return false;
            }

            String insertSql = "insert into msgs (msg) values " +
                    "('"+msg+"')";
            sta.executeUpdate(insertSql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(res, sta, con);
        }
        return true;
    }
}
