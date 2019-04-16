# AWS EC2 Describe API
- Lists all active EC2 instances
- EC2 attributes displayed
  - Name - Display Name
  - Id - Unique Identifier
  - Type - Size eg. t2.micro etc.
  - State - Running, terminated etc.
  - Availability Zone
  - Public IP Address
  - Private IP Address
- Can sort based on the above list of attributes
- Can sort in ascending (default) and descending mode
- Results are paged (defaults - page=1, pageSize=10)

## Testing
- run command './gradlew clean test'
- TDD, test report can be found on '.../aws-ec2-describe/build/reports/tests/test/index.html'
- BDD, Cucumber, report can be found on '.../aws-ec2-describe/cucumber-report/index.html'

## Run Locally (with aws access)
NOTE: Ideally access to applications should be controlled by custom access policies
but doing via user credentials as i'm short on time
- Login to AWS Console with Admin privileges (ability to create users and groups)
- Create Group with name 'ec2-read-only-group'
- Attach policy 'AmazonEC2ReadOnlyAccess' to the above group
- Create User 'ec2-read-only-user' with 'Programmatic access' selected
- Add User 'ec2-read-only-user' to group 'ec2-read-only-group'
- Obtain the access key and secret key and replace with placeholders in 'src/main/resources/application.yml'
- Run on CLI './gradlew bootRun'
- Go to 'http://localhost:5000/swagger-ui.html'
- Try it out!

## Deploy to AWS Elastic Beanstalk
- Must have AWS CLI setup locally
- Beanstalk environment created already as '.elasticbeanstalk/config.yml'
- Build a JAR file by running './gradlew clean build'
- Run on CLI 'eb create -s'
- select environment name or leave blank
- select cname prefix or leave blank
- Application available at URL displayed in terminal eg. 'aws-ec2-describe-dev.us-west-2.elasticbeanstalk.com'
- Go to 'http://<DOMAIN_URL>/swagger-ui.html'
- Try it out!


# TODO's (Not enough time, limited access to AWS free tier services)

## Securing Api using API Gateway
- Create a VPC with Private Subnet
- Create Elastic beanstalk environment with Java and configure
- Use Network Load Balancer and attach to VPC
- Load balancer setting visibility 'Internal' to make service not visible to outside world
- Select Private Subnet for Network load balancer
- Create API Gateway
- Create VPC Links to API Gateway
- Select Target Network Load Balancer created in previous step
- Access through API url 

## Controlling access through API Gateway by one or more strategies 
- API Resource policies
- Lambda Authorizers - eg. JWT (Json Web Token)
- Amazon Cognito User Pools Authorizer
- API Keys for rate limiting and monitoring usage per customer
