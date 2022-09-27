package com.coppy_camss.spring.backend.jdbc.service;

import com.coppy_camss.spring.backend.jdbc.domain.Member;
import com.coppy_camss.spring.backend.jdbc.repository.MemberRepositoryV2;
import com.coppy_camss.spring.backend.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * transaction - 트랜잭션 매니저
 */
@RequiredArgsConstructor
@Slf4j
public class MemberServiceV3_1 {
    //private final DataSource dataSource;
    private final PlatformTransactionManager transactionManager;
    private final MemberRepositoryV3 memberRepository;

    void bizLogic(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);
        memberRepository.update(fromId,fromMember.getMoney()-money);
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("이체중 오류 발생");
        }
        memberRepository.update(toId,fromMember.getMoney()+money);
    }


    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        //transaction 시작
        TransactionStatus status =  transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            bizLogic(fromId,toId,money);
            transactionManager.commit(status);
        }catch (Exception e){
            transactionManager.rollback(status);
            throw new IllegalStateException(e);
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
