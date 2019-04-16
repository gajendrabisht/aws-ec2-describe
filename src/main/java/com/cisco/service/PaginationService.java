package com.cisco.service;

import com.cisco.domain.Ec2;
import com.cisco.domain.PaginationResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaginationService {

    public PaginationResult result(List<Ec2> ec2s, int page, int pageSize) {

        if (page < 1)
            throw new IllegalArgumentException("Page must be greater than or equal to 1");
        if (pageSize < 1)
            throw new IllegalArgumentException("Page Size must be greater than or equal to 1");
        if (ec2s.size() > 0 && ec2s.size() <= pageSize * (page - 1))
            throw new IllegalArgumentException("Page number out of bounds");

        int fromIndex = pageSize * (page - 1);
        int toIndex = Math.min(pageSize * page, ec2s.size());

        return new PaginationResult(ec2s.size(), page, pageSize, ec2s.subList(fromIndex, toIndex));

    }

}
