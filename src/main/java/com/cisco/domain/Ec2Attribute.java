package com.cisco.domain;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Ec2Attribute {
    NAME("name", Ec2::getName),
    ID("id", Ec2::getId),
    TYPE("type", Ec2::getType),
    STATE("state", Ec2::getState),
    AVAILABILITY_ZONE("availabilityZone", Ec2::getAvailabilityZone),
    PUBLIC_IP("publicIp", Ec2::getPublicIp),
    PRIVATE_IP("privateIp", Ec2::getPrivateIp);

    private String name;
    Function<Ec2, String> function;

    Ec2Attribute(String name, Function<Ec2, String> function) {
        this.name = name;
        this.function = function;
    }

    public String getName() {
        return name;
    }

    public Function<Ec2, String> getFunction() {
        return function;
    }

    public static Ec2Attribute fromName(String name) {
        Optional<Ec2Attribute> match = Arrays.stream(Ec2Attribute.values()).filter(i -> i.getName().equalsIgnoreCase(name)).findFirst();
        if (match.isPresent())
            return match.get();
        throw new IllegalArgumentException(
                String.format("No matching EC2 attribute found for '%s'. Allowed values: [%s].", name,
                        Arrays.stream(Ec2Attribute.values()).map(Ec2Attribute::getName).collect(Collectors.joining(", "))));
    }

}
