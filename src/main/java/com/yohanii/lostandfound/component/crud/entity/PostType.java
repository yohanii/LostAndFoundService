package com.yohanii.lostandfound.component.crud.entity;

public enum PostType {
    FOUND("찾은 분실물 등록"), LOST("잃어버린 분실물 등록");

    private final String name;

    PostType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
