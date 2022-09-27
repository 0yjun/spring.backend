package com.coppy_camss.spring.backend.jdbc.service;

import com.coppy_camss.spring.backend.jdbc.domain.Member;
import com.coppy_camss.spring.backend.jdbc.repository.MemberRepositoryV1;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;

@RequiredArgsConstructor
public class MemberServiceV1 {
    private final MemberRepositoryV1 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId,fromMember.getMoney()-money);
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("이체중 오류 발생");
        }
        memberRepository.update(toId,fromMember.getMoney()+money);
    }
}
