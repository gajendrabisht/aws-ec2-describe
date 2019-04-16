package com.cisco.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Tag;
import com.cisco.aws.Ec2ClientProvider;
import com.cisco.domain.Ec2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Ec2Service {

    public static final int MAX_RESULTS_SIZE = 1000;

    @Autowired
    private Ec2ClientProvider ec2ClientProvider;

    public List<Ec2> getAllInstances(Regions region) {

        AmazonEC2 client = ec2ClientProvider.getClient(region);
        List<Ec2> ec2Instances = new ArrayList<>();

        String nextToken = null;
        do {
            DescribeInstancesRequest request = new DescribeInstancesRequest().withMaxResults(MAX_RESULTS_SIZE).withNextToken(nextToken);
            DescribeInstancesResult response = client.describeInstances(request);
            ec2Instances.addAll(getEc2Instances(response));
            nextToken = response.getNextToken();
        } while (nextToken != null);

        return ec2Instances;
    }

    private List<Ec2> getEc2Instances(DescribeInstancesResult response) {
        return response
                .getReservations()
                .stream()
                .flatMap(i -> i.getInstances().stream())
                .map(i ->
                        new Ec2(i.getTags().stream().map(Tag::getValue).collect(Collectors.joining(", ")),
                                i.getInstanceId(),
                                i.getInstanceType(),
                                i.getState().getName(),
                                i.getPlacement().getAvailabilityZone(),
                                i.getPublicIpAddress(),
                                i.getPrivateIpAddress()))
                .collect(Collectors.toList());
    }

}
