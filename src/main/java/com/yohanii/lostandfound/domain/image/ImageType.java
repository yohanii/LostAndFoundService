package com.yohanii.lostandfound.domain.image;

public enum ImageType {
    GENERAL("general/"), MEMBER("member/"), ITEM("item/");

    private final String s3Path;

    ImageType(String s3Path) {
        this.s3Path = s3Path;
    }

    public String getS3Path() {
        return s3Path;
    }
}
