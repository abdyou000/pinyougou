package com.pinyougou.common.pojo;

import java.io.Serializable;
import java.util.List;

public class PageResult<T> implements Serializable {

    private long total;
    private List<T> rows;

    public PageResult() {
    }

    public PageResult(long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public PageResult withTotal(long total) {
        this.total = total;
        return this;
    }

    public PageResult withRows(List<T> rows) {
        this.rows = rows;
        return this;
    }
}
