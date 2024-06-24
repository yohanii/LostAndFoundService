package com.yohanii.lostandfound.component.crud.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yohanii.lostandfound.component.crud.entity.Post;
import com.yohanii.lostandfound.component.crud.entity.PostType;
import com.yohanii.lostandfound.component.crud.entity.QPost;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.yohanii.lostandfound.component.crud.entity.QPost.post;

public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Post> findAllByTypeAndContent(PostType postType, String content, Pageable pageable) {

        QPost post = QPost.post;

        List<Post> pageContent = getPageContent(postType, content, post, pageable);

        Long count = getCount(postType, content, post);

        return new PageImpl<>(pageContent, pageable, count);
    }

    private Long getCount(PostType postType, String content, QPost post) {
        return queryFactory
                .select(post.count())
                .from(post)
                .where(typeEq(postType),
                        contentContains(content))
                .fetchOne();
    }

    private List<Post> getPageContent(PostType postType, String content, QPost post, Pageable pageable) {
        return queryFactory
                .select(post)
                .from(post)
                .where(typeEq(postType),
                        contentContains(content))
                .offset(pageable.getOffset())
                .orderBy(post.createdTime.desc())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression typeEq(PostType type) {
        if (type == null) {
            return null;
        }
        return post.type.eq(type);
    }

    private BooleanExpression contentContains(String content) {
        if (!StringUtils.hasText(content)) {
            return null;
        }
        return post.content.contains(content);
    }

}
