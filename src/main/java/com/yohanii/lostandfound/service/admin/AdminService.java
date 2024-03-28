package com.yohanii.lostandfound.service.admin;

import com.yohanii.lostandfound.domain.member.Member;
import com.yohanii.lostandfound.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;

    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    @Transactional
    public void deleteMembers(List<Long> memberIds) {
        if (memberIds.isEmpty()) {
            return;
        }

        memberRepository.deleteAll(memberIds);
    }
}
