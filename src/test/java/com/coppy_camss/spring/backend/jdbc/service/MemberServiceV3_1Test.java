package com.coppy_camss.spring.backend.jdbc.service;

import com.coppy_camss.spring.backend.jdbc.domain.Member;
import com.coppy_camss.spring.backend.jdbc.repository.MemberRepositoryV2;
import com.coppy_camss.spring.backend.jdbc.repository.MemberRepositoryV3;
import com.zaxxer.hikari.HikariDataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.Connection;
import java.sql.SQLException;

import static com.coppy_camss.spring.backend.jdbc.connection.ConnectionConst.*;

/**
 * 트랜잭션 매니저
 */
public class MemberServiceV3_1Test {
    public static final String member_A = "memberA";
    public static final String member_B = "memberB";
    public static final String member_EX = "ex";

    private MemberRepositoryV3 memberRepository;
    private MemberServiceV3_1 memberService;

    @BeforeEach
    void before() throws SQLException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL,USERNAME,PASSWORD);
        memberRepository = new MemberRepositoryV3(dataSource);
        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        memberService = new MemberServiceV3_1(transactionManager,memberRepository);
    }
    @AfterEach
    void after() throws SQLException {
        memberRepository.delete(member_A);
        memberRepository.delete(member_B);
        memberRepository.delete(member_EX);
    }
    @Test
    @DisplayName("정상이체")
    void accountTransfer() throws SQLException {
        //given
        Member memberA = new Member(member_A,10000);
        Member memberB = new Member(member_B ,10000);
        memberRepository.save(memberA);
        memberRepository.save(memberB);
        //when
        memberService.accountTransfer(memberA.getMemberId(),memberB.getMemberId(),2000);
//        Assertions.assertThatThrownBy(
//                ()->memberService.accountTransfer(memberA.getMemberId(),memberB.getMemberId(),2000)
//        ).isInstanceOf(IllegalStateException.class);
        //then
        Member findMemberA = memberRepository.findById(memberA.getMemberId());
        Member findMemberB = memberRepository.findById(memberB.getMemberId());
        Assertions.assertThat(findMemberA.getMoney()).isEqualTo(8000);
        Assertions.assertThat(findMemberB.getMoney()).isEqualTo(12000);
    }
}
