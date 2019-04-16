package com.cisco.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.cisco.config.AwsConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CredentialsProviderTest {

    @Mock
    AwsConfig awsConfig;

    @InjectMocks
    CredentialsProvider credentialsProvider;

    @Test
    public void shouldReturnStaticCredentialsProvider() {
        given(awsConfig.getAccessKey()).willReturn("someAccessKey");
        given(awsConfig.getSecretKey()).willReturn("someSecretKey");

        AWSCredentialsProvider awsCredentialsProvider = credentialsProvider.get();

        assertThat(awsCredentialsProvider.getCredentials().getAWSAccessKeyId(), is("someAccessKey"));
        assertThat(awsCredentialsProvider.getCredentials().getAWSSecretKey(), is("someSecretKey"));
    }

}