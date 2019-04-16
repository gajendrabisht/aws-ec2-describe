package com.cisco.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.amazonaws.regions.Regions.EU_WEST_2;
import static com.amazonaws.regions.Regions.US_EAST_1;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class Ec2ClientProviderTest {

    @Mock
    CredentialsProvider credentialsProvider;

    @InjectMocks
    Ec2ClientProvider ec2ClientProvider;

    @Test
    public void shouldReturnSameClientForRegionForMultipleCalls() {
        AWSCredentialsProvider credentials = mock(AWSCredentialsProvider.class);
        given(credentialsProvider.get()).willReturn(credentials);

        AmazonEC2 clientOne = ec2ClientProvider.getClient(US_EAST_1);
        AmazonEC2 clientTwo = ec2ClientProvider.getClient(US_EAST_1);

        assertThat(clientOne, is(clientTwo));
        verify(credentialsProvider, times(1)).get();
    }

    @Test
    public void shouldReturnDifferentClientsForDifferentRegions() {
        AWSCredentialsProvider credentials = mock(AWSCredentialsProvider.class);
        given(credentialsProvider.get()).willReturn(credentials);

        AmazonEC2 clientOne = ec2ClientProvider.getClient(US_EAST_1);
        AmazonEC2 clientTwo = ec2ClientProvider.getClient(EU_WEST_2);

        assertNotEquals(clientOne, clientTwo);
    }
}