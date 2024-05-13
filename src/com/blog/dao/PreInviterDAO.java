package com.blog.dao;

import com.blog.bean.Msg;
import com.blog.bean.PreInviter;
import com.blog.utils.JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PreInviterDAO {
    public List<PreInviter> selectall() {
        Connection con = null;
        Statement sta = null;
        ResultSet res = null;

        PreInviter preInviter = null;
        List<PreInviter> preInviters = new ArrayList<>();
        try {
            con = JDBCUtils.getConnection();
            sta = con.createStatement();
            String sql = "select * from pre_inviters;";
            PreparedStatement pstmt = con.prepareStatement(sql);
            res = pstmt.executeQuery();
            while (res.next()) {
                int id = res.getInt("id");
                String name = res.getString("name");
                String email = res.getString("email");
                int userid = res.getInt("userid");

                preInviter = new PreInviter();
                preInviter.setId(id);
                preInviter.setName(name);
                preInviter.setEmail(email);
                preInviter.setUserid(userid);

                preInviters.add(preInviter);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(res, sta, con);
        }
        return preInviters;
    }

    public void delete(int userid)
    {
        Connection con = null;
        Statement sta = null;
        ResultSet res = null;
        try {
            con = JDBCUtils.getConnection();
            sta = con.createStatement();
            String sql = "delete from pre_inviters where userid ='"+userid+"'";
            PreparedStatement pstmt = con.prepareStatement(sql);
            int count = pstmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(res, sta, con);
        }
    }

}
