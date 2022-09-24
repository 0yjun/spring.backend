package com.coppy_camss.spring.backend.jdbc.repository;

import com.coppy_camss.spring.backend.jdbc.connection.DBConnectionUtil;
import com.coppy_camss.spring.backend.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.sql.*;
import java.util.NoSuchElementException;

@Slf4j
public class MemberRepositoryV0 {
    public Member findById(String memberId)  {
        String sql = "select * from member where member_id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,memberId);
            rs = pstmt.executeQuery();
            if(rs.next()){
                Member member = new Member();
                member.setMemberId(rs.getString("member_Id"));
                member.setMoney(rs.getInt("money"));
                return member;
            }else{
                throw new NoSuchElementException("member not found memberId = "+memberId);
            }

        } catch (SQLException e) {
            log.info("error "+e);
            throw new RuntimeException(e);
        }finally {
            close(con,pstmt,rs);
        }


    }

    public void update(String memberId, int money){
        Connection con = null;
        PreparedStatement pstmt = null;
        String sql = "update member set money = ? where member_id = ?";


        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1,money);
            pstmt.setString(2,memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("rsultSize ={}",resultSize);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            close(con,pstmt,null);
        }
    }
    public Member save(Member member) throws SQLException {
        String sql = "insert into member values(?,?)";
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            close(con, pstmt, null);
        }
    }

    public void delete(String memberId){
        String sql = "delete from member where member_id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close(con,pstmt,null);
        }
    }

    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }

    private void close(Connection con, Statement stmt, ResultSet rs){
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
    }
}
