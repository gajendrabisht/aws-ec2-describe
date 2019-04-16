package com.cisco.aws;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Ec2ClientProvider {

    @Autowired
    CredentialsProvider credentialsProvider;

    private Map<Regions, AmazonEC2> ec2Clients = new HashMap<>();

    public AmazonEC2 getClient(Regions region) {
        if (!ec2Clients.containsKey(region)) {
            ec2Clients.put(region, createClientForRegion(region));
        }
        return ec2Clients.get(region);
    }

    private AmazonEC2 createClientForRegion(Regions region) {
        return AmazonEC2ClientBuilder.standard().withRegion(region).withCredentials(credentialsProvider.get()).build();
    }

}
