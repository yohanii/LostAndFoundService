package com.yohanii.lostandfound.component.init;

import com.yohanii.lostandfound.component.crud.dto.post.PostSaveInfoDto;
import com.yohanii.lostandfound.component.crud.entity.ItemCategory;
import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.entity.MemberAuth;
import com.yohanii.lostandfound.component.crud.entity.PostType;
import com.yohanii.lostandfound.component.crud.repository.MemberRepository;
import com.yohanii.lostandfound.component.crud.repository.PostRepository;
import com.yohanii.lostandfound.component.crud.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitService {

    private final MemberRepository memberRepository;
    private final PostService postService;
    private final PostRepository postRepository;

    public static final int INIT_MEMBER_COUNT = 1000;
    public static final long INIT_POST_COUNT = 1000;
    public static final Map<String, Integer> itemImages = Map.of(
            "airpot.jpeg", 3,
            "hat.jpg", 2,
            "phone.jpeg", 2,
            "wallet.jpeg", 1,
            "watch.jpg", 2
    );

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

    public long fillPosts() {

        if (postRepository.count() >= INIT_POST_COUNT) {
            return 0L;
        }

        Map<String, byte[]> contents = getContents();

        long index = 0L;
        List<Member> members = memberRepository.findAll();
        for (Member member: members) {
            for (PostType type: PostType.values()) {
                for (String key: itemImages.keySet()) {
                    List<MultipartFile> files = getFiles(key, contents);

                    PostSaveInfoDto dto = new PostSaveInfoDto(
                            member.getId(),
                            "testTitle" + index,
                            "testContent" + index,
                            type,
                            "testItemPlace" + index,
                            ItemCategory.ETC,
                            files);
                    postService.savePost(dto);

                    index++;
                }
            }
        }

        return index;
    }

    private Map<String, byte[]> getContents() {

        Map<String, byte[]> contents = new HashMap<>();

        for (String key : itemImages.keySet()) {
            int count = itemImages.get(key);
            String[] split = key.split("\\.");

            for (int index = 1; index <= count; index++) {
                String name = split[0] + index + "." + split[1];
                Path path = Paths.get("src/main/resources/static/img/item/" + name);
                byte[] content = null;

                try {
                    content = Files.readAllBytes(path);
                    log.info("getFiles content.length = {}", content.length);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                contents.put(name, content);
            }
        }

        return contents;
    }

    private List<MultipartFile> getFiles(String key, Map<String, byte[]> contents) {
        List<MultipartFile> result = new ArrayList<>();

        int count = itemImages.get(key);
        String[] split = key.split("\\.");

        for (int index = 1; index <= count; index++) {
            String name = split[0] + index + "." + split[1];
            result.add(new MockMultipartFile(name, name, "text/plain", contents.getOrDefault(name, null)));
        }

        return result;
    }
}
