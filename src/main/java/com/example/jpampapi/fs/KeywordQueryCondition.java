package com.example.jpampapi.fs;


public class KeywordQueryCondition {
    private String keyValue;
    private String columnNames;

    public KeywordQueryCondition() {
    }

    public KeywordQueryCondition(String keyValue, String columnNames) {
        this.keyValue = keyValue;
        this.columnNames = columnNames;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public String getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String columnNames) {
        this.columnNames = columnNames;
    }
}
