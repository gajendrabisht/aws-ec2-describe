package com.cisco.service;

import com.cisco.domain.Ec2;
import com.cisco.domain.PaginationResult;
import com.cisco.util.Ec2TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static com.cisco.util.Ec2TestUtils.createEc2;
import static java.util.Arrays.asList;
import static java.util.Collections.EMPTY_LIST;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaginationServiceTest {

    PaginationService paginationService = new PaginationService();

    @ParameterizedTest(name = "page:{0}, pageSize:{1}, exception:{2}")
    @CsvSource({
            "0, 1, Page must be greater than or equal to 1",
            "1, 0, Page Size must be greater than or equal to 1",
            "3, 1, Page number out of bounds",
            "2, 3, Page number out of bounds"
    })
    public void shouldThrowIllegalArgumentException(int page, int pageSize, String exceptionMessage) {
        List<Ec2> ec2s = asList(createEc2("ec1"), createEc2("ec2"));
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
                paginationService.result(ec2s, page, pageSize));
        assertThat(thrown.getMessage(), is(exceptionMessage));
    }

    @Test
    public void shouldReturnEmptyWhenNoResultsFound() {
        PaginationResult result = paginationService.result(EMPTY_LIST, 1, 1);
        assertThat(result.getResults(), is(EMPTY_LIST));
    }

    @ParameterizedTest(name = "page:{0}, pageSize:{1}, results:{2}")
    @CsvSource({
            "1, 2, ec1|ec2",
            "2, 2, ec3|ec4",
            "3, 2, ec5|ec6",
            "4, 2, ec7",
            "1, 3, ec1|ec2|ec3",
            "2, 3, ec4|ec5|ec6",
            "3, 3, ec7",
            "1, 4, ec1|ec2|ec3|ec4",
            "2, 4, ec5|ec6|ec7",
            "2, 4, ec5|ec6|ec7",
            "1, 7, ec1|ec2|ec3|ec4|ec5|ec6|ec7",
    })
    public void shouldReturnPaginatedResult(int page, int pageSize, String results) {
        List<Ec2> ec2s = asList(createEc2("ec1"), createEc2("ec2"), createEc2("ec3"),
                createEc2("ec4"), createEc2("ec5"), createEc2("ec6"), createEc2("ec7"));

        PaginationResult paginationResult = paginationService.result(ec2s, page, pageSize);

        assertThat(paginationResult.getPage(), is(page));
        assertThat(paginationResult.getPageSize(), is(pageSize));
        assertThat(paginationResult.getResultCount(), is(7));

        List<Ec2> expectedResult = asList(results.split("\\|")).stream().map(Ec2TestUtils::createEc2).collect(toList());
        assertThat(paginationResult.getResults(), is(expectedResult));
    }

}