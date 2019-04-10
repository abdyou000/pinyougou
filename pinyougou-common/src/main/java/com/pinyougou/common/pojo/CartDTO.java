package com.pinyougou.common.pojo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

public class CartDTO implements Serializable {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private String username;

    public CartDTO() {
    }

    public CartDTO(HttpServletRequest request, HttpServletResponse response, String username) {
        this.request = request;
        this.response = response;
        this.username = username;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
