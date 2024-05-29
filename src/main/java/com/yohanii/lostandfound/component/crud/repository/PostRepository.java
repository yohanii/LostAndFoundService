package com.yohanii.lostandfound.component.crud.repository;

import com.yohanii.lostandfound.component.crud.dto.post.PostSearchRequestDto;
import com.yohanii.lostandfound.component.crud.entity.Post;
import com.yohanii.lostandfound.component.crud.entity.PostType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    long countByType(PostType postType);
    List<Post> findAllByType(PostType postType);
    List<Post> findAllByMemberId(Long id);

    @Modifying
    @Query("delete from Post p where p.id in :postIds")
    void deleteAll(@Param("postIds") List<Long> postIds);

    @Query("select p from Post p")
    List<Post> findAllByPostSearchRequestDto(PostSearchRequestDto dto);

//    public List<Post> findAll(PostSearchRequestDto dto) {
//
//        String jpql = "select p from Post p";
//        boolean isFirstCondition = true;
//
//        if (dto.getType() != null) {
//            if (isFirstCondition) {
//                jpql += " where ";
//                isFirstCondition = false;
//            } else {
//                jpql += " and ";
//            }
//            jpql += "p.type = :type";
//        }
//
//        if (!StringUtils.isEmptyOrWhitespace(dto.getContent())) {
//            if (isFirstCondition) {
//                jpql += " where ";
//                isFirstCondition = false;
//            } else {
//                jpql += " and ";
//            }
//            jpql += "p.content like concat('%', :content, '%')";
//        }
//
//        jpql += " order by p.createdTime desc";
//        TypedQuery<Post> query = em.createQuery(jpql, Post.class)
//                .setMaxResults(1000);
//
//        if (dto.getType() != null) {
//            query = query.setParameter("type", dto.getType());
//        }
//        if (!StringUtils.isEmptyOrWhitespace(dto.getContent())) {
//            query = query.setParameter("content", dto.getContent());
//        }
//
//        return query.getResultList();
//    }
}
