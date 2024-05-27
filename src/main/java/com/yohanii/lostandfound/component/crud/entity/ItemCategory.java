package com.yohanii.lostandfound.component.crud.entity;

public enum ItemCategory {
    ETC("기타"), SMART_PHONE("스마트폰"), WALLET("지갑");

    private final String name;

    ItemCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
