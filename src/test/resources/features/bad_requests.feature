Feature: Bad requests

  Scenario: Region invalid
    When I request ec2s with query params as
      | region        |
      | invalidRegion |
    Then I should get response status 400
    And I should get error message '{"errors":["No matching Region found for 'invalidRegion'. Allowed values: [us-gov-west-1, us-gov-east-1, us-east-1, us-east-2, us-west-1, us-west-2, eu-west-1, eu-west-2, eu-west-3, eu-central-1, eu-north-1, ap-south-1, ap-southeast-1, ap-southeast-2, ap-northeast-1, ap-northeast-2, sa-east-1, cn-north-1, cn-northwest-1, ca-central-1]."]}'

  Scenario: Sort By Attribute Invalid
    When I request ec2s with query params as
      | sortBy        |
      | invalidSortBy |
    Then I should get response status 400
    And I should get error message '{"errors":["No matching EC2 attribute found for 'invalidSortBy'. Allowed values: [name, id, type, state, availabilityZone, publicIp, privateIp]."]}'

  Scenario: Page must be >= 1
    When I request ec2s with query params as
      | page |
      | 0    |
    Then I should get response status 400
    And I should get error message '{"errors":["getAll.page: must be greater than or equal to 1"]}'

  Scenario: Page Size must be >= 1
    When I request ec2s with query params as
      | pageSize |
      | 0        |
    Then I should get response status 400
    And I should get error message '{"errors":["getAll.pageSize: must be greater than or equal to 1"]}'

  Scenario: Page must be <= 1000
    When I request ec2s with query params as
      | page |
      | 1001 |
    Then I should get response status 400
    And I should get error message '{"errors":["getAll.page: must be less than or equal to 1000"]}'

  Scenario: Page Size must be <= 100
    When I request ec2s with query params as
      | pageSize |
      | 101      |
    Then I should get response status 400
    And I should get error message '{"errors":["getAll.pageSize: must be less than or equal to 100"]}'

  Scenario: Page number out of bounds - when no results found
    When I request ec2s with query params as
      | page |
      | 2    |
    Then I should get response status 400
    And I should get error message '{"errors":["Page number out of bounds"]}'

