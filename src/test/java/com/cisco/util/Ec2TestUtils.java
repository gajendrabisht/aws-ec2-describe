package com.cisco.util;

import com.amazonaws.services.ec2.model.*;
import com.cisco.domain.Ec2;

public class Ec2TestUtils {

    public static Ec2 createEc2(String name) {
        return new Ec2(name, "", "", "", "", "", "");
    }

    public static Instance createInstance(String name, String id, String type, String state, String avZone, String publicIp, String privateIp) {
        return new Instance()
                .withTags(new Tag("key", name))
                .withInstanceId(id)
                .withInstanceType(InstanceType.fromValue(type))
                .withState(new InstanceState().withName(state))
                .withPlacement(new Placement().withAvailabilityZone(avZone))
                .withPublicIpAddress(publicIp)
                .withPrivateIpAddress(privateIp);
    }

}
