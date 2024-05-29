package com.yohanii.lostandfound.component.crud.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yohanii.lostandfound.component.crud.dto.post.PostSearchRequestDto;
import com.yohanii.lostandfound.component.crud.entity.Post;
import com.yohanii.lostandfound.component.crud.entity.PostType;
import com.yohanii.lostandfound.component.crud.entity.QPost;
import jakarta.persistence.EntityManager;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.yohanii.lostandfound.component.crud.entity.QPost.post;

public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Post> findAllByPostSearchRequestDto(PostSearchRequestDto dto) {

        QPost post = QPost.post;

        return queryFactory
                .select(post)
                .from(post)
                .where(typeEq(dto.getType()),
                        contentContains(dto.getContent()))
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
