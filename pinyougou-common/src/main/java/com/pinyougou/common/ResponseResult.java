package com.pinyougou.common;

import java.io.Serializable;

public class ResponseResult<T> implements Serializable {

    private boolean success;
    private String message;
    private T data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private ResponseResult(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseResult<T> ok() {
        return newInstance(true, "", null);
    }

    public static <T> ResponseResult<T> ok(String msg) {
        return newInstance(true, msg, null);
    }

    public static <T> ResponseResult<T> ok(T data) {
        return newInstance(true, "", data);
    }

    public static <T> ResponseResult<T> error(String msg) {
        return newInstance(false, msg, null);
    }

    private static <T> ResponseResult<T> newInstance(boolean success, String msg, T data) {
        return new ResponseResult<>(success, msg, data);
    }
}