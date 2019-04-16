package com.cisco.domain;

import com.google.common.base.Objects;

public class Ec2 {

    private String name;
    private String id;
    private String type;
    private String state;
    private String availabilityZone;
    private String publicIp;
    private String privateIp;

    public Ec2(String name, String id, String type, String state, String availabilityZone, String publicIp, String privateIp) {
        this.name = name;
        this.id = id;
        this.type = type;
        this.state = state;
        this.availabilityZone = availabilityZone;
        this.publicIp = publicIp;
        this.privateIp = privateIp;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getState() {
        return state;
    }

    public String getAvailabilityZone() {
        return availabilityZone;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public String getPrivateIp() {
        return privateIp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ec2 ec2 = (Ec2) o;
        return Objects.equal(name, ec2.name) &&
                Objects.equal(id, ec2.id) &&
                Objects.equal(type, ec2.type) &&
                Objects.equal(state, ec2.state) &&
                Objects.equal(availabilityZone, ec2.availabilityZone) &&
                Objects.equal(publicIp, ec2.publicIp) &&
                Objects.equal(privateIp, ec2.privateIp);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, id, type, state, availabilityZone, publicIp, privateIp);
    }
}
