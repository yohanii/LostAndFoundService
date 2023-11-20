package com.yohanii.lostandfound.domain.user;

import lombok.Builder;
import org.springframework.stereotype.Repository;

import java.util.*;

//@Repository
public class UserMemoryRepository {

    private static Map<Long, User> store = new HashMap<>();
    private static long sequence = 0L;

    public User save(User user) {
//        User newUser = User.builder()
//                .id(++sequence)
//                .name(user.getName())
//                .loginId(user.getLoginId())
//                .password(user.getPassword())
//                .build();
//        store.put(newUser.getId(), newUser);
//        return newUser;
        return null;
    }

    public User findById(Long id) {
        return store.get(id);
    }

    public Optional<User> findByLoginId(String loginId) {
        return findAll().stream()
                .filter(user -> user.getLoginId().equals(loginId))
                .findFirst();
    }

    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
