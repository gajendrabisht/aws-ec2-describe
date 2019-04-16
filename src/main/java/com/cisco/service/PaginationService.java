package com.cisco.service;

import com.cisco.domain.Ec2;
import com.cisco.domain.PaginationResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaginationService {

    public PaginationResult result(List<Ec2> ec2s, int page, int pageSize) {

        if (isOutOfBoundForEmptyList(ec2s, page) || isOutOfBoundForNonEmptyList(ec2s, page, pageSize))
            throw new IllegalArgumentException("Page number out of bounds");

        int fromIndex = pageSize * (page - 1);
        int toIndex = Math.min(pageSize * page, ec2s.size());

        return new PaginationResult(ec2s.size(), page, pageSize, ec2s.subList(fromIndex, toIndex));

    }

    private boolean isOutOfBoundForNonEmptyList(List<Ec2> ec2s, int page, int pageSize) {
        return !ec2s.isEmpty() && ec2s.size() <= pageSize * (page - 1);
    }

    private boolean isOutOfBoundForEmptyList(List<Ec2> ec2s, int page) {
        return ec2s.isEmpty() && page > 1;
    }

}
