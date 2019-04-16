package com.cisco.controller;

import com.amazonaws.regions.Regions;
import com.cisco.domain.Ec2;
import com.cisco.domain.Order;
import com.cisco.domain.PaginationResult;
import com.cisco.service.Ec2Service;
import com.cisco.service.PaginationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.cisco.domain.Order.ascending;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class Ec2ControllerTest {

    @Mock
    Ec2Service ec2Service;
    @Mock
    PaginationService paginationService;

    @InjectMocks
    Ec2Controller ec2Controller;

    @Test
    public void shouldThrowExceptionWhenInvalidRegion() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
                ec2Controller.getAll("invalidRegion", "state", ascending, 1, 1));
        assertThat(thrown.getMessage(), is("No matching Region found for 'invalidRegion'. Allowed values: [us-gov-west-1, us-gov-east-1, us-east-1, us-east-2, us-west-1, us-west-2, eu-west-1, eu-west-2, eu-west-3, eu-central-1, eu-north-1, ap-south-1, ap-southeast-1, ap-southeast-2, ap-northeast-1, ap-northeast-2, sa-east-1, cn-north-1, cn-northwest-1, ca-central-1]."));
    }

    @Test
    public void shouldThrowExceptionWhenInvalidSortByAttribute() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
                ec2Controller.getAll(Regions.US_EAST_1.getName(), "invalidAttribute", ascending, 1, 1));
        assertThat(thrown.getMessage(), is("No matching EC2 attribute found for 'invalidAttribute'. Allowed values: [name, id, type, state, availabilityZone, publicIp, privateIp]."));
    }

    @ParameterizedTest(name = "page:{0}, pageSize:{1}, results:{2}")
    @CsvSource({
            "name, ascending, ec1|ec2|ec3",
            "name, descending, ec3|ec2|ec1",
            "id, ascending, ec2|ec1|ec3",
            "id, descending, ec3|ec1|ec2",
            "type, ascending, ec2|ec3|ec1",
            "type, descending, ec1|ec3|ec2",
            "state, ascending, ec3|ec1|ec2",
            "state, descending, ec2|ec1|ec3",
            "availabilityZone, ascending, ec3|ec1|ec2",
            "availabilityZone, descending, ec2|ec1|ec3",
            "publicIp, ascending, ec1|ec3|ec2",
            "publicIp, descending, ec2|ec3|ec1",
            "privateIp, ascending, ec2|ec3|ec1",
            "privateIp, descending, ec1|ec3|ec2",
    })
    public void shouldOrderByAttribute(String sortByAttribute, String order, String resultString) {
        Regions region = Regions.US_EAST_1;
        List<Ec2> ec2s = Arrays.asList(
                new Ec2("ec1", "id-8", "t2.medium", "Running", "us-east-1", "0.0.0.0", "9.9.9.9"),
                new Ec2("ec2", "id-7", "c4.large", "Terminated", "us-west-2", "8.8.8.8", "2.2.2.2"),
                new Ec2("ec3", "id-9", "t1.micro", "Pending", "eu-west-1", "1.1.1.1", "7.7.7.7")
        );
        List<String> expectedOrderedInstanceNames = Arrays.asList(resultString.split("\\|"));

        List<Ec2> expectedOrderedInstances = new ArrayList<>();
        expectedOrderedInstanceNames.stream().forEach(name -> {
            Optional<Ec2> match = ec2s.stream().filter(i -> i.getName().equals(name)).findFirst();
            if (match.isPresent())
                expectedOrderedInstances.add(match.get());
        });

        given(ec2Service.getAllInstances(region)).willReturn(ec2s);
        PaginationResult expectedResult = new PaginationResult(3, 1, 10, expectedOrderedInstances);
        given(paginationService.result(expectedOrderedInstances, 1, 10)).willReturn(expectedResult);

        PaginationResult actualResult = ec2Controller.getAll(region.getName(), sortByAttribute, Order.valueOf(order), 1, 10);

        assertThat(actualResult, is(expectedResult));
    }

}