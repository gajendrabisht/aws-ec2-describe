# AWS EC2 Describe API
- Lists all active EC2 instances
- EC2 attributes displayed
  - Name - Display Name
  - Id - Unique Identifier
  - Type - Size eg. t2.micro etc
  - State - Running, terminated
  - Availability Zone
  - Public IP Address
  - Private IP Address
- Can sort based on the above list of attributes
- Can sort in ascending (default) and descending mode
- Results are paged (defaults - page=1, pageSize=10)

## Testing
- run command './gradlew clean test'
- test report can be found on '.../aws-ec2-describe/build/reports/tests/test/index.html'

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

## Deploy to AWS Beanstalk
- Must have AWS CLI setup locally
- Beanstalk environment created already as '.elasticbeanstalk/config.yml'
- Run on CLI 'eb create -s'
- select environment name or leave blank
- select cname prefix or leave blank
- Application available at URL displayed in terminal eg. 'aws-ec2-describe-dev.us-west-2.elasticbeanstalk.com'
- Go to 'http://<DOMAIN_URL>/swagger-ui.html'
- Try it out!
