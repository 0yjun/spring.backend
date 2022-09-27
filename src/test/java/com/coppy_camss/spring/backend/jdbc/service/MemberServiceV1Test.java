package com.coppy_camss.spring.backend.jdbc.service;

import com.coppy_camss.spring.backend.jdbc.domain.Member;
import com.coppy_camss.spring.backend.jdbc.repository.MemberRepositoryV1;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.net.URL;
import java.sql.SQLException;

import static com.coppy_camss.spring.backend.jdbc.connection.ConnectionConst.*;

/**
 * 기본 동작, 트랜젝션이 없어서 문제 발생
 */
public class MemberServiceV1Test {
    public static final String member_A = "memberA";
    public static final String member_B = "memberB";
    public static final String member_EX = "ex";

    private MemberRepositoryV1 memberRepository;
    private MemberServiceV1 memberService;

    @BeforeEach
    void before(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL,USERNAME,PASSWORD);
        memberRepository = new MemberRepositoryV1(dataSource);
        memberService = new MemberServiceV1(memberRepository);
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
        Member memberEX = new Member(member_EX,10000);
        memberRepository.save(memberA);
        memberRepository.save(memberEX);
        //when
        //memberService.accountTransfer(memberA.getMemberId(),memberEX.getMemberId(),2000);
        Assertions.assertThatThrownBy(
                ()->memberService.accountTransfer(memberA.getMemberId(),memberEX.getMemberId(),2000)
        ).isInstanceOf(IllegalStateException.class);
        //then
        Member findMemberA = memberRepository.findById(memberA.getMemberId());
        Member findMemberB = memberRepository.findById(memberEX.getMemberId());
        Assertions.assertThat(findMemberA.getMoney()).isEqualTo(8000);
        Assertions.assertThat(findMemberB.getMoney()).isEqualTo(10000);
    }
}
