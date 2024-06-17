package com.yohanii.lostandfound.component.init;

import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.entity.MemberAuth;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class InitService {

    private final MemberRepository memberRepository;

    public static final int INIT_MEMBER_COUNT = 1000;

    @Transactional
    public boolean saveAdmin() {

        if (memberRepository.findByLoginId("admin").isPresent()) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        Member admin = Member.builder()
                .loginId("admin")
                .password("admin")
                .name("admin")
                .nickName("admin")
                .auth(MemberAuth.ADMIN)
                .createdTime(now)
                .updatedTime(now)
                .build();

        memberRepository.save(admin);

        return true;
    }

    @Transactional
    public int fillMembers() {

        if (memberRepository.count() >= INIT_MEMBER_COUNT) {
            return 0;
        }

        ;
        return memberRepository.saveAll(
                        IntStream.range(0, INIT_MEMBER_COUNT)
                                .mapToObj(index -> {
                                    LocalDateTime now = LocalDateTime.now();

                                    return Member.builder()
                                            .loginId("testLoginId" + index)
                                            .password("testPassword" + index)
                                            .name("testName" + index)
                                            .nickName("testNickName" + index)
                                            .auth(MemberAuth.MEMBER)
                                            .createdTime(now)
                                            .updatedTime(now)
                                            .build();
                                })
                                .toList())
                .size();
    }

    @Transactional
    public int fillPosts() {

        return 0;
    }
}
