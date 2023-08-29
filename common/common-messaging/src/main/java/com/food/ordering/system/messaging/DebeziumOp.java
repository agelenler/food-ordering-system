package com.food.ordering.system.messaging;

public enum DebeziumOp {

    CREATE("c"), UPDATE("u"), DELETE("d");

    private String value;
    DebeziumOp(String val) {
        this.value = val;
    }

    public String getValue() {
        return value;
    }
}
