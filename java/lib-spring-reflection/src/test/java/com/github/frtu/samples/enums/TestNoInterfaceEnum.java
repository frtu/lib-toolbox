package com.github.frtu.samples.enums;

public enum TestNoInterfaceEnum {
    ENUM1(1, "First enum"),
    ENUM2(2, "Second enum"),
    ENUM3(3, "Third enum");

    private Integer index;
    private String description;

    TestNoInterfaceEnum(Integer index, String description) {
        this.index = index;
        this.description = description;
    }

    public Integer getIndex() {
        return index;
    }

    public String getDescription() {
        return description;
    }
}
