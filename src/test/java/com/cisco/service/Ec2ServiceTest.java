package com.cisco.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.*;
import com.cisco.aws.Ec2ClientProvider;
import com.cisco.domain.Ec2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.amazonaws.services.ec2.model.InstanceType.T2Medium;
import static com.cisco.util.Ec2TestUtils.createInstance;
import static java.util.Collections.EMPTY_LIST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class Ec2ServiceTest {

    @Mock
    private Ec2ClientProvider ec2ClientProvider;

    @InjectMocks
    private Ec2Service ec2Service;

    @Test
    public void shouldReturnEmptyWhenNoInstancesFound() {
        Regions region = Regions.US_EAST_1;
        mockEc2DescribeInstancesCall(region, EMPTY_LIST);

        List<Ec2> actualInstances = ec2Service.getAllInstances(region);

        assertThat(actualInstances, is(EMPTY_LIST));
    }

    @Test
    public void shouldReturnOneSingleInstance() {
        Regions region = Regions.US_EAST_1;
        Instance instance = new Instance()
                .withTags(new Tag("key1", "value1"), new Tag("key2", "value2"))
                .withInstanceId("someInstanceId")
                .withInstanceType(T2Medium)
                .withState(new InstanceState().withName("Running"))
                .withPlacement(new Placement().withAvailabilityZone("someAvailabilityZone"))
                .withPublicIpAddress("somePublicIpAddress")
                .withPrivateIpAddress("somePrivateIpAddress");

        mockEc2DescribeInstancesCall(region, Arrays.asList(instance));

        List<Ec2> actualInstances = ec2Service.getAllInstances(region);

        assertThat(actualInstances.size(), is(1));
        assertThat("Name (made by joining tag values)", actualInstances.get(0).getName(), is("value1, value2"));
        assertThat("Instance Id", actualInstances.get(0).getId(), is("someInstanceId"));
        assertThat("Instance Type", actualInstances.get(0).getType(), is(T2Medium.toString()));
        assertThat("Instance State", actualInstances.get(0).getState(), is("Running"));
        assertThat("Availability Zone", actualInstances.get(0).getAvailabilityZone(), is("someAvailabilityZone"));
        assertThat("Public IP Address", actualInstances.get(0).getPublicIp(), is("somePublicIpAddress"));
        assertThat("Private IP Address", actualInstances.get(0).getPrivateIp(), is("somePrivateIpAddress"));
    }

    @Test
    public void shouldReturnMultipleInstances() {
        Regions region = Regions.US_EAST_1;

        List<Instance> instances = IntStream.range(1, 4).mapToObj(i ->
                createInstance("ec" + i, "id" + i, T2Medium.toString(), "Running" + i,
                        "us-east-" + i, "publicIp" + i, "privateIp" + i)
        ).collect(Collectors.toList());

        mockEc2DescribeInstancesCall(region, instances);

        List<Ec2> actualInstances = ec2Service.getAllInstances(region);

        assertThat(actualInstances.size(), is(instances.size()));
        IntStream.range(1, 4).forEach(i -> {
            assertThat(actualInstances.get(i - 1).getName(), is("ec" + i));
            assertThat(actualInstances.get(i - 1).getId(), is("id" + i));
            assertThat(actualInstances.get(i - 1).getType(), is(T2Medium.toString()));
            assertThat(actualInstances.get(i - 1).getState(), is("Running" + i));
            assertThat(actualInstances.get(i - 1).getAvailabilityZone(), is("us-east-" + i));
            assertThat(actualInstances.get(i - 1).getPublicIp(), is("publicIp" + i));
            assertThat(actualInstances.get(i - 1).getPrivateIp(), is("privateIp" + i));
        });
    }


    private void mockEc2DescribeInstancesCall(Regions region, List<Instance> ec2Instances) {
        AmazonEC2 client = mock(AmazonEC2.class);
        given(ec2ClientProvider.getClient(region)).willReturn(client);
        DescribeInstancesResult response = mock(DescribeInstancesResult.class);
        given(client.describeInstances(any(DescribeInstancesRequest.class))).willReturn(response);
        Reservation reservation = mock(Reservation.class);
        given(response.getReservations()).willReturn(Arrays.asList(reservation));
        given(reservation.getInstances()).willReturn(ec2Instances);
        given(response.getNextToken()).willReturn(null);
    }

}