package com.coppy_camss.spring.backend.jdbc.service;

import com.coppy_camss.spring.backend.jdbc.domain.Member;
import com.coppy_camss.spring.backend.jdbc.repository.MemberRepositoryV1;
import com.coppy_camss.spring.backend.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * transaction - 파라미터 연동, 풀을 고려한 종료
 */
@RequiredArgsConstructor
@Slf4j
public class MemberServiceV2 {
    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    void bizLogic(Connection con,String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(con,fromId);
        Member toMember = memberRepository.findById(con,toId);
        memberRepository.update(fromId,fromMember.getMoney()-money);
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("이체중 오류 발생");
        }
        memberRepository.update(toId,fromMember.getMoney()+money);
    }


    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            con.setAutoCommit(false);
            bizLogic(con,fromId,toId,money);
            con.commit();
        }catch (Exception e){
            con.rollback();
            throw new IllegalStateException(e);
        }finally {
            release(con);
        }
    }

    void release(Connection con){
        if(con != null){
            try {
                con.setAutoCommit(true);
                con.close();
            }catch (Exception e){
                log.info("error",e);
            }
        }
    }
}
