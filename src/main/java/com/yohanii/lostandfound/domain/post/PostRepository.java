package com.yohanii.lostandfound.domain.post;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    public Long save(Post post) {
        em.persist(post);
        return post.getId();
    }

    public Post findById(Long id) {
        return em.find(Post.class, id);
    }

    public List<Post> findAll() {
        return em.createQuery("select p from Post p", Post.class)
                .getResultList();
    }

    public void delete(Long postId) {
        em.remove(findById(postId));
    }

    public List<Post> findLostPosts() {
        return em.createQuery("select p from Post p where p.type =: type", Post.class)
                .setParameter("type", PostType.LOST)
                .getResultList();
    }

    public List<Post> findFoundPosts() {
        return em.createQuery("select p from Post p where p.type =: type", Post.class)
                .setParameter("type", PostType.FOUND)
                .getResultList();
    }

}
