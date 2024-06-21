package com.yohanii.lostandfound.component.crud.repository;

import com.yohanii.lostandfound.component.crud.entity.Post;
import com.yohanii.lostandfound.component.crud.entity.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom{

    long countByType(PostType postType);
    Page<Post> findAllByType(PostType postType, Pageable pageable);
    List<Post> findAllByMemberId(Long id);

    @Modifying
    @Query("delete from Post p where p.id in :postIds")
    void deleteAll(@Param("postIds") List<Long> postIds);

}
