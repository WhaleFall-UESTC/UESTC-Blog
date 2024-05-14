package com.blog.dao;

import com.blog.bean.Msg;
import com.blog.bean.Nts;
import com.blog.utils.JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NtsDAO {
    public List<Nts> selectall() {
        Connection con = null;
        Statement sta = null;
        ResultSet res = null;

        Nts nts = null;
        List<Nts> ntss = new ArrayList<>();
        try {
            con = JDBCUtils.getConnection();
            sta = con.createStatement();
            String sql = "select * from ntss;";
            PreparedStatement pstmt = con.prepareStatement(sql);
            res = pstmt.executeQuery();
            while (res.next()) {
                int id = res.getInt("id");
                String m5g = res.getString("nts");

                nts = new Nts();
                nts.setId(id);
                nts.setNts(m5g);

                ntss.add(nts);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(res, sta, con);
        }
        return ntss;
    }

    public String latestNts() {
        Connection con = null;
        Statement sta = null;
        ResultSet res = null;

        try {
            con = JDBCUtils.getConnection();
            sta = con.createStatement();
            String sql = "select * from ntss order by id desc limit 1;";
            PreparedStatement pstmt = con.prepareStatement(sql);
            res = pstmt.executeQuery();
            if (res.next()) {
                String Nts = res.getString("nts");
                return Nts;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(res, sta, con);
        }

        return null;
    }

    public boolean send(Nts inf)
    {
        String nts = inf.getNts();
        Connection con = null;
        Statement sta = null;
        ResultSet res = null;
        try {
            con = JDBCUtils.getConnection();
            sta = con.createStatement();
            if(nts.isEmpty()){
                return false;
            }

            String insertSql = "insert into ntss (nts) values " +
                    "('"+nts+"')";
            sta.executeUpdate(insertSql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(res, sta, con);
        }
        return true;
    }
}