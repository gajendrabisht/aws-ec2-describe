package com.cisco.controller;

import com.amazonaws.regions.Regions;
import com.cisco.domain.Ec2;
import com.cisco.domain.Ec2Attribute;
import com.cisco.domain.Order;
import com.cisco.domain.PaginationResult;
import com.cisco.service.Ec2Service;
import com.cisco.service.PaginationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(path = "/ec2s", produces = APPLICATION_JSON)
public class Ec2Controller {

    @Autowired
    private Ec2Service ec2Service;

    @Autowired
    private PaginationService paginationService;

    @RequestMapping(method = GET)
    public PaginationResult getAll(@RequestParam(required = false, defaultValue = "us-east-1") String region,
                                   @RequestParam(required = false, defaultValue = "state") String sortBy,
                                   @RequestParam(required = false, defaultValue = "ascending") Order order,
                                   @RequestParam(required = false, defaultValue = "1") Integer page,
                                   @RequestParam(required = false, defaultValue = "10") Integer pageSize) {

        List<Ec2> ec2s = ec2Service.getAllInstances(resolveRegion(region));

        ec2s.sort(Comparator.comparing(Ec2Attribute.fromName(sortBy).getFunction()));

        if (order.equals(Order.descending))
            Collections.reverse(ec2s);

        return paginationService.result(ec2s, page, pageSize);
    }

    private Regions resolveRegion(String region) {
        Regions regionResolved;
        try {
            regionResolved = Regions.fromName(region);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(String.format("No matching Region found for '%s'. Allowed values: [%s].", region,
                    Arrays.stream(Regions.values()).map(Regions::getName).collect(Collectors.joining(", "))));
        }
        return regionResolved;
    }

}
