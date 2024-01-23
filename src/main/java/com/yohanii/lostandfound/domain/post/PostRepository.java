package com.yohanii.lostandfound.domain.post;

import com.yohanii.lostandfound.dto.post.PostSearchRequestDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.thymeleaf.util.StringUtils;

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

    public List<Post> findAll(PostSearchRequestDto dto) {

        String jpql = "select p from Post p";
        boolean isFirstCondition = true;

        if (dto.getType() != null) {
            if (isFirstCondition) {
                jpql += " where ";
                isFirstCondition = false;
            } else {
                jpql += " and ";
            }
            jpql += "p.type = :type";
        }

        if (!StringUtils.isEmptyOrWhitespace(dto.getContent())) {
            if (isFirstCondition) {
                jpql += " where ";
                isFirstCondition = false;
            } else {
                jpql += " and ";
            }
            jpql += "p.content like concat('%', :content, '%')";
        }

        TypedQuery<Post> query = em.createQuery(jpql, Post.class)
                .setMaxResults(1000);

        if (dto.getType() != null) {
            query = query.setParameter("type", dto.getType());
        }
        if (!StringUtils.isEmptyOrWhitespace(dto.getContent())) {
            query = query.setParameter("content", dto.getContent());
        }

        return query.getResultList();
    }

    public List<Post> findAll(long userId) {
        return em.createQuery("select p from Post p where p.user.id =: userId", Post.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
