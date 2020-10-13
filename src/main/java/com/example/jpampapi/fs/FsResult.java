package com.example.jpampapi.fs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class FsResult<E> {
    private Integer code = 1;
    private String message = "";
    private Long total;
    private List<E> data;
    public static final Integer CODE_SUCCESS = 1;
    public static final Integer CODE_FAILED = 2;
    public static final Integer CODE_PARTIAL_SUCCESS = 3;
    public static final Integer CODE_LOCK = 4;
    public static final Integer CODE_DIFFERENT_PLACE_LOGIN = 5;
    public static final Integer CODE_SERVER_DOWN = 6;
    public static final Integer CODE_PASSWORD_OVERDUE = 7;
    public static final Integer CODE_TOKEN_ERROR = 8;

    public static <E> FsResult<E> success() {
        return FsResult.<E>builder().code(CODE_SUCCESS).message("操作成功").build();
    }

    public static <E> FsResult<E> success(String message) {
        return FsResult.<E>builder().code(CODE_SUCCESS).message(message).build();
    }

    public static <E> FsResult<E> success(String message, E data) {
        return FsResult.<E>builder().code(CODE_SUCCESS).message(message).total(1L).data(Arrays.asList(data)).build();
    }

    public static <E> FsResult<E> success(String message, List<E> data) {
        return FsResult.<E>builder().code(CODE_SUCCESS).message(message).total(Long.valueOf(data.size())).data(data).build();
    }

    public static <E> FsResult<E> success(String message, List<E> data, Long total) {
        return FsResult.<E>builder().code(CODE_SUCCESS).message(message).total(total).data(data).build();
    }

    public static <E> FsResult<E> error() {
        return FsResult.<E>builder().code(CODE_FAILED).message("操作失败").build();
    }

    public static <E> FsResult<E> error(Integer code) {
        return FsResult.<E>builder().code(code).message("操作失败").build();
    }

    public static <E> FsResult<E> error(String message) {
        return FsResult.<E>builder().code(CODE_FAILED).message(message).total(1L).build();
    }

    public static <E> FsResult<E> error(Integer code, String message) {
        return FsResult.<E>builder().code(code).message(message).total(1L).build();
    }

    public static <E> FsResult<E> error(Integer code, String message, E data) {
        return FsResult.<E>builder().code(code).message(message).total(1L).data(Arrays.asList(data)).build();
    }

    public static <E> FsResult error(String message, List data) {
        return FsResult.<E>builder().code(CODE_FAILED).message(message).total(Long.valueOf(data.size())).data(data).build();
    }

    public static <E> FsResult<E> error(Integer code, String message, List<E> data) {
        return FsResult.<E>builder().code(code).message(message).total(Long.valueOf(data.size())).data(data).build();
    }

    public static <E> FsResult<E> error(Integer code, String message, List<E> data, Long total) {
        return FsResult.<E>builder().code(code).message(message).total(total).data(data).build();
    }

}
