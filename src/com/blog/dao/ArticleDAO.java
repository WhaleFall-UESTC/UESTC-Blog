package com.blog.dao;

import com.blog.bean.Article;
import com.blog.utils.JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleDAO {
    public List<Article> selectPart(int minId, int maxId) {
        List<Article> articles = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;

        try {
            con = JDBCUtils.getConnection();
            String sql = "select * from articles where id >= ? and id <= ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, minId);
            pstmt.setInt(2, maxId);
            res = pstmt.executeQuery();

            while (res.next()) {
                Article article = new Article();
                int id = res.getInt("id");
                String title = res.getString("title");
                String descr = res.getString("descr");
                String time = res.getString("time");
                article.setArticle(id, title, descr, time);
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(res, pstmt, con);
        }

        return articles;
    }

    public boolean insert(Article article) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = JDBCUtils.getConnection();
            String sql = "INSERT INTO articles (id, title, descr, time) VALUES (?, ?, ?, ?);";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, article.getId());
            pstmt.setString(2, article.getTitle());
            pstmt.setString(3, article.getDescription());
            pstmt.setString(4, article.getArticleTime());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(pstmt, con);
        }
        return false;
    }

    public boolean update(int Id, String column, String newData) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = JDBCUtils.getConnection();
            String sql = "UPDATE articles SET " + column + " = ? WHERE id = ?;";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, newData);
            pstmt.setInt(2, Id);

            int affectedRows = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(pstmt, con);
        }
        return false;
    }

    public boolean delete(int Id) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = JDBCUtils.getConnection();
            String sql = "DELETE FROM articles WHERE id = ?;";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, Id);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(pstmt, con);
        }
        return false;
    }

    public int countArticles() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        int count = 0;

        try {
            con = JDBCUtils.getConnection();
            String sql = "SELECT COUNT(*) FROM articles;";
            pstmt = con.prepareStatement(sql);
            res = pstmt.executeQuery();

            if (res.next()) {
                count = res.getInt(1); // 获取统计结果
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(res, pstmt, con);
        }
        return count;
    }

    public List<String> getTimeSpan() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;

        List<String> list = new ArrayList<>();
        try {
            con = JDBCUtils.getConnection();
            String sql = "SELECT Min(time) AS min_time, MAX(time) AS max_time FROM articles;";
            pstmt = con.prepareStatement(sql);
            res = pstmt.executeQuery();

            if (res.next()) {
                list.add(res.getString("min_time"));
                list.add(res.getString("max_time"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(res, pstmt, con);
        }
        return list;
    }

    public List<Article> selectArticlesByTime(String YMTime) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet res = null;
        List<Article> articles = new ArrayList<>();

        try {
            con = JDBCUtils.getConnection();
            String sql = "select * from articles where DATE_FORMAT(time, '%Y-%m') = ? ORDER BY time DESC;";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, YMTime);
            res = pstmt.executeQuery();

            while (res.next()) {
                Article article = new Article();
                int id = res.getInt("id");
                String title = res.getString("title");
                String descr = res.getString("descr");
                String time = res.getString("time");
                article.setArticle(id, title, descr, time);

                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(res, pstmt, con);
        }

        return articles;
    }

    public List<String> findDifferentYearMonths() {
        List<String> yearMonths = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JDBCUtils.getConnection(); // 获取数据库连接
            String sql = "SELECT DATE_FORMAT(time, '%Y-%m') AS l FROM articles GROUP BY l ORDER BY l DESC;";
            preparedStatement = connection.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String yearMonth = resultSet.getString("l");
                yearMonths.add(yearMonth);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(resultSet, preparedStatement, connection); // 关闭资源
        }
        return yearMonths;
    }
}