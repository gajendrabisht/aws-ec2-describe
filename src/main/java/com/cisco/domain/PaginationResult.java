package com.cisco.domain;

import java.util.List;

public class PaginationResult {

    private int resultCount;
    private int page;
    private int pageSize;
    private List<Ec2> results;

    public PaginationResult(int resultCount, int page, int pageSize, List<Ec2> results) {
        this.resultCount = resultCount;
        this.page = page;
        this.pageSize = pageSize;
        this.results = results;
    }

    public int getResultCount() {
        return resultCount;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public List<Ec2> getResults() {
        return results;
    }
}
