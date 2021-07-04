package com.github.frtu.samples.enums;

public enum TestEnum implements IEnum, EnumsAndNotEnums {
    TENUM1(1, "First enum"),
    TENUM2(2, "Second enum"),
    TENUM3(3, "Third enum");

    private Integer index;
    private String description;

    TestEnum(Integer index, String description) {
        this.index = index;
        this.description = description;
    }

    public Integer getIndex() {
        return index;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
