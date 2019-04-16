Feature: Get list of EC2 instances within a region

  Scenario: No Instances found - Return empty list
    When I request ec2s with query params as
      | region    |
      | us-east-1 |
    Then I should get response status 200
    And I should get response body as
      | resultCount | page | pageSize | results |
      | 0           | 1    | 10       | []      |

  Scenario: # TODO: Use @MockBean or @SpyBean to mock ec2Service, Open Issue : https://github.com/cucumber/cucumber-jvm/issues/1470