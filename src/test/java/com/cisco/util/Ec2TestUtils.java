package com.cisco.util;

import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceState;
import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.Placement;
import com.cisco.domain.Ec2;

public class Ec2TestUtils {

    public static Ec2 createEc2(String name) {
        return new Ec2(name, "", "", "", "", "", "");
    }

    public static Instance createInstance(String name, String id, String type, String state, String avZone, String publicIp, String privateIp) {
        return new Instance()
                .withKeyName(name)
                .withInstanceId(id)
                .withInstanceType(InstanceType.fromValue(type))
                .withState(new InstanceState().withName(state))
                .withPlacement(new Placement().withAvailabilityZone(avZone))
                .withPublicIpAddress(publicIp)
                .withPrivateIpAddress(privateIp);
    }

}
