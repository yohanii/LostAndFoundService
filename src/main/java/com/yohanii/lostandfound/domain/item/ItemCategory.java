package com.yohanii.lostandfound.domain.item;

public enum ItemCategory {
    SMART_PHONE("스마트폰"), WALLET("지갑");

    private final String name;

    ItemCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
