package com.cisco.cucumber.steps;

import com.amazonaws.regions.Regions;
import com.cisco.cucumber.CucumberTestApplication;
import com.cisco.cucumber.CukeWorld;
import com.cisco.domain.Ec2;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpMethod.GET;

public class CukeStepDefs extends CucumberTestApplication {

    private CukeWorld cukeWorld;

    public CukeStepDefs() {
        this.cukeWorld = new CukeWorld();
    }

    @Given("^ec2 instances exist in region '(.*)'$")
    public void ec2_instances_exist_in_region(String region, DataTable table) {
        ArrayList<Ec2> ec2s = new ArrayList<>();
        table.asMaps().forEach(map ->
                ec2s.add(new Ec2(
                        map.getOrDefault("name", "someName"),
                        map.getOrDefault("id", "someId"),
                        map.getOrDefault("type", "someType"),
                        map.getOrDefault("state", "someState"),
                        map.getOrDefault("availabilityZone", "someAvailabilityZone"),
                        map.getOrDefault("publicIp", "somePublicIp"),
                        map.getOrDefault("privateIp", "somePrivateIp")
                ))
        );
        given(this.ec2Service.getAllInstances(Regions.fromName(region))).willReturn(ec2s);
    }

    @When("^I request ec2s with query params as$")
    public void i_request_ec2s_for_region(DataTable table) {
        Map<String, String> map = table.asMaps().get(0);
        String queryString = map.keySet().stream().map(key -> String.format("%s=%s", key, map.get(key))).collect(Collectors.joining("&"));
        ResponseEntity<String> response = restTemplate.exchange(String.format("%s/ec2s?%s", baseUrl(), queryString), GET, null, String.class);
        cukeWorld.setResponseStatus(response.getStatusCodeValue());
        cukeWorld.setResponseBody(response.getBody());
    }

    @Then("^I should get response status (\\d+)$")
    public void i_should_get_response_status(int responseStatusCode) {
        assertThat(cukeWorld.getResponseStatus(), is(responseStatusCode));
    }

    @Then("^I should get error message '(.*)'$")
    public void i_should_get_errorMessage(String errorMessage) {
        assertThat(cukeWorld.getResponseBody(), is(errorMessage));
    }

    @Then("^I should get response body as$")
    public void i_should_get_response_body_as(DataTable table) throws JSONException {
        JSONObject actual = new JSONObject(cukeWorld.getResponseBody());
        Map<String, String> expected = table.asMaps().get(0);
        Arrays.asList("resultCount", "page", "pageSize").stream().forEach(key ->
                assertThat(actual.optInt(key), is(Integer.parseInt(expected.get(key))))
        );
        assertThat(actual.optJSONArray("results").toString(), is(expected.get("results")));
    }

}
