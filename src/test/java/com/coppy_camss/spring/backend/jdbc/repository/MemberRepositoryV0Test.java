package com.coppy_camss.spring.backend.jdbc.repository;

import com.coppy_camss.spring.backend.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class MemberRepositoryV0Test {
    MemberRepositoryV0 repository = new MemberRepositoryV0();
    @Test
    void crud () throws SQLException {
        Member member = new Member("memberV1",1000);
        //repository.save(member);
        //Member findMember = repository.findById(member.getMemberId());
        //log.info("member : "+member);
        //Assertions.assertThat(findMember).isEqualTo(member);

        repository.update(member.getMemberId(),20000);
        Member updatedMember = repository.findById(member.getMemberId());
        log.info("member is "+updatedMember);
    }

}