package com.cisco.cucumber;

import com.cisco.Application;
import com.cisco.service.Ec2Service;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
@ContextConfiguration
public class CucumberTestApplication {

    @MockBean
    protected Ec2Service ec2Service;

    @LocalServerPort
    private int port = 8080;

    protected TestRestTemplate restTemplate = new TestRestTemplate();

    public String baseUrl() {
        return String.format("http://localhost:%s", port);
    }

}
