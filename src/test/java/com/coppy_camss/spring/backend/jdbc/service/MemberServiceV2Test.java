package com.coppy_camss.spring.backend.jdbc.service;

import com.coppy_camss.spring.backend.jdbc.domain.Member;
import com.coppy_camss.spring.backend.jdbc.repository.MemberRepositoryV1;
import com.coppy_camss.spring.backend.jdbc.repository.MemberRepositoryV2;
import com.zaxxer.hikari.HikariDataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static com.coppy_camss.spring.backend.jdbc.connection.ConnectionConst.*;

/**
 * 커넷션 파라미터 전달방식 동기화
 */
public class MemberServiceV2Test {
    public static final String member_A = "memberA";
    public static final String member_B = "memberB";
    public static final String member_EX = "ex";

    private MemberRepositoryV2 memberRepository;
    private MemberServiceV2 memberService;

    @BeforeEach
    void before() throws SQLException {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        Connection con = dataSource.getConnection();
        memberRepository = new MemberRepositoryV2(dataSource);
        memberService = new MemberServiceV2(dataSource,memberRepository);
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
