package com.example.jpampapi.fs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataCondition<T, M> {
    private T data;
    private M condition;
}
